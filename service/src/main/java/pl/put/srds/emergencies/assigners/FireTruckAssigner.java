package pl.put.srds.emergencies.assigners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.cassandra.CassandraInsufficientReplicasAvailableException;
import org.springframework.data.cassandra.CassandraReadTimeoutException;
import org.springframework.stereotype.Service;
import pl.put.srds.emergencies.assigners.exceptions.UnableToAssignException;
import pl.put.srds.emergencies.assigners.exceptions.UnableToReleaseException;
import pl.put.srds.emergencies.dbmodel.Assignment;
import pl.put.srds.emergencies.dbmodel.FireTruck;
import pl.put.srds.emergencies.dbmodel.FireTruckKey;
import pl.put.srds.emergencies.dbmodel.repository.EmergenciesRepository;
import pl.put.srds.emergencies.dbmodel.repository.FireTruckRepository;
import pl.put.srds.emergencies.generated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FireTruckAssigner {

    private final EmergenciesRepository repository;
    private final FireTruckRepository fireTruckRepository;

    private Random r = new Random();
    private static final int MAX_TRIES_IN_GETTING_FREE_TRUCK = 10; // TODO do configa z tym
    private static final int MAX_TRIES_IN_ASSIGNING_TRUCK = 10; // TODO do configa z tym
    private static final int MAX_FIRE_BRIGADE_ID = 1000;  // TODO do configa z tym

    public EmergenciesRequestConfirmation assignVehicles(EmergenciesRequest request){
        EmergenciesRequestConfirmation.Builder b = EmergenciesRequestConfirmation.newBuilder();

        String requestId = UUID.randomUUID().toString();
        b.setRequestId(requestId);

        try {
            for (int bId: request.getGBABrigadesList()) {
                assignVehicleAndAddToMessage(bId, 1, requestId, b);
            }

            for (int bId: request.getGCBABrigadesList()) {
                assignVehicleAndAddToMessage(bId, 2, requestId, b);
            }

            for (int bId: request.getGLBABrigadesList()) {
                assignVehicleAndAddToMessage(bId, 3, requestId, b);
            }

            for (int bId: request.getSDBrigadesList()) {
                assignVehicleAndAddToMessage(bId, 4, requestId, b);
            }

            for (int bId: request.getSCRtBrigadesList()) {
                assignVehicleAndAddToMessage(bId, 5, requestId, b);
            }

            for (int bId: request.getSRdBrigadesList()) {
                assignVehicleAndAddToMessage(bId, 6, requestId, b);
            }
        } catch (UnableToAssignException e) {
            log.error(String.format("Unable to process request due to: %s", e.getMessage()), e);
            b.clear();
            b.setRequestId("-1");

            for (FireTruckAssignment fta : b.getAssignedTrucksList()) {
                try {
                    releaseTruck(fta, requestId);
                } catch (UnableToReleaseException | DataAccessException releasingException) {
                    log.error(String.format("Unable to process request due to: %s", releasingException.getMessage()), releasingException);
                }
            }
            b.clearAssignedTrucks();
        }

        return b.build();
    }

    public EmergenciesReleasingConfirmation releaseVehicles(EmergenciesReleasing request){
        List<FireTruckAssignment> releasedTrucks = new ArrayList<>();
        try {
            for (FireTruckAssignment fta : request.getAssignedTrucksList()) {
                releaseTruck(fta, request.getRequestId());
                releasedTrucks.add(fta);
            }
            return EmergenciesReleasingConfirmation.newBuilder()
                    .setSucceeded(true)
                    .addAllReleasedTrucks(releasedTrucks)
                    .build();
        }
        catch (DataAccessException | UnableToReleaseException e) {
            log.error(String.format("Unable to process release due to: %s", e.getMessage()), e);
            return EmergenciesReleasingConfirmation.newBuilder()
                    .setSucceeded(false)
                    .addAllReleasedTrucks(releasedTrucks)
                    .build();
        }
    }

    private void releaseTruck(FireTruckAssignment fta, String requestId) throws UnableToReleaseException {
        FireTruckKey ftk = new FireTruckKey();
        ftk.setBrigadeId(fta.getBrigadeId());
        ftk.setTypeId(fta.getTypeId());

        FireTruck ft = new FireTruck();
        ft.setKey(ftk);
        ft.setTruckId(fta.getTruckId());

        boolean succeeded = false;
        int i = 0;
        do {
            succeeded = repository.releaseAssignment(ft, requestId);

            if (!succeeded){
                log.warn("Could not apply batch");
            } else  {
                break;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            i++;
        }while(!succeeded && i < 10);

        if (!succeeded) {
            throw new UnableToReleaseException("Batch cannot be applied!");
        }
    }

    private FireTruck getFreeFireTruck(int brigadeId, int typeId) throws UnableToAssignException {
        int erroneousIterations = 0;
        FireTruck ft;
        do {
            try {
                ft = fireTruckRepository.findFirstByKeyBrigadeIdAndKeyTypeIdAndAssigned(brigadeId, typeId, false);
            }
            catch (CassandraInsufficientReplicasAvailableException | CassandraReadTimeoutException e) {
                log.error(String.format("Unable to process request due to: %s", e.getMessage()), e);
                erroneousIterations++;
                ft = null;
            }

            if (ft == null) {
                brigadeId = getRandomBrigadeId();
            }

        } while (erroneousIterations < MAX_TRIES_IN_GETTING_FREE_TRUCK && ft == null);

        if (ft == null)
        {
            throw new UnableToAssignException("Unable to find free vehicle");
        }

        return ft;
    }

    private FireTruck assignVehicle(int brigadeId, int typeId, String requestId) throws UnableToAssignException {
        FireTruck ft;
        int iterations = 0;

        do {
            ft = getFreeFireTruck(brigadeId, typeId);
            try {
                Assignment a = repository.makeAssignment(ft, requestId);
                if (a == null) {
                    ft = null;
                }
                else if (!repository.hasFirstAssignment(a)) {
                    ft = null;
                    repository.rollbackAssignment(a);
                }
            }
            catch (DataAccessException e) {
                log.error(String.format("Unable to process request due to: %s", e.getMessage()), e);
                iterations++;
                ft = null;
            }
        } while(iterations < MAX_TRIES_IN_ASSIGNING_TRUCK && ft == null);

        if (ft == null) {
            throw new  UnableToAssignException("Unable to assign vehicle");
        }

        return  ft;
    }

    private void assignVehicleAndAddToMessage(int brigadeId, int typeId, String requestId, EmergenciesRequestConfirmation.Builder b) throws UnableToAssignException {
        FireTruck assignedVehicle = assignVehicle(brigadeId, typeId, requestId);
        b.addAssignedTrucks(FireTruckAssignment.newBuilder()
                .setTruckId(assignedVehicle.getTruckId())
                .setBrigadeId(assignedVehicle.getKey().getBrigadeId())
                .setTypeId(assignedVehicle.getKey().getTypeId())
                .build());
    }

    private int getRandomBrigadeId(){
        return r.nextInt(MAX_FIRE_BRIGADE_ID) + 1;
    }
}

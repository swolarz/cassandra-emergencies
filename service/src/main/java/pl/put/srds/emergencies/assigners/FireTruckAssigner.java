package pl.put.srds.emergencies.assigners;

import org.springframework.beans.factory.annotation.Autowired;
import pl.put.srds.emergencies.dbmodel.Assignment;
import pl.put.srds.emergencies.dbmodel.FireTruck;
import pl.put.srds.emergencies.dbmodel.FireTruckKey;
import pl.put.srds.emergencies.dbmodel.repository.EmergenciesRepositoryImpl;
import pl.put.srds.emergencies.dbmodel.repository.FireTruckRepository;
import pl.put.srds.emergencies.generated.*;

import java.util.Random;
import java.util.UUID;

public class FireTruckAssigner {

    @Autowired
    private EmergenciesRepositoryImpl repository;

    @Autowired
    private FireTruckRepository fireTruckRepository;

    private Random r = new Random();
    private static final int MAX_TRIES_IN_GETTING_FREE_TRUCK = 10; // TODO do configa z tym
    private static final int MAX_TRIES_IN_ASSIGNING_TRUCK = 10; // TODO do configa z tym
    private static final int MAX_FIRE_BRIGADE_ID = 1000;  // TODO do configa z tym

    public EmergenciesRequestConfirmation assignVehicles(EmergenciesRequest request){
        EmergenciesRequestConfirmation.Builder b = EmergenciesRequestConfirmation.newBuilder();

        String requestId = UUID.randomUUID().toString();
        b.setRequestId(requestId);

        request.getGBABrigadesList().forEach(bId -> assignVehicleAndAddToMessage(bId, 1, requestId, b));
        request.getGCBABrigadesList().forEach(bId -> assignVehicleAndAddToMessage(bId, 2, requestId, b));
        request.getGLBABrigadesList().forEach(bId -> assignVehicleAndAddToMessage(bId, 3, requestId, b));
        request.getSDBrigadesList().forEach(bId -> assignVehicleAndAddToMessage(bId, 4, requestId, b));
        request.getSCRtBrigadesList().forEach(bId -> assignVehicleAndAddToMessage(bId, 5, requestId, b));
        request.getSRdBrigadesList().forEach(bId -> assignVehicleAndAddToMessage(bId, 6, requestId, b));

        return b.build();
    }

    public EmergenciesReleasingConfirmation releaseVehicles(EmergenciesReleasing request){
        request.getAssignedTrucksList().forEach(fta -> {
            FireTruckKey ftk = new FireTruckKey();
            ftk.setBrigadeId(fta.getBrigadeId());
            ftk.setTypeId(fta.getTruckId());

            FireTruck ft = new FireTruck();
            ft.setKey(ftk);
            ft.setTruckId(fta.getTruckId());

            repository.releaseAssignment(ft, request.getRequestId());
            //TODO dodac lapanie wyjatkow i obsluge bledow - w tym np. zwolnienie zwolnionego pojazdu!!!
        });

        return EmergenciesReleasingConfirmation.newBuilder().setSucceeded(true).build();
    }

    private FireTruck getFreeFireTruck(int brigadeId, int typeId) {
        int iterations = 0;
        FireTruck ft;
        do {
            iterations++;

            ft = fireTruckRepository.findFirstByKeyBrigadeIdAndKeyTypeIdAndKeyIsAssigned(brigadeId, typeId, false);
            if (ft == null) {
                brigadeId = getRandomBrigadeId();
            }

        } while (iterations < MAX_TRIES_IN_GETTING_FREE_TRUCK && ft == null);

        if (ft == null)
        {
            //TODO dodanie wyjatku tutaj
        }

        return ft;
    }

    private FireTruck assignVehicle(int brigadeId, int typeId, String requestId){
        FireTruck ft;
        int iterations = 0;

        do {
            iterations++;

            ft = getFreeFireTruck(brigadeId, typeId);
            Assignment a = repository.makeAssignment(ft, requestId);
            if(!repository.hasFirstAssignment(a))
            {
                repository.rollbackAssignment(a);
                ft = null;
            }
        } while(iterations < MAX_TRIES_IN_ASSIGNING_TRUCK && ft == null);

        if (ft == null) {
            //TODO tu dodac wyjatek
        }

        return  ft;
    }

    private void assignVehicleAndAddToMessage(int brigadeId, int typeId, String requestId, EmergenciesRequestConfirmation.Builder b){
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

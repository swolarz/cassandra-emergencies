package pl.put.srds.emergencies.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.put.srds.emergencies.assigners.FireTruckAssigner;
import pl.put.srds.emergencies.generated.EmergenciesReleasing;
import pl.put.srds.emergencies.generated.EmergenciesReleasingConfirmation;
import pl.put.srds.emergencies.generated.EmergenciesRequest;
import pl.put.srds.emergencies.generated.EmergenciesRequestConfirmation;


@Service
@Slf4j
public class EmergenciesCoordinatorService extends pl.put.srds.emergencies.generated.EmergenciesCoordinatorGrpc.EmergenciesCoordinatorImplBase {
    private final FireTruckAssigner assigner;

    public EmergenciesCoordinatorService(FireTruckAssigner assigner) {
        this.assigner = assigner;
    }

    @Override
    public void releaseEmergencies(EmergenciesReleasing request, StreamObserver<EmergenciesReleasingConfirmation> responseObserver) {
        responseObserver.onNext(handleReleaseEmergencies(request));
        responseObserver.onCompleted();
    }

    @Override
    public void requestEmergencies(EmergenciesRequest request, StreamObserver<EmergenciesRequestConfirmation> responseObserver) {
        responseObserver.onNext(handleRequestEmergencies(request));
        responseObserver.onCompleted();
    }

    private EmergenciesRequestConfirmation handleRequestEmergencies(EmergenciesRequest request) {
        log.info("Before handling request");
        EmergenciesRequestConfirmation requestConfirmation = assigner.assignVehicles(request);
        if (!requestConfirmation.getRequestId().equals("-1")) {
            log.info(String.format("After succeeded handling request of id = %s", requestConfirmation.getRequestId()));
        } else {
            log.error(String.format("After failed handling request of id = %s", requestConfirmation.getRequestId()));
        }

        return requestConfirmation;
    }

    private EmergenciesReleasingConfirmation handleReleaseEmergencies(EmergenciesReleasing request) {
        log.info(String.format("Before handling release of id = %s", request.getRequestId()));
        EmergenciesReleasingConfirmation response =  assigner.releaseVehicles(request);
        if (response.getSucceeded()) {
            log.info(String.format("After succeeded handling release of id = %s", request.getRequestId()));
        } else {
            log.error(String.format("After failed handling release of id = %s", request.getRequestId()));
        }

        return response;
    }
}

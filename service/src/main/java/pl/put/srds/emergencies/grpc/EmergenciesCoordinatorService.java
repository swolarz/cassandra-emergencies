package pl.put.srds.emergencies.grpc;

import io.grpc.stub.StreamObserver;
import pl.put.srds.emergencies.assigners.FireTruckAssigner;
import pl.put.srds.emergencies.generated.EmergenciesReleasing;
import pl.put.srds.emergencies.generated.EmergenciesReleasingConfirmation;
import pl.put.srds.emergencies.generated.EmergenciesRequest;
import pl.put.srds.emergencies.generated.EmergenciesRequestConfirmation;

class EmergenciesCoordinatorService extends pl.put.srds.emergencies.generated.EmergenciesCoordinatorGrpc.EmergenciesCoordinatorImplBase {
    private FireTruckAssigner assigner = new FireTruckAssigner();

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
        return assigner.assignVehicles(request);
    }

    private EmergenciesReleasingConfirmation handleReleaseEmergencies(EmergenciesReleasing request) {
        return assigner.releaseVehicles(request);
    }
}

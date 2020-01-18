package pl.put.srds.emergencies.grpc;

import io.grpc.stub.StreamObserver;
import pl.put.srds.emergencies.generated.EmergenciesReleasing;
import pl.put.srds.emergencies.generated.EmergenciesReleasingConfirmation;
import pl.put.srds.emergencies.generated.EmergenciesRequest;
import pl.put.srds.emergencies.generated.EmergenciesRequestConfirmation;

class EmergenciesCoordinatorService extends pl.put.srds.emergencies.generated.EmergenciesCoordinatorGrpc.EmergenciesCoordinatorImplBase {
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
        return EmergenciesRequestConfirmation.newBuilder().setRequestId("1").build();
    }

    private EmergenciesReleasingConfirmation handleReleaseEmergencies(EmergenciesReleasing request) {
        return EmergenciesReleasingConfirmation.newBuilder().build();
    }
}

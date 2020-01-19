package pl.put.srds.emergencies.grpc;

import io.grpc.stub.StreamObserver;
import pl.put.srds.emergencies.generated.*;

import java.util.LinkedList;
import java.util.Random;

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
        Random r  = new Random();

        if (r.nextBoolean()) {
            LinkedList<FireTruckAssignment> assignments = new LinkedList<>();

            request.getGBABrigadesList().forEach(e ->
                    assignments.add(FireTruckAssignment.newBuilder()
                            .setBrigadeId(e)
                            .setTruckId(2)
                            .setTypeId(1)
                            .build()));

            request.getGCBABrigadesList().forEach(e ->
                    assignments.add(FireTruckAssignment.newBuilder()
                            .setBrigadeId(e)
                            .setTruckId(2)
                            .setTypeId(2)
                            .build()));

            request.getGLBABrigadesList().forEach(e ->
                    assignments.add(FireTruckAssignment.newBuilder()
                            .setBrigadeId(e)
                            .setTruckId(2)
                            .setTypeId(3)
                            .build()));

            request.getSCRtBrigadesList().forEach(e ->
                    assignments.add(FireTruckAssignment.newBuilder()
                            .setBrigadeId(e)
                            .setTruckId(2)
                            .setTypeId(4)
                            .build()));

            request.getSDBrigadesList().forEach(e ->
                    assignments.add(FireTruckAssignment.newBuilder()
                            .setBrigadeId(e)
                            .setTruckId(2)
                            .setTypeId(5)
                            .build()));

            request.getSRdBrigadesList().forEach(e ->
                    assignments.add(FireTruckAssignment.newBuilder()
                            .setBrigadeId(e)
                            .setTruckId(2)
                            .setTypeId(6)
                            .build()));

            return EmergenciesRequestConfirmation.newBuilder()
                    .setRequestId("1")
                    .addAllAssignedTrucks(assignments)
                    .build();
        }
        else {
            return EmergenciesRequestConfirmation.newBuilder()
                    .setRequestId("-1")
                    .build();
        }
    }

    private EmergenciesReleasingConfirmation handleReleaseEmergencies(EmergenciesReleasing request) {
        return EmergenciesReleasingConfirmation.newBuilder().build();
    }
}

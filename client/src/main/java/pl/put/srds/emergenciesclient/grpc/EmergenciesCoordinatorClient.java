package pl.put.srds.emergenciesclient.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import pl.put.srds.emergencies.generated.*;
import pl.put.srds.emergenciesclient.grpc.generator.RequestsGenerator;

import java.util.concurrent.TimeUnit;

public class EmergenciesCoordinatorClient {

    private final ManagedChannel channel;
    private final EmergenciesCoordinatorGrpc.EmergenciesCoordinatorBlockingStub blockingStub;
    private final EmergenciesCoordinatorGrpc.EmergenciesCoordinatorStub asyncStub;
    private static RequestsGenerator generator = new RequestsGenerator();

    public EmergenciesCoordinatorClient(String host, int port){
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    private EmergenciesCoordinatorClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = EmergenciesCoordinatorGrpc.newBlockingStub(channel);
        asyncStub = EmergenciesCoordinatorGrpc.newStub(channel);
    }

    public void requestForEmergenciesAsync() {
        EmergenciesRequest request = generator.GenerateRequest();
        StreamObserver<EmergenciesRequestConfirmation> observer = new StreamObserver<>() {
            @Override
            public void onNext(EmergenciesRequestConfirmation value) {
                System.out.println(value.getRequestId());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                System.out.println("ERROR");
            }

            @Override
            public void onCompleted() {
                System.out.println("FINISHED");
            }
        };
        asyncStub.requestEmergencies(request, observer);
    }

    public void releaseEmergenciesAsync(String requestId) {
        EmergenciesReleasing releaseRequest = EmergenciesReleasing.newBuilder().setRequestId(requestId).build();
        StreamObserver<EmergenciesReleasingConfirmation> observer = new StreamObserver<>() {
            @Override
            public void onNext(EmergenciesReleasingConfirmation value) {
                System.out.println("ZWOLNIONE");
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                System.out.println("ERROR");
            }

            @Override
            public void onCompleted() {
                System.out.println("FINISHED");
            }
        };
        asyncStub.releaseEmergencies(releaseRequest, observer);
    }

    public String requestForEmergenciesSync() {
        EmergenciesRequest request =  generator.GenerateRequest();
        return blockingStub.requestEmergencies(request).getRequestId();
    }

    public void releaseEmergenciesSync(String requestId) {
        EmergenciesReleasing releaseRequest = EmergenciesReleasing.newBuilder().setRequestId(requestId).build();
        blockingStub.releaseEmergencies(releaseRequest);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}

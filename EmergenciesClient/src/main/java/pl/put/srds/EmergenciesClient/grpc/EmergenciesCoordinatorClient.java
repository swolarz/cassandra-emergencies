package pl.put.srds.EmergenciesClient.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import pl.put.srds.emergencies.generated.EmergenciesCoordinatorGrpc;
import pl.put.srds.emergencies.generated.EmergenciesRequestConfirmation;

import java.util.concurrent.TimeUnit;

public class EmergenciesCoordinatorClient {

    private final ManagedChannel channel;
    private final EmergenciesCoordinatorGrpc.EmergenciesCoordinatorBlockingStub blockingStub;
    private final EmergenciesCoordinatorGrpc.EmergenciesCoordinatorStub asyncStub;

    public EmergenciesCoordinatorClient(String host, int port){
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    private EmergenciesCoordinatorClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = EmergenciesCoordinatorGrpc.newBlockingStub(channel);
        asyncStub = EmergenciesCoordinatorGrpc.newStub(channel);
    }

    public void requestForEmergenciesAsync() {
        pl.put.srds.emergencies.generated.EmergenciesRequest request = pl.put.srds.emergencies.generated.EmergenciesRequest.newBuilder().setGBAAmount(2).setGCBAAmount(1).build();
        StreamObserver<pl.put.srds.emergencies.generated.EmergenciesRequestConfirmation> observer = new StreamObserver<>() {
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
        pl.put.srds.emergencies.generated.EmergenciesReleasing releaseRequest = pl.put.srds.emergencies.generated.EmergenciesReleasing.newBuilder().setRequestId(requestId).build();
        StreamObserver<pl.put.srds.emergencies.generated.EmergenciesReleasingConfirmation> observer = new StreamObserver<>() {
            @Override
            public void onNext(pl.put.srds.emergencies.generated.EmergenciesReleasingConfirmation value) {
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
        pl.put.srds.emergencies.generated.EmergenciesRequest request = pl.put.srds.emergencies.generated.EmergenciesRequest.newBuilder().setGBAAmount(2).setGCBAAmount(1).build();
        return blockingStub.requestEmergencies(request).getRequestId();
    }

    public void releaseEmergenciesSync(String requestId) {
        pl.put.srds.emergencies.generated.EmergenciesReleasing releaseRequest = pl.put.srds.emergencies.generated.EmergenciesReleasing.newBuilder().setRequestId(requestId).build();
        blockingStub.releaseEmergencies(releaseRequest);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}

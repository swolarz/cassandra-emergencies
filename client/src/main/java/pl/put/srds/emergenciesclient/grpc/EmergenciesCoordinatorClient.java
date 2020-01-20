package pl.put.srds.emergenciesclient.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import pl.put.srds.emergencies.generated.*;
import pl.put.srds.emergenciesclient.grpc.generator.RequestsGenerator;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        PrintRequestedFireTrucks(request);
        return requestForEmergenciesSync(request);
    }

    private String requestForEmergenciesSync(EmergenciesRequest request) {
        EmergenciesRequestConfirmation response = blockingStub.requestEmergencies(request);

        if (response.getRequestId().equals("-1"))
        {
            System.out.println("ERROR: Resend request in a while");
            Random r = new Random();
            try {
                Thread.sleep((r.nextInt(5) + 1) * 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return requestForEmergenciesSync(request);
        }
        else
        {
            PrintAssignedFireTrucks(response);
        }

        return response.getRequestId();
    }

    public void releaseEmergenciesSync(String requestId) {
        EmergenciesReleasing releaseRequest = EmergenciesReleasing.newBuilder().setRequestId(requestId).build();
        blockingStub.releaseEmergencies(releaseRequest);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private void PrintRequestedFireTrucks(EmergenciesRequest request) {
        System.out.println("REQUESTED FIRE TRUCKS:");
        System.out.println("GBA:");
        PrintList(request.getGBABrigadesList());
        System.out.println("GCBA:");
        PrintList(request.getGCBABrigadesList());
        System.out.println("GLBA:");
        PrintList(request.getGLBABrigadesList());
        System.out.println("SD:");
        PrintList(request.getSDBrigadesList());
        System.out.println("SCRt:");
        PrintList(request.getSCRtBrigadesList());
        System.out.println("SRd:");
        PrintList(request.getSRdBrigadesList());
    }

    private void PrintAssignedFireTrucks(EmergenciesRequestConfirmation response) {
        System.out.println("ASSIGNED FIRE TRUCKS:");
        System.out.println("GBA:");
        PrintList(response.getAssignedTrucksList().stream().filter(e -> e.getTypeId() == 1).map(FireTruckAssignment::getBrigadeId).collect(Collectors.toList()));
        System.out.println("GCBA:");
        PrintList(response.getAssignedTrucksList().stream().filter(e -> e.getTypeId() == 2).map(FireTruckAssignment::getBrigadeId).collect(Collectors.toList()));
        System.out.println("GLBA:");
        PrintList(response.getAssignedTrucksList().stream().filter(e -> e.getTypeId() == 3).map(FireTruckAssignment::getBrigadeId).collect(Collectors.toList()));
        System.out.println("SD:");
        PrintList(response.getAssignedTrucksList().stream().filter(e -> e.getTypeId() == 4).map(FireTruckAssignment::getBrigadeId).collect(Collectors.toList()));
        System.out.println("SCRt:");
        PrintList(response.getAssignedTrucksList().stream().filter(e -> e.getTypeId() == 5).map(FireTruckAssignment::getBrigadeId).collect(Collectors.toList()));
        System.out.println("SRd:");
        PrintList(response.getAssignedTrucksList().stream().filter(e -> e.getTypeId() == 6).map(FireTruckAssignment::getBrigadeId).collect(Collectors.toList()));
    }

    private void PrintList(List<Integer> list) {
        list.forEach(entry -> System.out.print("\t" + entry));
        System.out.println();
    }
}

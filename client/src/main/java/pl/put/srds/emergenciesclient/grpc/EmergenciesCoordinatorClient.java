package pl.put.srds.emergenciesclient.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pl.put.srds.emergencies.generated.*;
import pl.put.srds.emergenciesclient.RequestCounter;
import pl.put.srds.emergenciesclient.configs.ClientProperties;
import pl.put.srds.emergenciesclient.grpc.generator.RequestsGenerator;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EmergenciesCoordinatorClient {

    private final ManagedChannel channel;
    private final EmergenciesCoordinatorGrpc.EmergenciesCoordinatorBlockingStub blockingStub;
    private final Random random = new Random();
    private static RequestsGenerator generator = new RequestsGenerator();

    public EmergenciesCoordinatorClient(String host, int port){
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    private EmergenciesCoordinatorClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = EmergenciesCoordinatorGrpc.newBlockingStub(channel);
    }

    public void handleTheAlert() {
        EmergenciesRequest request =  generator.GenerateRequest();

        EmergenciesRequestConfirmation response = requestForEmergenciesSync(request);

        waitAlertTime();

        releaseEmergenciesSync(response);
    }

    private EmergenciesRequestConfirmation requestForEmergenciesSync(EmergenciesRequest request) {
        EmergenciesRequestConfirmation response;
        boolean loopSucceeded = false;
        do {
            response = blockingStub.requestEmergencies(request);
            RequestCounter.increment();

            if (response.getRequestId().equals("-1")) {
                System.out.println("ERROR: Resend request in a while");
                waitRetransmissionTime();
            }
            else {
                loopSucceeded = true;
            }
        } while (!loopSucceeded);

        return response;
    }

    private void releaseEmergenciesSync(EmergenciesRequestConfirmation requestConfirmation) {
        EmergenciesReleasing releaseRequest = EmergenciesReleasing.newBuilder()
                .setRequestId(requestConfirmation.getRequestId())
                .addAllAssignedTrucks(requestConfirmation.getAssignedTrucksList())
                .build();

        EmergenciesReleasingConfirmation releasingConfirmation;
        List<FireTruckAssignment> vehiclesToRelease = requestConfirmation.getAssignedTrucksList();
        boolean loopSucceeded = false;
        do {
            releasingConfirmation = blockingStub.releaseEmergencies(releaseRequest);
            RequestCounter.increment();

            if (!releasingConfirmation.getSucceeded()) {
                vehiclesToRelease.removeAll(releasingConfirmation.getReleasedTrucksList());

                if (vehiclesToRelease.isEmpty()) {
                    loopSucceeded = true;
                } else {
                    releaseRequest = EmergenciesReleasing.newBuilder()
                            .setRequestId(requestConfirmation.getRequestId())
                            .addAllAssignedTrucks(vehiclesToRelease)
                            .build();
                    System.out.println("ERROR: Resend releasing in a while");
                    waitRetransmissionTime();
                }
            }
            else {
                loopSucceeded = true;
            }
        } while (!loopSucceeded);
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

    private void waitAlertTime() {
        Sleep(ClientProperties.getBaseAlertMultiplier(), ClientProperties.getMaxAlertMultiplier(), ClientProperties.getAlertTimeToMultiply());
    }

    private void waitRetransmissionTime() {
        Sleep(ClientProperties.getBaseRetransmissionMultiplier(), ClientProperties.getMaxRetransmissionMultiplier(), ClientProperties.getRetransmissionTimeToMultiply());
    }

    private void Sleep(int base, int bound, int multiplier) {
        try {
            Thread.sleep(getNextRandom(base, bound, multiplier));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private int getNextRandom(int base, int bound, int multiplier) {
        return (random.nextInt(bound) + base) * multiplier;
    }
}

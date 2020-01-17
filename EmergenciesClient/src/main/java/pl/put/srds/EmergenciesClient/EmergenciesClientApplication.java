package pl.put.srds.EmergenciesClient;


import pl.put.srds.EmergenciesClient.grpc.EmergenciesCoordinatorClient;

public class EmergenciesClientApplication {
    public static void main(String[] args) throws InterruptedException {
        EmergenciesCoordinatorClient client = new EmergenciesCoordinatorClient("localhost", 12345);
        client.requestForEmergencies();
        client.releaseEmergencies("1");

        Thread.sleep(2000);

        System.out.println("EXIT");
        client.shutdown();
    }
}

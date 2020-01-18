package pl.put.srds.EmergenciesClient;


import pl.put.srds.EmergenciesClient.grpc.EmergenciesCoordinatorClient;

public class EmergenciesClientApplication {
    public static void main(String[] args) throws InterruptedException {
        EmergenciesCoordinatorClient client = new EmergenciesCoordinatorClient("localhost", 12345);

        client.requestForEmergenciesAsync();
        client.releaseEmergenciesAsync("1");

        try
        {
            String rId = client.requestForEmergenciesSync();
            System.out.println(rId);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        try
        {
            client.releaseEmergenciesSync("1");
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

        Thread.sleep(2000);

        System.out.println("EXIT");
        client.shutdown();
    }
}

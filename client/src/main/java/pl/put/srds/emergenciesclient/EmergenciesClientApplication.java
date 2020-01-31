package pl.put.srds.emergenciesclient;


import pl.put.srds.emergenciesclient.configs.ClientProperties;
import pl.put.srds.emergenciesclient.grpc.EmergenciesCoordinatorClient;

public class EmergenciesClientApplication {
    public static void main(String[] args) throws InterruptedException {
        EmergenciesCoordinatorClient client =
                new EmergenciesCoordinatorClient(ClientProperties.getHost(), ClientProperties.getPort());
        try {
            while (true) {
                client.handleTheAlert();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            client.shutdown();
        }
    }
}

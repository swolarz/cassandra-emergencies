package pl.put.srds.emergenciesclient;


import pl.put.srds.emergenciesclient.configs.ClientProperties;
import pl.put.srds.emergenciesclient.grpc.EmergenciesCoordinatorClient;

import java.util.Random;

public class EmergenciesClientApplication {
    public static void main(String[] args) throws InterruptedException {
        EmergenciesCoordinatorClient client = new EmergenciesCoordinatorClient(ClientProperties.getHost(), ClientProperties.getPort());
        Random r = new Random();
        try {
        while (true) {
                String rId = client.requestForEmergenciesSync();
                System.out.println(rId);
                Thread.sleep(ClientProperties.getBaseSleepTimeAfterConfirmation() * (r.nextInt(ClientProperties.getMaxSleepMultiplier()) + 1));
                client.releaseEmergenciesSync(rId);
        }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            client.shutdown();
        }
    }
}

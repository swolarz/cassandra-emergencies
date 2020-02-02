package pl.put.srds.emergenciesclient;


import pl.put.srds.emergenciesclient.configs.ClientProperties;
import pl.put.srds.emergenciesclient.grpc.EmergenciesCoordinatorClient;

public class EmergenciesClientApplication {
    public static void main(String[] args) {

        for (int i = 0; i < ClientProperties.getClientsThreadsAmount(); i++) {
            new Thread("Client" + i){
                public void run(){
                    EmergenciesCoordinatorClient client =
                            new EmergenciesCoordinatorClient(ClientProperties.getHost(), ClientProperties.getPort());
                    try {
                        while (true) {
                            client.handleTheAlert();
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    } finally {
                        try {
                            client.shutdown();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }
}

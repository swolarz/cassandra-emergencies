package pl.put.srds.emergencies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class EmergenciesApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(EmergenciesApplication.class, args);
//		EmergenciesCoordinatorServer server = new EmergenciesCoordinatorServer(12345);
//		try {
//			server.start();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		server.blockThreadUntilShutdown();
	}
}

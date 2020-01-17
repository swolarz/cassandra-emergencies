package pl.put.srds.emergencies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import pl.put.srds.emergencies.generated.EmergenciesCoordinatorGrpc;
import pl.put.srds.emergencies.grpc.EmergenciesCoordinatorServer;
import pl.put.srds.emergencies.grpc.EmergenciesCoordinatorService;

import java.io.IOException;

@SpringBootApplication
@ComponentScan({"pl.put.srds.emergencies.generated","pl.put.srds.emergencies.grpc"})
public class EmergenciesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmergenciesApplication.class, args);
		EmergenciesCoordinatorServer server = new EmergenciesCoordinatorServer(12345);
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

package pl.put.srds.emergencies.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class EmergenciesCoordinatorServer {

    private final EmergenciesCoordinatorService emergenciesCoordinatorService;
    private final int port;

    private Server server;


    public EmergenciesCoordinatorServer(EmergenciesCoordinatorService coordinatorService,
                                        @Value("${emergencies.service.port}") int port) throws IOException {
        this.emergenciesCoordinatorService = coordinatorService;
        this.port = port;

        this.start();
    }

    public void blockThreadUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(emergenciesCoordinatorService)
                .build()
                .start();
        log.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    EmergenciesCoordinatorServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }
}

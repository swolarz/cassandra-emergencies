package pl.put.srds.emergenciesclient.grpc.generator;

import pl.put.srds.emergencies.generated.EmergenciesRequest;
import pl.put.srds.emergenciesclient.configs.ClientProperties;

import java.util.LinkedList;
import java.util.Random;

public class RequestsGenerator {
    private static final Random random = new Random();

    public EmergenciesRequest GenerateRequest() {
        EmergenciesRequest request;
        do {
            request =EmergenciesRequest.newBuilder()
                    .addAllGBABrigades(GenerateBrigades())
                    .addAllGCBABrigades(GenerateBrigades())
                    .addAllGLBABrigades(GenerateBrigades())
                    .addAllSCRtBrigades(GenerateBrigades())
                    .addAllSDBrigades(GenerateBrigades())
                    .addAllSRdBrigades(GenerateBrigades())
                    .build();
        } while ( request.getGBABrigadesList().isEmpty() &&
                request.getGCBABrigadesList().isEmpty() &&
                request.getGLBABrigadesList().isEmpty() &&
                request.getSCRtBrigadesList().isEmpty() &&
                request.getSDBrigadesList().isEmpty() &&
                request.getSRdBrigadesList().isEmpty());
        return  request;
    }

    private LinkedList<Integer> GenerateBrigades() {
        LinkedList<Integer> brigades = new LinkedList<>();
        for (int i = 0; i < ClientProperties.getMaxBrigadeId(); i++) {
            if (random.nextDouble() > ClientProperties.getChooseBrigadeThreshold()){
                brigades.add(i + 1);
            }
        }
        return brigades;
    }
}

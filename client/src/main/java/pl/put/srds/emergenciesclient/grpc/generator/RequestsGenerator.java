package pl.put.srds.emergenciesclient.grpc.generator;

import pl.put.srds.emergencies.generated.EmergenciesRequest;
import pl.put.srds.emergenciesclient.configs.ClientProperties;

import java.util.LinkedList;
import java.util.Random;

public class RequestsGenerator {
    private static final Random random = new Random();

    public EmergenciesRequest GenerateRequest() {
        return EmergenciesRequest.newBuilder()
                .addAllGBABrigades(GenerateBrigades())
                .addAllGCBABrigades(GenerateBrigades())
                .addAllGLBABrigades(GenerateBrigades())
                .addAllSCRtBrigades(GenerateBrigades())
                .addAllSDBrigades(GenerateBrigades())
                .addAllSRdBrigades(GenerateBrigades())
                .build();
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

package pl.put.srds.emergenciesclient.grpc.generator;

import pl.put.srds.emergencies.generated.EmergenciesRequest;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

public class RequestsGenerator {
    private int MaxBrigadeId;
    private static final String PROPERTIES_FILENAME = "config.properties";
    private static final String MaxBrigadeIdPropertiesKey = "MaxBrigadeId";
    private static final Random random = new Random();
    public RequestsGenerator() {
        try {
            Properties properties = new Properties();
            properties.load(Objects.requireNonNull(RequestsGenerator.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)));
            MaxBrigadeId = Integer.parseInt(properties.getProperty(MaxBrigadeIdPropertiesKey).trim());
        }
        catch (Throwable t) {
            t.printStackTrace();
            MaxBrigadeId = 10;
            System.out.println("Error during setting MaxBrigadeId from config. Using default MaxBrigadeId = 10");
        }
    }

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
        for (int i = 0; i < MaxBrigadeId; i++) {
            if (random.nextBoolean()){
                brigades.add(i + 1);
            }
        }
        return brigades;
    }
}

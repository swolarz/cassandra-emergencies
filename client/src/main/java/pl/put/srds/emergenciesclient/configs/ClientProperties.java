package pl.put.srds.emergenciesclient.configs;

import java.util.Objects;
import java.util.Properties;

public final class ClientProperties {
    private static String host;
    private static int port;
    private static int maxBrigadeId;
    private static int baseSleepTimeAfterConfirmation;
    private static int maxSleepMultiplier;

    private final static String HOST_KEY = "Host";
    private final static String PORT_KEY = "Port";
    private final static String MAX_BRIGADE_ID_KEY = "MaxBrigadeId";
    private final static String BASE_SLEEP_TIME_AFTER_CONFIRMATION = "BaseSleepTimeAfterConfirmation";
    private final static String MAX_SLEEP_MULTIPLIER = "MaxSleepMultiplier";
    private static final String PROPERTIES_FILENAME = "config.properties";

    static {
        try {
            Properties properties = new Properties();
            properties.load(Objects.requireNonNull(ClientProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)));
            host = properties.getProperty(HOST_KEY).trim();
            port = Integer.parseInt(properties.getProperty(PORT_KEY).trim());
            maxBrigadeId = Integer.parseInt(properties.getProperty(MAX_BRIGADE_ID_KEY).trim());
            baseSleepTimeAfterConfirmation = Integer.parseInt(properties.getProperty(BASE_SLEEP_TIME_AFTER_CONFIRMATION).trim());
            maxSleepMultiplier = Integer.parseInt(properties.getProperty(MAX_SLEEP_MULTIPLIER).trim());
        }
        catch (Throwable t) {
            t.printStackTrace();
            assignDefaultValues();
            System.out.println("Error during setting values from config. Using default values");
        }
    }

    private static void assignDefaultValues() {
        host = "127.0.0.1";
        port = 12345;
        maxBrigadeId = 10;
        baseSleepTimeAfterConfirmation= 200;
        maxSleepMultiplier = 7;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static int getMaxBrigadeId() {
        return maxBrigadeId;
    }

    public static int getMaxSleepMultiplier() {
        return maxSleepMultiplier;
    }

    public static int getBaseSleepTimeAfterConfirmation() {
        return baseSleepTimeAfterConfirmation;
    }
}

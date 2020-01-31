package pl.put.srds.emergenciesclient.configs;

import java.util.Objects;
import java.util.Properties;

public final class ClientProperties {
    private static String host;
    private static int port;
    private static int maxBrigadeId;
    private static int baseSleepTimeAfterConfirmation;
    private static int maxSleepMultiplier;

    private static int baseRetransmissionMultiplier;
    private static int maxRetransmissionMultiplier;
    private static int retransmissionTimeToMultiply;
    private static int baseAlertMultiplier;
    private static int maxAlertMultiplier;
    private static int alertTimeToMultiply;
    private static double chooseBrigadeThreshold;

    private final static String HOST_KEY = "Host";
    private final static String PORT_KEY = "Port";
    private final static String MAX_BRIGADE_ID_KEY = "MaxBrigadeId";
    private final static String BASE_SLEEP_TIME_AFTER_CONFIRMATION = "BaseSleepTimeAfterConfirmation";
    private final static String MAX_SLEEP_MULTIPLIER = "MaxSleepMultiplier";
    private final static String BASE_RETRANSMISSION_MULTIPLIER_KEY = "BaseRetransmissionMultiplier";
    private final static String MAX_RETRANSMISSION_MULTIPLIER_KEY = "MaxRetransmissionMultiplier";
    private final static String RETRANSMISSION_TIME_TO_MULTIPLY_KEY = "RetransmissionTimeToMultiply";
    private final static String BASE_ALERT_MULTIPLIER_KEY = "BaseAlertMultiplier";
    private final static String MAX_ALERT_MULTIPLIER_KEY = "MaxAlertMultiplier";
    private final static String ALERT_TIME_TO_MULTIPLY_KEY = "AlertTimeToMultiply";
    private final static String CHOOSE_BRIGADE_THRESHOLD_KEY = "ChooseBrigadeThreshold";
    private final static String PROPERTIES_FILENAME = "config.properties";

    static {
        try {
            Properties properties = new Properties();
            properties.load(Objects.requireNonNull(ClientProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)));
            host = properties.getProperty(HOST_KEY).trim();
            port = Integer.parseInt(properties.getProperty(PORT_KEY).trim());
            maxBrigadeId = Integer.parseInt(properties.getProperty(MAX_BRIGADE_ID_KEY).trim());
            baseSleepTimeAfterConfirmation = Integer.parseInt(properties.getProperty(BASE_SLEEP_TIME_AFTER_CONFIRMATION).trim());
            maxSleepMultiplier = Integer.parseInt(properties.getProperty(MAX_SLEEP_MULTIPLIER).trim());
            baseRetransmissionMultiplier = Integer.parseInt(properties.getProperty(BASE_RETRANSMISSION_MULTIPLIER_KEY).trim());
            maxRetransmissionMultiplier = Integer.parseInt(properties.getProperty(MAX_RETRANSMISSION_MULTIPLIER_KEY).trim());
            retransmissionTimeToMultiply = Integer.parseInt(properties.getProperty(RETRANSMISSION_TIME_TO_MULTIPLY_KEY).trim());
            baseAlertMultiplier = Integer.parseInt(properties.getProperty(BASE_ALERT_MULTIPLIER_KEY).trim());
            maxAlertMultiplier = Integer.parseInt(properties.getProperty(MAX_ALERT_MULTIPLIER_KEY).trim());
            alertTimeToMultiply = Integer.parseInt(properties.getProperty(ALERT_TIME_TO_MULTIPLY_KEY).trim());
            chooseBrigadeThreshold = Double.parseDouble(properties.getProperty(CHOOSE_BRIGADE_THRESHOLD_KEY).trim());
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

    public static int getBaseRetransmissionMultiplier() {
        return baseRetransmissionMultiplier;
    }

    public static int getMaxRetransmissionMultiplier() {
        return maxRetransmissionMultiplier;
    }

    public static int getRetransmissionTimeToMultiply() {
        return retransmissionTimeToMultiply;
    }

    public static int getBaseAlertMultiplier() {
        return baseAlertMultiplier;
    }

    public static int getMaxAlertMultiplier() {
        return maxAlertMultiplier;
    }

    public static int getAlertTimeToMultiply() {
        return alertTimeToMultiply;
    }

    public static double getChooseBrigadeThreshold() {
        return chooseBrigadeThreshold;
    }
}

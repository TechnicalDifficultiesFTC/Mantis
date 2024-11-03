package org.firstinspires.ftc.teamcode;

public class TelemetryCache {
    /**
     * This class handles the telemetry cache for the robot.
     * It stores and updates telemetry messages and compiles them for display.
     */


    private final String MOTM;
    String[] telemetryCache;

    /**
     * Assigns initial message values so we aren't printing null before the while loop gets going
     *
     * @param MOTM Message of the match
     */
    public TelemetryCache(String MOTM,int cacheLen) {
        this.MOTM = MOTM;
        this.telemetryCache = new String[cacheLen+1];
    }
    public void updateTelemetryCache(String specifier, String message) {
        switch (specifier) {
            case "Overview":
                telemetryCache[0] = "Overview: "+message;
                break;
            case "EncoderLogs":
                telemetryCache[1] = "\nEncoderLog: "+message;
                break;
            case "ServoLog":
                telemetryCache[2] = "CRSERVO Status: "+message;
                break;
            case "PowerStatus":
                telemetryCache[3] = "Power Status: "+message;
                break;
            case "SlideMotor":
                telemetryCache[4] = "\nSlideMotor Status: "+message;
                break;
            case "towerMotor":
                telemetryCache[5] = "TowerMotor Status: "+message;
                break;
            default:
                telemetryCache[6] = "\nAttempted to register message to log with unknown specifier" + specifier;
        }
    }
    /**
     * Compiles everything from telemetry cache into one nice looking string ready to display
     * @return Compiled telemetry message
     */
    public String compileTelemetryCache() {
        StringBuilder compiledTelemetry = new StringBuilder();

        compiledTelemetry.append("MOTM: ").append(MOTM).append("\n");
        for (String messageData : telemetryCache) {
            if (messageData == null) {
                continue;
            }
            compiledTelemetry.append("\n").append(messageData);
        }
        return compiledTelemetry+"\n______________________________________________";
    }
}

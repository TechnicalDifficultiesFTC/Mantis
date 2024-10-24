package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class TelemetryCache {
    /**
     * This class handles the telemetry cache for the robot.
     * It stores and updates telemetry messages and compiles them for display.
     */

    private static final String[] telemetryCache = new String[7];
    private final String MOTM;

    /**
     * Assigns initial message values so we aren't printing null before the while loop gets going
     * @param MOTM Message of the match
     * @param init0 Initialization value for Overview message
     * @param init1 Initialization value for Movement Status message
     * @param init2 Initialization value for CRSERVO message
     * @param init3 Initialization value for Power message
     */
    public TelemetryCache(String MOTM,String init0,String init1,String init2,String init3) {
        this.MOTM = MOTM;
        //Send initial values to cache so we aren't printing null values on call
        telemetryCache[0] = init0;
        telemetryCache[1] = init1;
        telemetryCache[2] = init2;
        telemetryCache[3] = init3;
    }
    public void updateTelemetryCache(String specifier, String message) {
        switch (specifier) {
            case "Overview":
                telemetryCache[0] = "Overview: "+message;
                break;
            case "SlideMotor":
                telemetryCache[1] = "SlideMotor POW: "+message;
                break;
            case "ServoLog":
                telemetryCache[2] = "CRSERVO: "+message;
                break;
            case "PowerStatus":
                telemetryCache[3] = "Power: "+message;
                break;
            case "EncoderPos":
                telemetryCache[4] = "EncoderPos (tower): "+message;
                break;
            default:
                telemetryCache[6] = "Attempted to register message to log with unknown specifier" + specifier;
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

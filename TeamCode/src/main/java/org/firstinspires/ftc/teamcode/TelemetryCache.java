package org.firstinspires.ftc.teamcode;

public class TelemetryCache {
    private static final String[] telemetryCache = new String[6];
    private final String MOTM;
    public TelemetryCache(String MOTM) {
        this.MOTM = MOTM;
    }
    public void updateTelemetryCache(String specifier, String message) {
        switch (specifier) {
            case "Overview":
                telemetryCache[0] = "Overview: "+message;
                break;
            case "Movement":
                telemetryCache[1] = "Movement Status: "+message;
                break;
            case "SlideMotor":
                telemetryCache[2] = "SlideMotor: "+message;
                break;
            case "ShaftMotor":
                telemetryCache[3] = "ShaftMotor: "+message;
                break;
            case "PowerStatus":
                telemetryCache[4] = "Power: "+message;
                break;
            default:
                telemetryCache[5] = "Attempt to register message to log with unknown specifier" + specifier;
        }
    }

    public String compileTelemetryCache() {
        StringBuilder compiledTelemetry = new StringBuilder();
        compiledTelemetry.append("MOTM (message of the match): ").append(MOTM).append("\n");
        for (int i = 0; i < (telemetryCache.length-1); ++i) {
            compiledTelemetry.append("\n").append(telemetryCache[i]);
        }
        return compiledTelemetry.toString();
    }
}

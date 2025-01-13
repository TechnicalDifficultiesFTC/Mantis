package org.firstinspires.ftc.teamcode.Main.Helpers;

public enum DeviceRegistry {
    //Motors
    FRONT_LEFT_MOTOR("FLM"),
    BACK_LEFT_MOTOR("BLM"),
    FRONT_RIGHT_MOTOR("FRM"),
    BACK_RIGHT_MOTOR("BRM"),
    TOWER_MOTOR("towerMotor"),
    SLIDE_MOTOR("slideMotor"),
    //Servos
    INTAKE_SERVO("intakeServo"),
    CLIMB_MOTOR_LEFT("climbOne"),
    CLIMB_MOTOR_RIGHT("climbTwo");

    private final String deviceName;

    DeviceRegistry(String deviceName) {
        this.deviceName = deviceName;
    }

    public String str() {
        return deviceName;
    }
}
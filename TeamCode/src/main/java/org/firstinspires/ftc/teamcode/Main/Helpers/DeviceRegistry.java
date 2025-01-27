package org.firstinspires.ftc.teamcode.Main.Helpers;

public enum DeviceRegistry {
    //Motors
    FRONT_LEFT_MOTOR("FLM"),
    BACK_LEFT_MOTOR("BLM"),
    FRONT_RIGHT_MOTOR("FRM"),
    BACK_RIGHT_MOTOR("BRM"),
    TOWER_MOTOR("towerMotor"),
    SLIDE_MOTOR("slideMotor"),
    CLIMB_MOTOR_LEFT("climbOne"),
    CLIMB_MOTOR_RIGHT("climbTwo"),
    //Servos
    INTAKE_SERVO("intakeServo"),
    //Encoders
    //TODO: Change these names to whatever port motor they are plugged in with
    /*
    The direction of each Encoder is independent of the DcMotorEx it’s created with.
    Changing the direction of an Encoder has no effect on the direction of its associated DcMotorEx
    (and vice versa).
     */
    LEFT_THROUGHBORE_ENC("leftEnc"),
    RIGHT_THROUGHBORE_ENC("rightEnc"),
    YAW_THROUGHBORE_ENC("yaw");


    private final String deviceName;

    DeviceRegistry(String deviceName) {
        this.deviceName = deviceName;
    }

    public String str() {
        return deviceName;
    }
}
package org.firstinspires.ftc.teamcode.Main.Helpers;

/**
 * Holds static values
 */
public class Constants {
    public static final boolean SHOW_CTRL1 = false;
    public static final boolean SHOW_CTRL2 = false;

    //Used in beaUtils.triggerBoolean() to determine the amount a trigger needs to be pressed to return true
    public static final double TRIGGER_THRESHOLD = .1;

    //Used for software underclock
    public static final double MIN_SPEED = 0.33;
    public static final double MAX_SPEED = 1;

    //Necessary encoder information
    private static final int TOWER_ENCODER_TYPE = 4; //Quadrature
    private static final int SLIDE_ENCODER_TYPE = 4; //Quadrature

    private static final double TOWER_MOTOR_PPR = 5281.1;
    private static final double SLIDE_MOTOR_PPR = 587.7;

    public static final double TOWER_MOTOR_CPR = TOWER_MOTOR_PPR * TOWER_ENCODER_TYPE;
    public static final double SLIDE_MOTOR_CPR = SLIDE_MOTOR_PPR * SLIDE_ENCODER_TYPE;

    //Mess around with these until the drivetrain drivetrains
    //Btw setting a value to false is setting the motor to go in reverse when given a positive signal

    public static final boolean FRONT_LEFT_DT_MOTOR_FORWARD = false;

    public static final boolean FRONT_RIGHT_DT_MOTOR_FORWARD = true;
    public static final boolean BACK_LEFT_DT_MOTOR_FORWARD = false;

    public static final boolean BACK_RIGHT_DT_MOTOR_FORWARD = false;

    //Pinkarm Vars
    public static final int SCALER = 50;
    //End effector servo
    public static final double OUTTAKE_POWER = .5;
    public static final double INTAKE_POWER = 1;

    //Tag funzies :)
    public static String dasshTag = "|- Dassh01 -|";
}

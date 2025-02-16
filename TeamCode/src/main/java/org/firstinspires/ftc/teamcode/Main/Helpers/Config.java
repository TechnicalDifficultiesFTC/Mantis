package org.firstinspires.ftc.teamcode.Main.Helpers;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Holds static values
 */
@com.acmerobotics.dashboard.config.Config
public class Config {
    //Auto
    public static double AUTO_DRIVE_SPEED = .5;

    //Telemetry
    public static boolean SHOW_DT_STATUS = false; //Where DT is "Drivetrain"
    public static boolean SHOW_ARM_STATUS = false;
    public static boolean SHOW_CTRL1 = false;
    public static boolean SHOW_CTRL2 = false;
    public static boolean SHOW_ENCODER_DATA = false;

    //Used in beaUtils.triggerBoolean() to determine the amount a trigger needs to be pressed to return true
    public static final double TRIGGER_THRESHOLD = .1;

    //Used for software underclock
    public static double MIN_CLIMB_SPEED = .5; //Negative because reversal
    public static double MAX_CLIMB_SPEED = 1;
    public static float CLIMB_LOCK_DOWN_POWER = 1;

    public final static double MIN_DT_SPEED = .5;
    public final static double MAX_DT_SPEED = 1;

    //Mess around with these until the drivetrain drivetrains
    //Btw setting a value to false is setting the motor to go in reverse when given a positive signal

    public static final boolean FRONT_LEFT_DT_MOTOR_FORWARD = false;

    public static final boolean FRONT_RIGHT_DT_MOTOR_FORWARD = true;
    public static final boolean BACK_LEFT_DT_MOTOR_FORWARD = false;

    public static final boolean BACK_RIGHT_DT_MOTOR_FORWARD = false;

    //Pinkarm Vars
    public static double SCALER = 25;
    //End effector servo
    public static double SERVO_OUTTAKE_POWER = -.7;
    public static double SERVO_INTAKE_POWER = 1;

    //TODO: Update & test
    public static double pinkArmExtensionCutoff = 0; // MEASURED IN TICKS, measure to 45 degrees
    /*
    RunModes:
    RUN_TO_POSITION -> Uses encoder feedback (FFL) to push to a position set by .setTargetPosition(int position) [0,360]?, then holds the last given position
    RUN_USING_ENCODER -> Uses encoder feedback to push at a velocity given on a range of [-1,1] set by .setPower(int velocity)
    RUN_WITHOUT_ENCODER -> SHOULD ONLY BE USED ON MOTORS W/O ENCODERS, uses no feedback to attempt to gain a velocity
     */

    public static DcMotor.RunMode ARM_MODE = DcMotor.RunMode.RUN_USING_ENCODER;

    public static int pinkArmExtensionLimitTicks = -2700;

    //Tag funzies :)
    public static String dasshTag = "                                                 88            \n" +
            "                                           ,d    \"\"            \n" +
            "                                           88                  \n" +
            "88,dPYba,,adPYba,  ,adPPYYba, 8b,dPPYba, MM88MMM 88 ,adPPYba,  \n" +
            "88P'   \"88\"    \"8a \"\"     `Y8 88P'   `\"8a  88    88 I8[    \"\"  \n" +
            "88      88      88 ,adPPPPP88 88       88  88    88  `\"Y8ba,   \n" +
            "88      88      88 88,    ,88 88       88  88,   88 aa    ]8I  \n" +
            "88      88      88 `\"8bbdP\"Y8 88       88  \"Y888 88 `\"YbbdP\"'  \n" +
            "                                                               ";
}

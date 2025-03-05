package org.firstinspires.ftc.teamcode.Main.Helpers;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Holds static values
 */
@com.acmerobotics.dashboard.config.Config
public class Config {
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
    /*
    RunModes:
    RUN_TO_POSITION -> Uses encoder feedback (FFL) to push to a position set by .setTargetPosition(int position) [0,360]?, then holds the last given position
    RUN_USING_ENCODER -> Uses encoder feedback to push at a velocity given on a range of [-1,1] set by .setPower(int velocity)
    RUN_WITHOUT_ENCODER -> SHOULD ONLY BE USED ON MOTORS W/O ENCODERS, uses no feedback to attempt to gain a velocity
     */

    /*
    ---------------------------------------------------------------- LIMITS ZONE ----------------------------------------------------------------
     */

    //Extension limit

    public static int pinkArmExtensionLimitTicks = -2700;

    public static int pinkArmDegrees_ApplyExtensionLimit_InTicks = -1070; //Measured to about 18.7 degrees

    //BELOW IS FOR AUTO'S
    //Rotational -3330, -3250

    public static int pinkArmDegreesRotation_HighBasket_InTicks = -3300; //Ticks to move to when initialized at base to reach high basket
    public static int pinkArmDegreesRotation_ToTouchGround = 75; //Reverse this amount to go back up to a good amount to ascend? //TODO: Test this

    //Extensional
    public static int pinkArmExtensionAmountSlides_ToReachPieceInAuto_InTicks = -1400; //Ticks to extend the slides to in order to reach sample in auto
    public static int pinkArmExtensionAmountSlides_ToReachHighBasket_InTicks = -3250;
    public static int pinkArmExtensionAmountSlides_TillSafeToDescend_InTicks = 0; //TODO: FILL IN
    public static int pinkArmEscapeAmount_InTicks = -420;
    public static int climbArmsExtensionAmount_InTicks = 0; //TODO: FILL IN

    //The initial pose listed below looks like this: https://drive.google.com/file/d/1Utc5_G4_l_5cw0eufCwzscnooBlsDLXZ/view?usp=sharing
    public static Pose2d initialBluePose = new Pose2d(20,62,Math.toRadians(-90));

    /*
    ---------------------------------------------------------------- LIMITS ZONE ----------------------------------------------------------------
     */
    //Tag funzies :)
    public static String dasshTag = "Dassh01";

}

package org.firstinspires.ftc.teamcode.Main.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.Debounce;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Helpers.TowerPosMovementStatus;

public class PinkArm extends Utils {

    private Debounce debounceA;
    public boolean actionActive = false;
    public final RevColorSensorV3 colorSensorV3;
    public final Encoder towerEncoder,slideEncoder;
    private final boolean runWithEncoders;
    public int pinkArmExtensionTicks = 0;
    public int pinkArmRotationalTicks = 0;
    boolean leftTriggerPressed = false;
    boolean rightTriggerPressed = false;
    public TowerPosMovementStatus towerPosMovementStatus = TowerPosMovementStatus.NOT_MOVING;
    public DcMotor towerMotor, slideMotor;
    public CRServo intakeServo;
    double towerMotorPower, slideMotorPower, intakeServoPower;
    public int towerMotorPos;
    public boolean visionSensorInitialized;

    /**
     * Initialize PinkArm and pass in PinkArm motors and servo objects
     */
    public PinkArm (HardwareMap hardwareMap, boolean runWithEncoders) {
        debounceA = new Debounce();

        this.runWithEncoders = runWithEncoders;

        towerMotor = hardwareMap.dcMotor.get(DeviceRegistry.TOWER_MOTOR.str());
        slideMotor = hardwareMap.dcMotor.get(DeviceRegistry.SLIDE_MOTOR.str());
        intakeServo = hardwareMap.crservo.get(DeviceRegistry.INTAKE_SERVO.str());

        colorSensorV3 = (RevColorSensorV3) hardwareMap.colorSensor.get(DeviceRegistry.COLOR_SENSOR.str());
        intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);

        this.visionSensorInitialized = colorSensorV3.initialize();

        if (runWithEncoders) {
            //Initialize both motor encoders in run to position
            towerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            towerMotor.setTargetPosition(0); //Initial position is set to zero to avoid RUN_TO_POSITION no position error
            towerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor.setTargetPosition(0); //Initial position is set to zero to avoid RUN_TO_POSITION no position error
            towerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        }
        else {
            //Both towerMotor and slideMotor on manual if not using encoders
            towerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            towerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //Set slidemotor configs
            slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        //Set zeropowermode of both motors to brake
        towerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Direction changes:
        intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        towerMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Reconsidered. I love overflow encoders!
        towerEncoder = new OverflowEncoder(new RawEncoder((DcMotorEx) towerMotor));
        slideEncoder = new OverflowEncoder(new RawEncoder((DcMotorEx) slideMotor));
    }
    /*
    ------------------------------------------------------------------------------------ ACTIONS ZONE ------------------------------------------------------------------------------------
     */

//    public void intake() {
//        intakeServo.setPower(Config.SERVO_INTAKE_POWER);
//    }
//
//    public void outtake() {
//        intakeServo.setPower(Config.SERVO_OUTTAKE_POWER);
//    }
//
//    public void stopIntake() {
//        intakeServo.setPower(0);
//    }

//    public void bringArmToEscape() {
//        towerMotor.setTargetPosition(Config.pinkArmDegreesRotation_EscapeAmount_InTicks);
//        towerMotor.setPower(Config.autosActionPower);
//        while (HyperOnePiece_AUTO.opModeIsActive) {
//            if (towerMotor.getCurrentPosition() == Config.pinkArmDegreesRotation_EscapeAmount_InTicks) {
//                return;
//            }
//        }
//    }
//
//    public void extendArmToBoundary() {
//        slideMotor.setTargetPosition(Config.pinkArmExtensionAmountSlides_TillSafeToDescend_InTicks);
//        slideMotor.setPower(Config.autosActionPower);
//        while (HyperOnePiece_AUTO.opModeIsActive) {
//            if (slideMotor.getCurrentPosition() == Config.pinkArmExtensionAmountSlides_TillSafeToDescend_InTicks) {
//                return;
//            }
//        }
//    }
//
//    public void raiseArmToHighBasket() {
//        towerMotor.setTargetPosition(Config.pinkArmDegreesRotation_HighBasket_InTicks);
//        towerMotor.setPower(Config.autosActionPower);
//
//        while (HyperOnePiece_AUTO.opModeIsActive) {
//            if (towerMotor.getCurrentPosition() == Config.pinkArmDegreesRotation_HighBasket_InTicks) {
//                return;
//            }
//        }
//    }





    private class StopIntake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeServo.setPower(0);
            return intakeServo.getPower() != 0; //Rerun if power is not 0, otherwise stop action
        }
    }
    private class Outtake implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeServo.setPower(Config.SERVO_OUTTAKE_POWER);
            if (intakeServo.getPower() != Config.SERVO_OUTTAKE_POWER) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    private class Intake implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                intakeServo.setPower(Config.SERVO_INTAKE_POWER);
            }

            if (intakeServo.getPower() != Config.SERVO_INTAKE_POWER) {
                intakeServo.setPower(Config.SERVO_INTAKE_POWER);
                return true;
            }
            else {
                return false;
            }

        }
    }

    private class IntakeRetainPieceMode implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            intakeServo.setPower(Config.SERVO_RETAINING_POWER);
            return false;
        }
    }
    private class IntakeUntilPieceDetected implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                intakeServo.setPower(Config.SERVO_INTAKE_POWER);
            }

            if (!isPieceVisible()) { //IF piece isn't detected in intake
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * ARM MUST BE ESCAPED BEFORE THIS IS RUN!!
     * Method assumes towerMotor is zeroed
     */
    private class RaiseArmToHighBasket implements Action {
        boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                towerMotor.setTargetPosition(Config.pinkArmDegreesRotation_HighBasket_InTicks);
                towerMotor.setPower(Config.autosActionPower);
            }

            if (towerMotor.getCurrentPosition() == Config.pinkArmDegreesRotation_HighBasket_InTicks) {
                return false;
            }
            return true;
        }
    }

    private class BringArmToEscapeAngle implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                towerMotor.setTargetPosition(Config.pinkArmDegreesRotation_EscapeAmount_InTicks);
                towerMotor.setPower(Config.autosActionPower);
                initialized = true;
            }
            //Rerun until the amount of ticks is reached
            if (towerMotor.getCurrentPosition() == Config.pinkArmDegreesRotation_EscapeAmount_InTicks) {
                slideMotor.setTargetPosition(0);
                slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideMotor.setPower(Config.slideMotorAutoPower);
                return false;
            }
            return true;
        }

    }

    private class ExtendPinkArmToReachPiece implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                slideMotor.setTargetPosition(Config.pinkArmExtensionLimitTicks); //TODO: Find how much we ACTUALLY need in auto
                slideMotor.setPower(Config.autosActionPower);
            }
            if (slideMotor.getCurrentPosition() < (Config.pinkArmExtensionLimitTicks)) { //While pinkArm is not at expected position
                return true;
            }
            else {
                return false; //If pinkarm has reached expected position
            }
        }
    }

    private class ExtendPinkArmPastBoundary implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                slideMotor.setTargetPosition(Config.pinkArmExtensionAmountSlides_TillSafeToDescend_InTicks);
                slideMotor.setPower(Config.autosActionPower);
                initialized = true;
            }

            if (slideMotor.getCurrentPosition() == Config.pinkArmExtensionAmountSlides_TillSafeToDescend_InTicks) {
                return false;
            }

            return true;

        }
    }

    private class DescendPinkArmToReachGround implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                towerMotor.setTargetPosition(Config.pinkArmDegreesRotation_ToTouchGround);
                towerMotor.setPower(Config.autosActionPower);
                initialized = true;
            }

            if (towerMotor.getCurrentPosition() != Config.pinkArmDegreesRotation_ToTouchGround) {
                return true;
            }
            else {
                return false;
            }
        }
    }



    private class ReachTowardsHighBasket implements Action {
        boolean initalized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initalized) {
                slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideMotor.setTargetPosition(Config.pinkArmExtensionAmountSlides_ToReachHighBasket_InTicks);
                slideMotor.setPower(Config.autosActionPower);
            }

            if (slideMotor.getCurrentPosition() == Config.pinkArmExtensionAmountSlides_ToReachHighBasket_InTicks) {
                return false;
            }

            return true;
        }
    }

    private class ExtendSlightlyPastBoundary implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                slideMotor.setTargetPosition(Config.pinkArmExtensionAmountSlides_TillSafeToRaise_InTicks);
                slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slideMotor.setPower(Config.slideMotorAutoPower);
                initialized = true;
            }

            if (slideMotor.getCurrentPosition() == Config.pinkArmExtensionAmountSlides_TillSafeToRaise_InTicks) {
                return false;
            }

            return true;

        }
    }

    /*
    ------------------------------------------------------------------------------------ ACTIONS ZONE ------------------------------------------------------------------------------------
     */

    /*
    ------------------------------------------------------------------------------------ ACTIONS METHODS ZONE ------------------------------------------------------------------------------------
     */
    public Action extendSlightlyPastBoundary() {
        return new ExtendSlightlyPastBoundary();
    }
    public Action reachTowardsHighBasket() {
        return new ReachTowardsHighBasket();
    }
    public Action descendPinkArmToReachGround() {
        return new DescendPinkArmToReachGround();
    }
    public Action extendPinkArmPastBoundary() {
        return new ExtendPinkArmPastBoundary();
    }
    public Action intakeToRetainPiece() {
        return new IntakeRetainPieceMode();
    }
    public Action extendPinkArmToReachPiece() {
        return new ExtendPinkArmToReachPiece();
    }

    public Action intakeUntilPieceDetected() {
        return new IntakeUntilPieceDetected();
    }

    public Action stopIntake() {
        return new StopIntake();
    }

    public Action intake() {
        return new Intake();
    }

    public Action outtake() {
        return new Outtake();
    }
    public Action raiseArmToHighBasket() {
        return new RaiseArmToHighBasket();
    }

    public Action bringArmToEscapeAngle() {
        return new BringArmToEscapeAngle();
    }
    /*
    ------------------------------------------------------------------------------------ ACTIONS ZONE ------------------------------------------------------------------------------------
     */

    public String armStatusAsString() {
        //Tower Encoder
        int towerEncoderPosition = towerEncoder.getPositionAndVelocity().position;
        int towerEncoderVelocity = towerEncoder.getPositionAndVelocity().velocity;
        //Slide Encoder
        int slideEncoderPosition = slideEncoder.getPositionAndVelocity().position;
        int slideEncoderVelocity = slideEncoder.getPositionAndVelocity().velocity;
        return ("Tower Motor Encoder: " +
                "\nEncoder position (ticks) = " + towerEncoderPosition +
                "\nEncoder velocity (units) = " + towerEncoderVelocity + "\n" +

                "Slide Motor Encoder: " +
                "\n Encoder position (ticks) = " + slideEncoderPosition +
                "\n Encoder velocity (units) = " + slideEncoderVelocity);
    }
    /**
     * Main processing loop of PinkArm
     * @param gamepad All input from gamepad 2 as gamepad obj
     */
    public void processInput(Gamepad gamepad) {

        if (!Config.encodersMode) { //Standard Mode
            setIntakeServoPower(gamepad);
            setArmPowers(gamepad);

            if (debounceA.isPressed(gamepad.a)) {
                slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                towerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                towerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }
        }
        else {
            setIntakeServoPower(gamepad);
            setArmPowersUsingEncoder(gamepad);
        }

    }

    public boolean isPieceVisible() {
        double distance = getVisionSensorDistanceInches();
        return distance < Config.criticalVisionSensorThresholdDistance;
    }

    public double getVisionSensorDistanceInches() {
        return colorSensorV3.getDistance(DistanceUnit.INCH); //Returns distance in inches
    }
    public String getVisionStatusAsString(){
        return ("Vision Sensor Distance: " + getVisionSensorDistanceInches() + "\n" +
                "Piece Visible?: " + isPieceVisible() + "\n");
    }

    /**
     * This method determines if the pinkarm rotational limit needs to be applied
     * by seeing if the current ticks are above the amount when pink arm extension can be deregulated
     * @return True if the pink arm extension limit should be applied, False if the pink arm extension limit shouldn't be
     */
    public boolean pinkArmExtensionLimitShouldBeApplied() {
        boolean pinkArmBehindLimit = isPinkArmExtensionLimitInEffect();
        boolean pinkArmHasExceededExtensionLimit = (pinkArmExtensionTicks > Config.pinkArmExtensionLimitTicks);
        return (pinkArmBehindLimit && pinkArmHasExceededExtensionLimit);
    }

    public boolean isPinkArmExtensionLimitInEffect() {
        return (pinkArmRotationalTicks < Config.pinkArmDegrees_ApplyExtensionLimit_InTicks);
    }

    public void setArmPowers(Gamepad gamepad) {
        pinkArmExtensionTicks = slideMotor.getCurrentPosition();
        pinkArmRotationalTicks = towerMotor.getCurrentPosition();

        if (pinkArmExtensionLimitShouldBeApplied()) { // If TRUE apply extension limit, ELSE run normally
            slideMotor.setTargetPosition(pinkArmExtensionTicks-25); //Moves pinkArm back 25 ticks
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //Enforce pinkArm restriction
        }
        else { //Execute arm logic normally
            slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            slideMotorPower = -gamepad.left_stick_y;
            slideMotor.setPower(slideMotorPower);
        }

        towerMotorPower = (gamepad.right_trigger - gamepad.left_trigger);
        towerMotor.setPower(towerMotorPower);
    }
    public void setArmPowersUsingEncoder(Gamepad gamepad) {
        leftTriggerPressed = triggerBoolean(gamepad.left_trigger);
        rightTriggerPressed = triggerBoolean(gamepad.right_trigger);

        if (leftTriggerPressed && rightTriggerPressed) {
            towerPosMovementStatus = TowerPosMovementStatus.NOT_MOVING;
        }
        else if (leftTriggerPressed) {
            towerMotorPos += (int) (gamepad.left_trigger * Config.SCALER);
            towerPosMovementStatus = TowerPosMovementStatus.MOVING_UP;
        }
        else if (rightTriggerPressed) {
            towerMotorPos -= (int) (gamepad.right_trigger * Config.SCALER);
            towerPosMovementStatus = TowerPosMovementStatus.MOVING_DOWN;
        }
        else {
            towerPosMovementStatus = TowerPosMovementStatus.NOT_MOVING;
        }

        towerMotor.setTargetPosition(towerMotorPos);
        towerMotor.setPower(.15);

        slideMotorPower = gamepad.left_stick_y;
        slideMotor.setPower(slideMotorPower);
    }
    public void setIntakeServoPower(Gamepad gamepad) {
        if (gamepad.left_bumper) { //Intake
            intakeServoPower = Config.SERVO_INTAKE_POWER;
        }
        else if (gamepad.right_bumper) { //Outtake
            intakeServoPower = Config.SERVO_OUTTAKE_POWER;
        }
        else {
            intakeServoPower = 0;
        }
        intakeServo.setPower(intakeServoPower);
    }

    /**
     * "If its a hypothetical variable you give it a hat"
     * @return Hypothetical towerMotorPos
     */
    public double getTowerMotorPoseHat() {
        return towerMotorPos;
    }

}
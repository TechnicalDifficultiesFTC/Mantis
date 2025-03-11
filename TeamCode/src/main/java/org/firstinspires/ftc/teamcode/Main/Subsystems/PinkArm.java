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
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Helpers.TowerPosMovementStatus;

import java.util.Objects;


public class PinkArm extends Utils {
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
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                intakeServo.setPower(Config.SERVO_RETAINING_POWER);
            }

            if (intakeServo.getPower() != Config.SERVO_RETAINING_POWER) {
                intakeServo.setPower(Config.SERVO_RETAINING_POWER);
                return true;
            }
            else {
                return false;
            }
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
            DcMotor.RunMode tempMode = null;
            if (!initialized) {
                if (towerMotor.getMode() != DcMotor.RunMode.RUN_TO_POSITION) { //If tower in wrong mode
                    tempMode = towerMotor.getMode(); //Store for later reset
                    towerMotor.setTargetPosition(Config.pinkArmDegreesRotation_HighBasket_InTicks); //Sets tower motor to reach to high basket
                    towerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); //Set to right more (will be reset later)
                }
            }

            if (towerMotor.getCurrentPosition() == Config.pinkArmDegreesRotation_HighBasket_InTicks) {
                if (Objects.nonNull(tempMode)) {
                    towerMotor.setMode(tempMode);
                }
                return false;
            }
            else {
                return true;
            }
        }
    }

    private class BringArmToEscapeAngle implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                towerMotor.setTargetPosition(Config.pinkArmDegreesRotation_EscapeAmount_InTicks);
                towerMotor.setPower(.5);
                initialized = true;
            }
            //Rerun until the amount of ticks is reached
            if (towerMotor.getCurrentPosition() == Config.pinkArmDegreesRotation_EscapeAmount_InTicks) {
                return false;
            }
            return true;
        }

    }

    private class extendPinkArm implements Action {
        boolean initialized = false;
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                slideMotor.setTargetPosition(Config.pinkArmExtensionLimitTicks); //TODO: Find how much we ACTUALLY need in auto
                slideMotor.setPower(.75);
            }
            if (slideMotor.getCurrentPosition() < (Config.pinkArmExtensionLimitTicks)) { //While pinkArm is not at expected position
                return true;
            }
            else {
                return false; //If pinkarm has reached expected position
            }
        }
    }

    /*
    ------------------------------------------------------------------------------------ ACTIONS ZONE ------------------------------------------------------------------------------------
     */

    /*
    ------------------------------------------------------------------------------------ ACTIONS METHODS ZONE ------------------------------------------------------------------------------------
     */

    public Action intakeToRetainPiece() {
        return new IntakeRetainPieceMode();
    }
    public Action extendPinkArm() {
        return new extendPinkArm();
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

        if (!runWithEncoders) { //Standard Mode
            setIntakeServoPower(gamepad);
            setArmPowers(gamepad);
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

    public void setArmPowers(@NonNull Gamepad gamepad) {
        pinkArmExtensionTicks = slideMotor.getCurrentPosition();
        pinkArmRotationalTicks = towerMotor.getCurrentPosition();

        //TODO: Consider moving monitor onto its own thread
        if (pinkArmExtensionLimitShouldBeApplied()) {
            slideMotor.setTargetPosition(pinkArmExtensionTicks-25);
            slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        else {
            slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            slideMotorPower = -gamepad.left_stick_y;
            slideMotor.setPower(slideMotorPower);

            towerMotorPower = (gamepad.right_trigger - gamepad.left_trigger);
            towerMotor.setPower(towerMotorPower);
        }
    }
    public void setArmPowersUsingEncoder(@NonNull Gamepad gamepad) {
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

        slideMotorPower = gamepad.left_stick_y;

        towerMotor.setTargetPosition(towerMotorPos);
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
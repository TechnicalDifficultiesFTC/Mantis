package org.firstinspires.ftc.teamcode.Main.Subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Helpers.TowerPosMovementStatus;

public class PinkArm extends Utils {
    private boolean runWithEncoders;
    public int pinkArmExtensionTicks = 0;
    public int pinkArmRotationalTicks = 0;
    boolean leftTriggerPressed = false;
    boolean rightTriggerPressed = false;
    public TowerPosMovementStatus towerPosMovementStatus = TowerPosMovementStatus.NOT_MOVING;
    public int towerPosIncreasing = -1;
    public DcMotor towerMotor, slideMotor;
    public CRServo intakeServo;
    double towerMotorPower, slideMotorPower, intakeServoPower;
    public int towerMotorPos;

    /**
     * Initialize PinkArm and pass in PinkArm motors and servo objects
     */
    public PinkArm (HardwareMap hardwareMap, boolean runWithEncoders) {
        this.runWithEncoders = runWithEncoders;

        towerMotor = hardwareMap.dcMotor.get(DeviceRegistry.TOWER_MOTOR.str());
        slideMotor = hardwareMap.dcMotor.get(DeviceRegistry.SLIDE_MOTOR.str());
        intakeServo = hardwareMap.crservo.get(DeviceRegistry.INTAKE_SERVO.str());
        intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);

        if (runWithEncoders) {
            towerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            towerMotor.setTargetPosition(0);
            towerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        else {
            towerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        towerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public class Outtake implements Action {
        private boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            if (!initialized) {
                intakeServo.setPower(Config.SERVO_OUTTAKE_POWER);
                initialized = true;
            }
            packet.put("Initialized?: ",initialized);
            new SleepAction(500);
            intakeServo.setPower(0);
            return false;
        }
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
            //TODO: Verify & debug
            setIntakeServoPower(gamepad);
            setArmPowersUsingEncoder(gamepad);
        }

    }



    public boolean pinkArmHasExceededBounds() {
        return (pinkArmExtensionTicks < -2770);
    }

    /**
     * This method determines if the pinkarm rotational limit needs to be applied
     * by seeing if the current ticks are above the amount when pink arm extension can be deregulated
     * @return True if the pink arm extension limit should be applied, False if the pink arm extension limit shouldn't be
     */
    public boolean pinkArmExtensionLimitShouldBeApplied() {
        return !(pinkArmRotationalTicks > Config.pinkArmExtensionCutoff);
    }
    public void setArmPowers(@NonNull Gamepad gamepad) {
        pinkArmExtensionTicks = slideMotor.getCurrentPosition();
        pinkArmRotationalTicks = towerMotor.getCurrentPosition();

        if (pinkArmHasExceededBounds() && pinkArmExtensionLimitShouldBeApplied()) {
            slideMotor.setPower(-1);
        }

        slideMotorPower = gamepad.left_stick_y;
        slideMotor.setPower(slideMotorPower);

        towerMotorPower = (gamepad.left_trigger - gamepad.right_trigger);
        towerMotor.setPower(towerMotorPower);
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
    public double getTowerMotorHypotheticalPos() {
        return towerMotorPos;
    }

}
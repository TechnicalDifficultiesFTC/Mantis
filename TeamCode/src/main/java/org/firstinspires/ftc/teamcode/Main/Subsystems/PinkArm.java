package org.firstinspires.ftc.teamcode.Main.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;

public class PinkArm extends Utils {
    public int pinkArmExtensionTicks = 0;
    boolean leftTriggerPressed = false;
    boolean rightTriggerPressed = false;
    public int towerPosIncreasing = -1;
    DcMotor towerMotor, slideMotor;
    public CRServo intakeServo;
    double towerMotorPower, slideMotorPower, intakeServoPower;
    public int towerMotorPos;
    final boolean runToPositionINACTIVE;

    /**
     * Initialize PinkArm and pass in PinkArm motors and servo objects
     * @param towerMotor Tower Motor
     * @param slideMotor Slide Motor
     * @param intakeServo CRServo Intake Servo
     */
    public PinkArm (DcMotor towerMotor, DcMotor slideMotor, CRServo intakeServo) {
        this.towerMotor = towerMotor;
        this.slideMotor = slideMotor;
        this.intakeServo = intakeServo;
        this.runToPositionINACTIVE = (Config.ARM_MODE != DcMotor.RunMode.RUN_TO_POSITION);

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
    //TODO: TEST!!
    public void processInput(Gamepad gamepad) {
        //TODO: Fix pinkarm FFL stabilization
        /*
        Cases:
        A) Run standard logic if auto stabilize is off and the runmode isn't run to position
        B) Run stabilization logic if auto stabilize is on and the runmode is run to position
        ELSE) Error out
         */
        setIntakeServoPower(gamepad);
        setArmPowers(gamepad);
    }

    public void processInput(Gamepad gamepad, DcMotor.RunMode runmode) {
        setIntakeServoPower(gamepad);
        setArmPowersUsingEncoder(gamepad);
    }

    public void setArmPowers(@NonNull Gamepad gamepad) {
        pinkArmExtensionTicks = slideMotor.getCurrentPosition();

        if (pinkArmExtensionTicks > -2770) {
            slideMotorPower = gamepad.left_stick_y;
            slideMotor.setPower(slideMotorPower);
        }
        else {
            slideMotorPower = gamepad.left_stick_y;
            slideMotor.setPower(slideMotorPower);
        }


        towerMotorPower = (gamepad.left_trigger - gamepad.right_trigger);
        towerMotor.setPower(towerMotorPower);
    }
    public void setArmPowersUsingEncoder(@NonNull Gamepad gamepad) {
        //TODO: Test telemetry handles
        leftTriggerPressed = triggerBoolean(gamepad.left_trigger);
        rightTriggerPressed = triggerBoolean(gamepad.right_trigger);

        if (leftTriggerPressed && rightTriggerPressed) {
            towerPosIncreasing = -1;
        }
        else if (leftTriggerPressed) {
            towerMotorPos += (int) ( gamepad.left_trigger * Config.SCALER);
            towerPosIncreasing = 1;
        }
        else if (rightTriggerPressed) {
            towerMotorPos -= (int) (gamepad.right_trigger * Config.SCALER);
            towerPosIncreasing = 2;
        }
        else {
            towerPosIncreasing = -1;
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
    public void setPosZero() {
        towerMotor.setTargetPosition(0);
        //slideMotor.setTargetPosition(0);
    }
    /**
     * Sets both the towerMotor and slideMotor to a unanimous mode
     */
    public void setArmMode(DcMotor.RunMode mode) {
        towerMotor.setMode(mode);
    }

}
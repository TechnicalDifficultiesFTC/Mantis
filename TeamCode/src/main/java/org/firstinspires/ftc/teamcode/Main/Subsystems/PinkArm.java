package org.firstinspires.ftc.teamcode.Main.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Main.Helpers.Constants;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;

public class PinkArm extends Utils {
    DcMotor towerMotor;
    DcMotor slideMotor;
    public CRServo intakeServo;
    double towerMotorPower;
    double slideMotorPower;
    double intakeServoPower;
    int towerMotorPos;

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

        towerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public static void holdPosition(int pos, DcMotor motor) {
        motor.setTargetPosition(pos);
    }

    /**
     * Main processing loop of PinkArm
     * @param gamepad All input from gamepad 2 as gamepad obj
     */
    public void processInput(Gamepad gamepad) {
        //TODO: Fix pinkarm FFL stabilization
        //Grab powers
        //towerMotorPos += (int) ((gamepad.left_trigger - gamepad.right_trigger) * Constants.SCALER);
        towerMotorPower = (gamepad.left_trigger - gamepad.right_trigger);
        slideMotorPower = gamepad.left_stick_y;
        //CRServo power logic
        //TODO: Curb outtake power
        if (gamepad.left_bumper) { //Intake
            intakeServoPower = Constants.INTAKE_POWER;
        }
        else if (gamepad.right_bumper) { //Outtake
            intakeServoPower = Constants.OUTTAKE_POWER;
        }
        else {
            intakeServoPower = 0;
        }
        //Send power to devices
        //holdPosition(towerMotorPos,towerMotor);
        towerMotor.setPower(towerMotorPower);
        slideMotor.setPower(slideMotorPower);
        intakeServo.setPower(intakeServoPower);
    }

    public double getTowerMotorHypotheticalPos() {
        return towerMotorPos;
    }
    /**
     * Zeros tower and slide motor encoders
     */
    public void zeroEncoders() {
        //Refresh encoder values to 0 at current position
        towerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Sends Enum command RUN_USING_ENCODERS to motors
     */
    public void restartMotors() {
        towerMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
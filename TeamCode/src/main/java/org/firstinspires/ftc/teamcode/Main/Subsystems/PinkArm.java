package org.firstinspires.ftc.teamcode.Main.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Main.Constants;
import org.firstinspires.ftc.teamcode.Main.Helpers.beaUtils;

public class PinkArm extends beaUtils {
    DcMotor towerMotor;
    DcMotor slideMotor;
    public CRServo intakeServo;
    double towerMotorPower;
    double slideMotorPower;
    double intakeServoPower;

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

    /**
     * Main processing loop of PinkArm
     * @param gamepad All input from gamepad 2 as gamepad obj
     */
    public void processInput(Gamepad gamepad) {
        //Grab powers
        towerMotorPower = gamepad.left_trigger - gamepad.right_trigger;
        slideMotorPower = gamepad.left_stick_y;
        //CRServo power logic
        if (gamepad.left_bumper) {
            intakeServoPower = 1;
        }
        else if (gamepad.right_bumper) {
            intakeServoPower = -1;
        }
        else {
            intakeServoPower = 0;
        }
        //Send power to devices
        towerMotor.setPower(towerMotorPower);
        slideMotor.setPower(slideMotorPower);
        intakeServo.setPower(intakeServoPower);
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
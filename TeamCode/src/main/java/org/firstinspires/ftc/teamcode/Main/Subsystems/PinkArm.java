package org.firstinspires.ftc.teamcode.Main.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Main.Helpers.beaUtils;

public class PinkArm {
    DcMotor towerMotor;
    DcMotor slideMotor;
    CRServo intakeServo;
    public PinkArm (DcMotor towerMotor, DcMotor slideMotor, CRServo intakeServo) {
        this.towerMotor = towerMotor;
        this.slideMotor = slideMotor;
        this.intakeServo = intakeServo;

        towerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void processInput(Gamepad gamepad) {
        //Tower Motor
        // TODO: Look into futures/promises to get rid of these wack if statements all over the place
        if (beaUtils.triggerBoolean(gamepad.left_trigger)) { //Start moving servo to position 0
            towerMotor.setPower(1);
        }
        else if (beaUtils.triggerBoolean(gamepad.right_trigger)) { //Start moving servo to position 1
            towerMotor.setPower(-1);
        }
        else {
            towerMotor.setPower(0);
        }
        //CRServo intake
        if (gamepad.left_bumper) {
            intakeServo.setPower(1);
        }
        else if (gamepad.right_bumper) {
            intakeServo.setPower(-1);
        }
        else {
            intakeServo.setPower(0);
        }
        //Slide Motor
        slideMotor.setPower(gamepad.left_stick_y);
    }
    public double getTowerMotorPosition() {
        return towerMotor.getCurrentPosition();
    }

    public double getSlideMotorPosition() {
        return slideMotor.getCurrentPosition();
    }
    public void zeroEncoders() {
        //Refresh encoder values to 0 at current position
        towerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    /**
     * Generic acceptance of runModes
     * @param runMode DcMotor.RunMode.[const here]
     */
    public void sendEncodersCommands(DcMotor.RunMode runMode) {
        towerMotor.setMode(runMode);
        slideMotor.setMode(runMode);
    }
}

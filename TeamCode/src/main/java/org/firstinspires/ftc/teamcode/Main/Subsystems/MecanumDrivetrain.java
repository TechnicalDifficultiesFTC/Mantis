package org.firstinspires.ftc.teamcode.Main.Subsystems;

import org.firstinspires.ftc.teamcode.Main.Constants;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

public class MecanumDrivetrain {
    static DcMotor frontLeftMotor;
    static DcMotor backLeftMotor;
    static DcMotor frontRightMotor;
    static DcMotor backRightMotor;

    public MecanumDrivetrain(DcMotor FLM, DcMotor BLM, DcMotor FRM, DcMotor BRM){
        frontLeftMotor = FLM;
        backLeftMotor = BLM;
        frontRightMotor = FRM;
        backRightMotor = BRM;
        // TODO: Full Zero Power Brake all drivetrain motors?
        //These motors have to be set to reverse by standard so that Mecanum works
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    double y;
    double x;
    double rx;
    double denominator;
    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;
    boolean lowPowerMode;

    double modulator;
    public void processInput(Gamepad gamepad){
        if (gamepad.dpad_right) {
            lowPowerMode = true;
        }
        else if (gamepad.dpad_left) {
            lowPowerMode = false;
        }

        modulator = lowPowerMode ? Constants.MIN_SPEED : Constants.MAX_SPEED;

        y = -gamepad.left_stick_y;
        x = gamepad.left_stick_x * 1.1;
        rx = gamepad.right_stick_x;

        denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        frontLeftPower = ((y + x + rx) / denominator)*modulator;
        backLeftPower = ((y - x + rx) / denominator)*modulator;
        frontRightPower = ((y - x - rx) / denominator)*modulator;
        backRightPower = ((y + x - rx) / denominator)*modulator;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }

    public boolean isLowPowerMode() {
        return lowPowerMode;
    }
}
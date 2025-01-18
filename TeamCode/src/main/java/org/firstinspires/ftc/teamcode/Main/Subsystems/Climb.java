package org.firstinspires.ftc.teamcode.Main.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Main.Constants;

public class Climb {
    DcMotor climbMotorLeft;
    DcMotor climbMotorRight;
    boolean climbLowPowerMode;
    public Climb (DcMotor climbMotorLeft, DcMotor climbMotorRight) {
        this.climbMotorLeft = climbMotorLeft;
        this.climbMotorRight = climbMotorRight;

        climbMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        climbMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        climbMotorLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        climbMotorRight.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    private void setArmsPower(float power) {
        boolean isLowPowerMode = climbLowPowerMode;

        if (power > 1 || power < -1 ) {
            throw new RuntimeException();
        }

        double modulator = isLowPowerMode ? Constants.MIN_SPEED : Constants.MAX_SPEED;

        climbMotorRight.setPower(power*modulator);
        climbMotorLeft.setPower(power*modulator);
    }

    public String getClimbArmsPositionAsString() {
        return "Climb Arm Left pos (encoder ticks) = " + climbMotorLeft.getCurrentPosition() +
                "\nClimb Arm Right pos (encoder ticks) = " + climbMotorRight.getCurrentPosition();
    }

    public boolean isClimbLowPowerMode() {
        return climbLowPowerMode;
    }
    public void processInput(Gamepad gamepad) {
        if (gamepad.square) {
            climbLowPowerMode = !climbLowPowerMode;
        }
        setArmsPower(gamepad.right_stick_y);
    }
}

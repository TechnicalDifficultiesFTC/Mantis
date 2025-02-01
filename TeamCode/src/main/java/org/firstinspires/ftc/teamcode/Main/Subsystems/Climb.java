package org.firstinspires.ftc.teamcode.Main.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.Debounce;

public class Climb {
    DcMotor climbMotorLeft;
    DcMotor climbMotorRight;
    boolean climbLowPowerMode = false;
    boolean climbLockDown = false;
    Debounce circleDebounce = new Debounce();
    Debounce sqaureDebounce = new Debounce();
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

        double modulator = isLowPowerMode ? Config.MIN_CLIMB_SPEED : Config.MAX_CLIMB_SPEED;

        climbMotorRight.setPower(power*modulator);
        climbMotorLeft.setPower(power*modulator);
    }

    public String getClimbArmsPositionAsString() {
        return "Climb Arm Left pos (encoder ticks) = " + climbMotorLeft.getCurrentPosition() +
                "\nClimb Arm Right pos (encoder ticks) = " + climbMotorRight.getCurrentPosition();
    }

    public boolean isClimbLockedDown() {
        return climbLockDown;
    }
    public boolean isClimbLowPowerMode() {
        return climbLowPowerMode;
    }
    public void processInput(Gamepad gamepad) {
        if (sqaureDebounce.isPressed(gamepad.square)) {
            climbLowPowerMode = !climbLowPowerMode;
        }
        if (circleDebounce.isPressed(gamepad.circle)) {
            climbLockDown = !climbLockDown;
        }

        //If climb is locked to down send climb motors down
        //If climb is unlocked then power scales off of the right stick y
        setArmsPower(climbLockDown ? Config.CLIMB_LOCK_DOWN_POWER : gamepad.right_stick_y);
    }
}

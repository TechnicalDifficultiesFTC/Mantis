package org.firstinspires.ftc.teamcode.Main.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

public class Climb {
    DcMotor climbMotorLeft;
    DcMotor climbMotorRight;
    public Climb (DcMotor climbMotorLeft, DcMotor climbMotorRight) {
        this.climbMotorLeft = climbMotorLeft;
        this.climbMotorRight = climbMotorRight;

        climbMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        climbMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        climbMotorLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        climbMotorRight.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private void setArmsPower(int power,boolean isLowPowerMode) {
        if (power > 1 || power < -1 ) {
            throw new RuntimeException();
        }
        double modulator = isLowPowerMode ? .5 : 1;
        climbMotorRight.setPower(power);
        climbMotorLeft.setPower(power);
    }

    public String getClimbArmsPositionAsString() {
        return "Climb Arm Left pos (encoder ticks) = " + climbMotorLeft.getCurrentPosition() +
                "\nClimb Arm Right pos (encoder ticks) = " + climbMotorRight.getCurrentPosition();
    }
    public void processInput(Gamepad gamepad,boolean isLowPowerMode) {
        if (gamepad.dpad_up) {
            setArmsPower(1,isLowPowerMode);
        }
        if (gamepad.dpad_down) {
            setArmsPower(-1,isLowPowerMode);
        }
        if (gamepad.dpad_up && gamepad.dpad_down) {
            setArmsPower(0,isLowPowerMode);
        }
    }
}

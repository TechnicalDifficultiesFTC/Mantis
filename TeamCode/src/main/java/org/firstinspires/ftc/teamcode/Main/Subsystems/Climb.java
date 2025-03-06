package org.firstinspires.ftc.teamcode.Main.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.Debounce;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;

public class Climb {

    /*
    ------------------------------------------------------------------------------------ ACTIONS ZONE ------------------------------------------------------------------------------------
     */
    public class raiseClimbToFirstRung implements Action {
        boolean initialized = false;

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            boolean climbArmsAtPosition = (climbMotorRight.getCurrentPosition() == Config.climbArmsExtensionAmount_InTicks);
            if (!initialized) {
                //Initially set climb arms to extend to the first rung
                climbMotorRight.setTargetPosition(Config.climbArmsExtensionAmount_InTicks);
                initialized = true;
            }
            //If the climb motor is not at the right height, will rerun (TRUE CASE)
            //If the climb motor is at the right height, will not rerun and will maintain that height (FALSE CASE)
            return !climbArmsAtPosition;
        }
    }
    /*
    ------------------------------------------------------------------------------------ ACTIONS ZONE ------------------------------------------------------------------------------------
     */
    DcMotor climbMotorLeft;
    DcMotor climbMotorRight;
    boolean climbLowPowerMode = false;
    boolean climbLockDown = false;
    Debounce circleDebounce = new Debounce();
    Debounce sqaureDebounce = new Debounce();

    /**
     * Climb singleton constructor
     * @param hardwareMap Hardware map
     * @param runToPosition Run to position should only be true if this constructor is being invoked in auto
     */
    public Climb (HardwareMap hardwareMap,boolean runToPosition) {

        if (runToPosition) {
            //Zero encoders
            climbMotorLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            climbMotorRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //Prime to launch
            climbMotorLeft.setTargetPosition(Config.climbArmsExtensionAmount_InTicks);
            climbMotorRight.setTargetPosition(Config.climbArmsExtensionAmount_InTicks);
            //Release!
            climbMotorLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            climbMotorRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }
        else {
            climbMotorLeft = hardwareMap.dcMotor.get(DeviceRegistry.CLIMB_MOTOR_LEFT.str());
            climbMotorRight = hardwareMap.dcMotor.get(DeviceRegistry.CLIMB_MOTOR_RIGHT.str());

            climbMotorLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            climbMotorRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            climbMotorLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            climbMotorRight.setDirection(DcMotorSimple.Direction.FORWARD);
        }
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

    public String toString() {
        return ("Climb Encoders: \n" +
                "Climb Motor Left Position: " + climbMotorLeft.getCurrentPosition() + "\n" +
                "Climb Motor Right Position: " + climbMotorRight.getCurrentPosition());
    }
}

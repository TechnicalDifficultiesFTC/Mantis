package org.firstinspires.ftc.teamcode.Main.Subsystems;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.Debounce;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumDrivetrain {
    static DcMotor frontLeftMotor;
    static DcMotor backLeftMotor;
    static DcMotor frontRightMotor;
    static DcMotor backRightMotor;

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
    Debounce sqaureDebounce = new Debounce();

    /**
     * Initialize the drivetrain and pass in appropriate motor objects
     */
    public MecanumDrivetrain(HardwareMap hardwareMap){
        frontLeftMotor = hardwareMap.dcMotor.get(DeviceRegistry.FRONT_LEFT_MOTOR.str());
        backLeftMotor = hardwareMap.dcMotor.get(DeviceRegistry.BACK_LEFT_MOTOR.str());
        frontRightMotor = hardwareMap.dcMotor.get(DeviceRegistry.FRONT_RIGHT_MOTOR.str());
        backRightMotor = hardwareMap.dcMotor.get(DeviceRegistry.BACK_RIGHT_MOTOR.str());

        //These motors have to be set to reverse by standard so that Mecanum works
        frontLeftMotor.setDirection(Config.FRONT_LEFT_DT_MOTOR_FORWARD ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(Config.FRONT_RIGHT_DT_MOTOR_FORWARD ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(Config.BACK_LEFT_DT_MOTOR_FORWARD ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(Config.BACK_RIGHT_DT_MOTOR_FORWARD ? DcMotorSimple.Direction.FORWARD : DcMotorSimple.Direction.REVERSE);
    }

    /**
     * Sets the zero power behavior of *all* drivetrain motors to input parameter
     * @param zeroPowerBehavior A zeroPowerBehavior which can be found at DcMotor.ZeroPowerBehavior.[Const here]
     */
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        frontLeftMotor.setZeroPowerBehavior(zeroPowerBehavior);
        backLeftMotor.setZeroPowerBehavior(zeroPowerBehavior);
        frontRightMotor.setZeroPowerBehavior(zeroPowerBehavior);
        backRightMotor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    /**
     * Handle drivetrain logic and update motors as such
     * @param gamepad All input from gamepad (1)
     */
    public void processInput(Gamepad gamepad){

        if (sqaureDebounce.isPressed(gamepad.square)) {
            lowPowerMode = !lowPowerMode;
        }

        modulator = lowPowerMode ? Config.MIN_DT_SPEED : Config.MAX_DT_SPEED;

        y = -gamepad.left_stick_y;
        x = gamepad.left_stick_x * 1.1;
        rx = gamepad.right_stick_x;

        denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        frontLeftPower = ((y + x + rx)*modulator);
        frontRightPower = ((y - x - rx)*modulator);
        backLeftPower = ((y - x + rx)*modulator);
        backRightPower = ((y + x - rx)*modulator);

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
    }

    /**
     * Overloaded method of processInput which allows you to manually control direction and speed of
     * the drivetrain without controller input
     */
    public void processInput(DcMotorSimple.Direction frontLeftWheelDirection, DcMotorSimple.Direction frontRightWheelDirection,
                             DcMotorSimple.Direction backLeftWheelDirection , DcMotorSimple.Direction backRightWheelDirection, double power) {
        frontLeftMotor.setPower(frontLeftWheelDirection == DcMotorSimple.Direction.FORWARD ? power : -power);
        frontRightMotor.setPower(frontRightWheelDirection == DcMotorSimple.Direction.FORWARD ? power : -power);
        backLeftMotor.setPower(backLeftWheelDirection == DcMotorSimple.Direction.FORWARD ? power : -power);
        backRightMotor.setPower(backRightWheelDirection == DcMotorSimple.Direction.FORWARD ? power : -power);

    }
    /**
     * Returns state of field lowPowerMode
     * @return lowPowerMode
     */
    public boolean isDrivetrainLowPowerMode() {
        return lowPowerMode;
    }

    public void strafeLeft(double power) {
        //uhhhhhhhhh
        frontRightMotor.setPower(power);
        backLeftMotor.setPower(power);

        frontLeftMotor.setPower(-power);
        backRightMotor.setPower(-power);

    }

    public String getMotorsStatusAsString() {
        return "Front Left Power = " + frontLeftMotor.getPower() +
                "\nFront Right Power = " + frontRightMotor.getPower() +
                "\nBack Left Power = " + backLeftMotor.getPower() +
                "\nBack Right Power = " + backRightMotor.getPower();
    }

}
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class MecanumDrive extends LinearOpMode {

    //Initialize global variables and local functions
    //By global variables I mean ONLY variables that are gonna get accessed by beaUtils/other classes, not finals and stuff
    public String MOTM = beaUtils.generateVoiceLine();
    TelemetryCache cache = new TelemetryCache(MOTM);
    void uplink (String specifier, String message) {
        cache.updateTelemetryCache(specifier, message);
        telemetry.addData("LOG =", cache.compileTelemetryCache());
        telemetry.update();
    }

    //Make the motors static and public so other classes can access them
    public static DcMotor frontLeftMotor;
    public static DcMotor backLeftMotor;
    public static DcMotor frontRightMotor;
    public static DcMotor backRightMotor;

    final double limitingValue = 3;
    //Set between [1,inf), all motor power variables will be divided by this value when low power mode is active
    @Override
    public void runOpMode() throws InterruptedException {
        //Motor binding

        frontLeftMotor = hardwareMap.dcMotor.get("FLM");
        backLeftMotor = hardwareMap.dcMotor.get("BLM");
        frontRightMotor = hardwareMap.dcMotor.get("FRM");
        backRightMotor = hardwareMap.dcMotor.get("BRM");

        DcMotor shaftMotor = hardwareMap.dcMotor.get("shaftMotor");
        DcMotor slideMotor = hardwareMap.dcMotor.get("slideMotor");

        //I think right side is supposed to be flipped? (yes it is)
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return; //The stop button

        //(Default) Variables that need to be initialized OUTSIDE of the loop (so like not redefined a million billion times over)
        boolean low_power_mode = false;
        boolean wheelsUnlocked = true;

        uplink("Overview", "All components successfully initialized!");

        while (opModeIsActive()) { //Primary loop

            double power_limiter = low_power_mode ? limitingValue : 1;

            //IF low power mode is active set value of power limiter to 2, if not keep it as 1
            //Values get divided by low power mode, so having it active cuts speeds in half

            double y = -gamepad1.left_stick_y; //y stick inverted
            double x = gamepad1.left_stick_x * 1.1; //counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;

            //Handle calculations for each motor in separate variables
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1); //Prevent division by 0 by making sure absolute values stay above 1? (I think?)
            double frontLeftPower = ((y + x + rx) / denominator)/ power_limiter;
            double backLeftPower = ((y - x + rx) / denominator)/ power_limiter;
            double frontRightPower = ((y - x - rx) / denominator)/ power_limiter;
            double backRightPower = ((y + x - rx) / denominator)/ power_limiter;

            //Send power variables to motors

            if (wheelsUnlocked) { //Only pushes power values to the motors if the robot is allowing itself to move
                beaUtils.pushDrivetrainPower(frontLeftPower,backLeftPower,frontRightPower,backRightPower);
            }

            //slideMotor TESTING
            if (beaUtils.triggerBoolean(gamepad1.left_trigger)) { //Start moving servo to position 0
                slideMotor.setPower(1);
                uplink("SlideMotor","Power FORWARD");
            }
            else if (beaUtils.triggerBoolean(gamepad1.right_trigger)) { //Start moving servo to position 1
                slideMotor.setPower(-1);
                uplink("SlideMotor", "Power REVERSE");
            }
            else {
                slideMotor.setPower(0);
                uplink("SlideMotor","Power NEUTRAL");
            }

            //shaftMotor TESTING
            if (beaUtils.triggerBoolean(gamepad2.left_trigger)) { //Start moving servo to position 0
                shaftMotor.setPower(1);
                uplink("SlideMotor","Power FORWARD");
            }
            else if (beaUtils.triggerBoolean(gamepad2.right_trigger)) { //Start moving servo to position 1
                shaftMotor.setPower(-1);
                uplink("SlideMotor", "Power REVERSE");
            }
            else {
                shaftMotor.setPower(0);
                uplink("SlideMotor","Power NEUTRAL");
            }

            //Button triggers
            if (gamepad1.dpad_up) {
                low_power_mode = true;
                uplink("PowerStatus", "Low Power Mode ONLINE");
            }

            if (gamepad1.dpad_down) {
                low_power_mode = false;
                uplink("PowerStatus", "Low Power Mode OFFLINE");
            }

            if (gamepad1.dpad_left) {
                wheelsUnlocked = false;
                uplink("Movement","Wheels LOCKED :(");
            }

            if (gamepad1.dpad_right) {
                wheelsUnlocked = true;
                uplink("Movement", "Wheels UNLOCKED :)");
            }

        }
    }
}

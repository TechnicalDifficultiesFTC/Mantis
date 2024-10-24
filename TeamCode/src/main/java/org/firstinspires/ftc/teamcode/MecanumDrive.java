package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class MecanumDrive extends LinearOpMode {

    //Initialize global variables and local functions
    //By global variables I mean ONLY variables that are gonna get accessed by beaUtils/other classes, not finals and stuff
    public String MOTM = beaUtils.generateVoiceLine();
    String overviewInit = "Overview: Greetings Programs";
    String slideMotorInit = "SlideMotor: Offline";
    String servoInit = "Servo: Offline";
    String powerStatus = "Power: Asleep";
    TelemetryCache cache = new TelemetryCache(MOTM,overviewInit,slideMotorInit,servoInit,powerStatus);
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
    public static DcMotor towerMotor;
    final double limitingValue = 3;
    //Set between [1,inf), all motor power variables will be divided by this value when low power mode is active
    @Override
    public void runOpMode() throws InterruptedException {
        //Motor binding

        frontLeftMotor = hardwareMap.dcMotor.get("FLM");
        backLeftMotor = hardwareMap.dcMotor.get("BLM");
        frontRightMotor = hardwareMap.dcMotor.get("FRM");
        backRightMotor = hardwareMap.dcMotor.get("BRM");
        towerMotor = hardwareMap.dcMotor.get("shaftMotor");
        //This guy doesn't really need to be field </3
        DcMotor slideMotor = hardwareMap.dcMotor.get("slideMotor");

        //Motor information mapping (only really necessary if doing stuff w/ encoders)
        MotorInformation towerMotorInformation = new MotorInformation(towerMotor,5281.1);
        MotorInformation slideMotorInformation = new MotorInformation(slideMotor,587.7);

        //Non-motor bindings
        CRServo intakeServo = hardwareMap.crservo.get("intakeServo");

        //Motor Configs
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Should I set the whole drivetrain to brake?
        towerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Refresh encoder values to 0 at current position
        towerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        if (isStopRequested()) return;

        //(Default) Variables that can be initialized OUTSIDE of the loop (so like not redefined a million billion times over)
        boolean low_power_mode = false;
        double power_limiter;
        double y;
        double x;
        double rx;
        double denominator;
        double frontLeftPower;
        double backLeftPower;
        double frontRightPower;
        double backRightPower;
        uplink("Overview", "All components successfully initialized!");

        while (opModeIsActive()) { //Primary loop

            power_limiter = low_power_mode ? limitingValue : 1;
            double towerEncoderPos = beaUtils.pinkArmEncoderToDegreeConversion(towerMotorInformation);
            double slideEncoderPos = beaUtils.pinkArmEncoderToDegreeConversion(slideMotorInformation);

            //IF low power mode is active set value of power limiter to 2, if not keep it as 1
            //Values get divided by low power mode, so having it active cuts speeds in half

            y = -gamepad1.left_stick_y; //y stick inverted
            x = gamepad1.left_stick_x * 1.1; //counteract imperfect strafing
            rx = -gamepad1.right_stick_x;

            //Handle calculations for each motor in separate variables
            denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1); //Prevent division by 0 by making sure absolute values stay above 1? (I think?)
            frontLeftPower = ((y + x + rx) / denominator)/ power_limiter;
            backLeftPower = ((y - x + rx) / denominator)/ power_limiter;
            frontRightPower = ((y - x - rx) / denominator)/ power_limiter;
            backRightPower = ((y + x - rx) / denominator)/ power_limiter;

            //Send power variables to motors
            beaUtils.pushDrivetrainPower(frontLeftPower,backLeftPower,frontRightPower,backRightPower);

            //slideMotor TESTING
            if (beaUtils.triggerBoolean(gamepad1.left_trigger)) {
                slideMotor.setPower(1);
                uplink("SlideMotor","Power FORWARD");
            }
            else if (beaUtils.triggerBoolean(gamepad1.right_trigger)) {
                slideMotor.setPower(-1);
                uplink("SlideMotor", "Power REVERSE");
            }
            else {
                slideMotor.setPower(0);
                uplink("SlideMotor","Power NEUTRAL");
            }

            //towerMotor TESTING
            if (beaUtils.triggerBoolean(gamepad2.left_trigger)) { //Start moving servo to position 0
                towerMotor.setPower(1);
                uplink("towerMotor","Power FORWARD");
            }
            else if (beaUtils.triggerBoolean(gamepad2.right_trigger)) { //Start moving servo to position 1
                towerMotor.setPower(-1);
                uplink("towerMotor", "Power REVERSE");
            }
            else {
                towerMotor.setPower(0);
                uplink("towerMotor","Power NEUTRAL");
            }

            //Button triggers
            if (gamepad1.dpad_right) {
                low_power_mode = true;
                uplink("PowerStatus", "Low Power Mode ONLINE");
            }

            else if (gamepad1.dpad_left) {
                low_power_mode = false;
                uplink("PowerStatus", "Low Power Mode OFFLINE");
            }

            if (gamepad2.left_bumper) {
                intakeServo.setPower(1);
            }
            else if (gamepad2.right_bumper) {
                intakeServo.setPower(-1);
            }
            else {
                intakeServo.setPower(0);
            }

            uplink("EncoderPos", "Tower Encoder Position: "+ towerEncoderPos+
            "\nSlide Encoder Position: "+slideEncoderPos);
        }
    }
}

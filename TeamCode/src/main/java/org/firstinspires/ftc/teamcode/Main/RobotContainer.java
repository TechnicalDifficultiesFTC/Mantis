package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Helpers.beaUtils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="Eradicator V:A", group="Linear OpMode")
public class RobotContainer extends LinearOpMode {

    //Initialize global variables and local functions
    //By global variables I mean ONLY variables that are gonna get accessed by beaUtils/other classes, not finals and stuff
    public String MOTM = beaUtils.generateVoiceLine();

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Overview: ","Initialized");
        telemetry.addData("MOTM: ",MOTM);
        //Grab devices

        //Drivetrain
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("FLM");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("BLM");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("FRM");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("BRM");

        //Pink Arm
        CRServo intakeServo = hardwareMap.crservo.get("intakeServo");
        DcMotor towerMotor = hardwareMap.dcMotor.get("towerMotor");
        DcMotor slideMotor = hardwareMap.dcMotor.get("slideMotor");

        MecanumDrivetrain drivetrain = new MecanumDrivetrain(frontLeftMotor,backLeftMotor,frontRightMotor,backRightMotor);
        PinkArm pinkArm = new PinkArm(towerMotor,slideMotor,intakeServo);

        //Drivetrain initial setup
        drivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //PinkArm initial setup
        pinkArm.zeroEncoders(); //WILL KILL MOTORS
        pinkArm.sendEncodersCommands(DcMotor.RunMode.RUN_USING_ENCODER); //Fingers crossed will restart motors

        telemetry.update();

        //Nothing past this point will run until the start button is pressed
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) { //Primary loop

            drivetrain.processInput(gamepad1);
            pinkArm.processInput(gamepad2);

            //Telemetry
            telemetry.addData("Overview: ","Online");
            telemetry.addData("MOTM: ",MOTM);
            telemetry.addLine("Low Power Mode Status: "+ drivetrain.isLowPowerMode());
            telemetry.addLine("Slide Motor Power: "+ slideMotor.getPower());
            telemetry.addLine("Tower Motor Power: "+ towerMotor.getPower());
            telemetry.addLine("Tower Encoder Position (revs): "+ beaUtils.getEncoderRevolutions(towerMotor.getCurrentPosition(),Constants.TOWER_MOTOR_PPR));
            telemetry.addLine("Slide Encoder Position (revs): "+ beaUtils.getEncoderRevolutions(slideMotor.getCurrentPosition(),Constants.SLIDE_MOTOR_PPR));
            telemetry.update();
        }
    }
}

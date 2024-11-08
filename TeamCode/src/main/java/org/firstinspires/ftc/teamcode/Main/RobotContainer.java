package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
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
        telemetry.addLine(Constants.dasshTag);
        //Grab devices

        //Drivetrain
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(DeviceRegistry.FRONT_LEFT_MOTOR.str());
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(DeviceRegistry.BACK_LEFT_MOTOR.str());
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(DeviceRegistry.FRONT_RIGHT_MOTOR.str());
        DcMotor backRightMotor = hardwareMap.dcMotor.get(DeviceRegistry.BACK_RIGHT_MOTOR.str());

        //Pink Arm
        CRServo intakeServo = hardwareMap.crservo.get(DeviceRegistry.INTAKE_SERVO.str());
        DcMotor towerMotor = hardwareMap.dcMotor.get(DeviceRegistry.TOWER_MOTOR.str());
        DcMotor slideMotor = hardwareMap.dcMotor.get(DeviceRegistry.SLIDE_MOTOR.str());

        MecanumDrivetrain drivetrain = new MecanumDrivetrain(frontLeftMotor,backLeftMotor,frontRightMotor,backRightMotor);
        PinkArm pinkArm = new PinkArm(towerMotor,slideMotor,intakeServo);

        //Drivetrain initial setup
        drivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //PinkArm initial setup
        pinkArm.zeroEncoders(); //WILL KILL MOTORS
        pinkArm.restartMotors(); //Fingers crossed will restart motors

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
            telemetry.addLine();
            telemetry.addLine("Slide Motor Power: "+ slideMotor.getPower());
            telemetry.addLine("Tower Motor Power: "+ towerMotor.getPower());
            telemetry.addLine();
            telemetry.addLine("Tower Encoder Position (revs): "+ pinkArm.getTowerMotorRevolutions());
            telemetry.addLine("Slide Encoder Position (revs): "+ pinkArm.getSlideMotorRevolutions());
            telemetry.addLine();
            telemetry.addLine("ASSUMED TOWER DEGREES: "+ pinkArm.getTowerDegree());
            telemetry.update();
        }
    }
}
package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Helpers.Constants;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Subsystems.Climb;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="MANTIS V:B3", group="Linear OpMode")

public class RobotContainer extends LinearOpMode {
//TODO: Test FTC dashboard
//TODO: Tune RoadRunner
    @Override
    public void runOpMode() throws InterruptedException {

        final String MOTM = Utils.generateMOTMLine();

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

        //Climb
        DcMotor climbMotorLeft = hardwareMap.dcMotor.get(DeviceRegistry.CLIMB_MOTOR_LEFT.str());
        DcMotor climbMotorRight = hardwareMap.dcMotor.get(DeviceRegistry.CLIMB_MOTOR_RIGHT.str());

        //Create subsystem singletons
        MecanumDrivetrain drivetrain = new MecanumDrivetrain(frontLeftMotor,backLeftMotor,frontRightMotor,backRightMotor);
        PinkArm pinkArm = new PinkArm(towerMotor,slideMotor,intakeServo);
        Climb climb = new Climb(climbMotorLeft,climbMotorRight);

        //Drivetrain initial setup
        drivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //PinkArm initial setup
        pinkArm.zeroEncoders(); //WILL KILL MOTORS
        pinkArm.restartMotors(); //Fingers crossed will restart motors

        telemetry.addLine("Systems Registered!");
        telemetry.update();

        //Nothing past this point will run until the start button is pressed
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) { //Primary loop
            //TODO: Implement pinkarm FFL debugging
            //Send input to subsystems from processing
            drivetrain.processInput(gamepad1);
            pinkArm.processInput(gamepad2);
            climb.processInput(gamepad2);

            //Telemetry
            telemetry.addLine("Overview: Online");
            telemetry.addData("MOTM: ", MOTM);
            telemetry.addLine("Low Power Mode Status (DT): " + drivetrain.isDrivetrainLowPowerMode());
            telemetry.addLine("Low Power Mode Status (CLIMB): " + climb.isClimbLowPowerMode());
            telemetry.addLine();
            telemetry.addLine("Climb lockdown status: " + climb.isClimbLockedDown());
            telemetry.addLine("Actual intakeServo power: " + intakeServo.getPower());
            telemetry.addLine("Buttons (LEFT/RIGHT) (gamepad2): " + gamepad2.left_bumper + "/" + gamepad2.right_bumper);
            telemetry.addLine();
            //telemetry.addLine("Slide Motor Power: " + slideMotor.getPower());
            //telemetry.addLine("Tower Motor Power: " + towerMotor.getPower());
            //telemetry.addLine();
            //telemetry.addLine("Drivetrain Status: \n" + drivetrain.getMotorsStatusAsString());
            //telemetry.addLine();
            //telemetry.addLine("Climb positional status: \n" + climb.getClimbArmsPositionAsString());
            //telemetry.addLine("HYPOTHETICAL tower pos: " + pinkArm.getTowerMotorHypotheticalPos());
            //telemetry.addLine("ACTUAL tower pos: " + towerMotor.getCurrentPosition());
            //telemetry.addLine();
            if (Constants.SHOW_CTRL1) {
                telemetry.addLine("GAMEPAD1 STATUS: \n" + gamepad1.toString());
            }
            if (Constants.SHOW_CTRL2) {
                telemetry.addLine("GAMEPAD2 STATUS: \n " + gamepad2.toString());
            }
            //Push telemetry log to driver hub console
            telemetry.update();
        }
    }
}
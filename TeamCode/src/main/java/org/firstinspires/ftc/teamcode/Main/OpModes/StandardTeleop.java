package org.firstinspires.ftc.teamcode.Main.OpModes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Subsystems.Climb;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="MANTIS V:1", group="Linear OpMode")

public class StandardTeleop extends LinearOpMode {
//TODO: Test FTC dashboard
//TODO: Tune RoadRunner
    @Override
    public void runOpMode() throws InterruptedException {

        final String MOTM = Utils.generateMOTMLine();

        telemetry.addLine(Config.dasshTag);
        //Grab devices

        //Drivetrain
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(DeviceRegistry.FRONT_LEFT_MOTOR.str());
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(DeviceRegistry.BACK_LEFT_MOTOR.str());
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(DeviceRegistry.FRONT_RIGHT_MOTOR.str());
        DcMotor backRightMotor = hardwareMap.dcMotor.get(DeviceRegistry.BACK_RIGHT_MOTOR.str());

        //Pink Arm
        CRServo intakeServo = hardwareMap.crservo.get(DeviceRegistry.INTAKE_SERVO.str());
        intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);

        //Create subsystem singletons
        MecanumDrivetrain drivetrain = new MecanumDrivetrain(frontLeftMotor,backLeftMotor,frontRightMotor,backRightMotor);
        PinkArm pinkArm = new PinkArm(hardwareMap);
        Climb climb = new Climb(hardwareMap);

        //Drivetrain initial setup
        drivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //PinkArm initial setup
        pinkArm.setArmMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); //Kill encoders initially, setting positions to 0 upon initialization
        pinkArm.setPosZero();
        pinkArm.setArmMode(Config.ARM_MODE); //Defined in config class;

        telemetry.addLine("Systems Registered!");
        telemetry.update();


        //FOR TESTING!!
        final Encoder par0, par1, perp;
        par0 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, DeviceRegistry.LEFT_THROUGHBORE_ENC.str())));
        par1 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, DeviceRegistry.RIGHT_THROUGHBORE_ENC.str())));
        perp = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, DeviceRegistry.YAW_THROUGHBORE_ENC.str())));
        //Nothing past this point will run until the start button is pressed
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) { //Primary loop
            //Send input to subsystems from processing
            drivetrain.processInput(gamepad1);
            pinkArm.processInput(gamepad2);
            climb.processInput(gamepad2);

            //Telemetry
            telemetry.addLine("Overview: Online");
            telemetry.addLine("MOTM: " + MOTM);
            telemetry.addLine("Uplink speed (MS): " + telemetry.getMsTransmissionInterval());
            telemetry.addLine("Low Power Mode Status (DT): " + drivetrain.isDrivetrainLowPowerMode());
            telemetry.addLine("Low Power Mode Status (CLIMB): " + climb.isClimbLowPowerMode());
            telemetry.addLine();
            telemetry.addLine("Climb lockdown status: " + climb.isClimbLockedDown());
            telemetry.addLine("Actual intakeServo power: " + intakeServo.getPower());
            telemetry.addLine("Buttons (LEFT/RIGHT) (gamepad2): " + gamepad2.left_bumper + "/" + gamepad2.right_bumper);
            telemetry.addLine("Tower Motor Pos: " + pinkArm.getTowerMotorHypotheticalPos());
            telemetry.addLine();
            telemetry.addLine("Slide arm extension (ticks): " + pinkArm.pinkArmExtensionTicks);
            telemetry.addLine("Slide arm extension limit reached?: " + pinkArm.pastExtensionLimit());
            telemetry.addLine();
            if (Config.SHOW_ARM_STATUS) {
                telemetry.addLine();
                telemetry.addLine("Slide Motor Power: " + pinkArm.slideMotor.getPower());
                telemetry.addLine("Tower Motor Power: " + pinkArm.towerMotor.getPower());
                telemetry.addLine();
                telemetry.addLine("Climb positional status: \n" + climb.getClimbArmsPositionAsString());
                telemetry.addLine("HYPOTHETICAL tower pos: " + pinkArm.getTowerMotorHypotheticalPos());
                telemetry.addLine("ACTUAL tower pos: " + pinkArm.towerMotor.getCurrentPosition());
                telemetry.addLine();
            }
            if (Config.SHOW_DT_STATUS) {
                telemetry.addLine();
                telemetry.addLine("Drivetrain Status: \n" + drivetrain.getMotorsStatusAsString());
                telemetry.addLine();
            }
            if (Config.SHOW_ENCODER_DATA) {
                telemetry.addLine();
                telemetry.addLine(
                        "Encoders: +\n Perp: " + perp.getPositionAndVelocity().velocity +
                        "\nPar0: " + par0.getPositionAndVelocity().velocity +
                        "\nPar1: " + par1.getPositionAndVelocity().velocity
                );
                telemetry.addLine();
            }
            if (Config.SHOW_CTRL1) {
                telemetry.addLine();
                telemetry.addLine("GAMEPAD1 STATUS: \n" + gamepad1.toString());
                telemetry.addLine();
            }
            if (Config.SHOW_CTRL2) {
                telemetry.addLine();
                telemetry.addLine("GAMEPAD2 STATUS: \n " + gamepad2.toString());
                telemetry.addLine();
            }
            //Push telemetry log to driver hub console
            telemetry.update();
        }
    }
}
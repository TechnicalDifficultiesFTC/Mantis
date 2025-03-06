package org.firstinspires.ftc.teamcode.Main.OpModes.TeleOps;

import android.bluetooth.BluetoothClass;

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
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.ThreeDeadWheelLocalizer;

@TeleOp(name="MANTIS V:1.5", group="Linear OpMode")

public class StandardTeleop extends LinearOpMode {
    OverflowEncoder perp;
    OverflowEncoder par0;
    OverflowEncoder par1;

    @Override
    public void runOpMode() throws InterruptedException {

        final String MOTM = Utils.generateMOTMLine();
        telemetry.addLine(Config.dasshTag);

        //Create subsystem singletons
        MecanumDrivetrain drivetrain = new MecanumDrivetrain(hardwareMap);
        PinkArm pinkArm = new PinkArm(hardwareMap,false);
        Climb climb = new Climb(hardwareMap,false);

        //Drivetrain initial setup
        drivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addLine("Systems Registered!");
        telemetry.update();

        //Nothing past this point will run until the start button is pressed
        waitForStart();

        telemetry.setMsTransmissionInterval(10);
        par0 = new OverflowEncoder(new RawEncoder(
                (DcMotorEx) hardwareMap.dcMotor.get(DeviceRegistry.LEFT_THROUGHBORE_ENC.str())));
        par1 = new OverflowEncoder(new RawEncoder(
                (DcMotorEx) hardwareMap.dcMotor.get(DeviceRegistry.RIGHT_THROUGHBORE_ENC.str())));
        perp = new OverflowEncoder(new RawEncoder(
                (DcMotorEx) hardwareMap.dcMotor.get(DeviceRegistry.YAW_THROUGHBORE_ENC.str())));
        par0.setDirection(DcMotorSimple.Direction.FORWARD);
        par1.setDirection(DcMotorSimple.Direction.FORWARD);

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
            telemetry.addLine("Actual intakeServo power: " + pinkArm.intakeServo.getPower());
            telemetry.addLine("Buttons (LEFT/RIGHT) (gamepad2): " + gamepad2.left_bumper + "/" + gamepad2.right_bumper);
            telemetry.addLine("Tower Motor Pos: " + pinkArm.getTowerMotorHypotheticalPos());
            telemetry.addLine();
            telemetry.addLine("Slide arm extension (ticks): " + pinkArm.pinkArmExtensionTicks);
            telemetry.addLine("Slide arm extension limit reached?: " + pinkArm.pinkArmHasExceededBounds());
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
                try {
                    telemetry.addLine(
                            "Encoders: +\n Perp: " + perp.getPositionAndVelocity().position +
                                    "\nPar0: " + par0.getPositionAndVelocity().position +
                                    "\nPar1: " + par1.getPositionAndVelocity().position
                    );
                }
                catch (NullPointerException e) {
                    telemetry.addLine("Encoders Null! Retrying!");
                }
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
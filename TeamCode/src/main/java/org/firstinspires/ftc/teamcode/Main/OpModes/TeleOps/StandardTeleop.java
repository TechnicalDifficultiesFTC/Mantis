package org.firstinspires.ftc.teamcode.Main.OpModes.TeleOps;

import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.external.samples.SampleRevBlinkinLedDriver;
import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Subsystems.Climb;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="MANTIS V:1.52", group="Primary OpMode")

public class StandardTeleop extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        final String motm = Utils.generateMOTMLine();
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

        if (isStopRequested()) return;

        while (opModeIsActive()) { //Primary loop

            //Send input to subsystems from processing
            drivetrain.processInput(gamepad1);
            pinkArm.processInput(gamepad2);
            climb.processInput(gamepad2);

            //Telemetry
            telemetry.addLine("Overview: Online | Uplink Speed = " + telemetry.getMsTransmissionInterval());
            telemetry.addLine("MOTM: " + motm);
            telemetry.addLine("Low Power Mode Status (DT): " + drivetrain.isDrivetrainLowPowerMode());
            telemetry.addLine("Low Power Mode Status (CLIMB): " + climb.isClimbLowPowerMode());
            telemetry.addLine();
            telemetry.addLine("Tower Motor Position: " + pinkArm.towerMotor.getCurrentPosition());
            telemetry.addLine("DESIRED Tower Motor POS: " + pinkArm.towerMotorPos);
            telemetry.addLine("Tower Motor GOTO: " + pinkArm.towerMotor.getTargetPosition());
            telemetry.addLine("Climb lockdown status: " + climb.isClimbLockedDown());
            telemetry.addLine();
            telemetry.addLine("Slide arm extension limit in effect?: " + pinkArm.isPinkArmExtensionLimitInEffect());
            telemetry.addLine("Slide arm extension limit reached?: " + pinkArm.pinkArmExtensionLimitShouldBeApplied());
            telemetry.addLine("Slide arm extension ticks: " + pinkArm.slideMotor.getCurrentPosition());
            telemetry.addLine();

            //Push telemetry log to driver hub console
            telemetry.update();
        }
    }
}
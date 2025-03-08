package org.firstinspires.ftc.teamcode.Main.OpModes.TeleOps;

import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Subsystems.Climb;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="MANTIS V:1.5", group="Linear OpMode")

public class StandardTeleop extends LinearOpMode {
    OverflowEncoder perp;
    OverflowEncoder par0;
    OverflowEncoder par1;

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
            telemetry.addLine("Overview: Online");
            telemetry.addLine("motm: " + motm);
            telemetry.addLine("Uplink speed (MS): " + telemetry.getMsTransmissionInterval());
            telemetry.addLine("Low Power Mode Status (DT): " + drivetrain.isDrivetrainLowPowerMode());
            telemetry.addLine("Low Power Mode Status (CLIMB): " + climb.isClimbLowPowerMode());
            telemetry.addLine();
            telemetry.addLine("Climb lockdown status: " + climb.isClimbLockedDown());
            telemetry.addLine("Tower Motor Pos: " + pinkArm.getTowerMotorPoseHat());
            telemetry.addLine();
            telemetry.addLine("Slide arm extension (ticks): " + pinkArm.pinkArmExtensionTicks);
            telemetry.addLine("Slide arm extension limit in effect?: " + pinkArm.isPinkArmExtensionLimitInEffect());
            telemetry.addLine("Slide arm extension limit reached?: " + pinkArm.pinkArmExtensionLimitShouldBeApplied());
            telemetry.addLine();

            //Push telemetry log to driver hub console
            telemetry.update();
        }
    }
}
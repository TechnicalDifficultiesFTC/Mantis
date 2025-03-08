package org.firstinspires.ftc.teamcode.Main.OpModes.AutoOps.AutosBlue;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;

@Autonomous(name="MANTIS AUTO: Strafe Left no enc RED/BLUE", group="Linear OpMode")
public class StrafeLeftNoEncAUTO_BLUE extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine(Config.dasshTag);

        MecanumDrivetrain mecanumDrivetrain = new MecanumDrivetrain(hardwareMap);
        mecanumDrivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Pose2d initialPose = new Pose2d(20,62,Math.toRadians(-90));
        HyperMecanumDrive hyperMecanumDrive = new HyperMecanumDrive(hardwareMap,initialPose);

        waitForStart();
        if (isStopRequested()) return;

        //Create and start the telemetry thread
        @SuppressLint("DefaultLocale") Thread telemetryThread = new Thread(() -> {
            while (!isStopRequested()) {
                double heading = hyperMecanumDrive.pose.heading.toDouble();
                double x = hyperMecanumDrive.pose.position.x;
                double y = hyperMecanumDrive.pose.position.y;
                telemetry.addLine("Telemetry is running...");
                telemetry.addLine("Estimated pose heading (radians): " +  heading);
                telemetry.addLine("Estimated pose position: " + "\n("
                        + Utils.roundAsString(x,3) + ","
                        + Utils.roundAsString(y,3) + ")");
                telemetry.update();
            }
        });

        telemetryThread.start();
        telemetry.setMsTransmissionInterval(5);

        double power = .35;

        mecanumDrivetrain.processInput(DcMotorSimple.Direction.REVERSE, DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.REVERSE,power);

        Thread.sleep(5000);

        power = 0;
        mecanumDrivetrain.processInput(DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.FORWARD,
                DcMotorSimple.Direction.FORWARD, DcMotorSimple.Direction.FORWARD,power);


    }

}


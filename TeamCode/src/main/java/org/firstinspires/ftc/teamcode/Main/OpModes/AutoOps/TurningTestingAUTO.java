package org.firstinspires.ftc.teamcode.Main.OpModes.AutoOps;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;

@Autonomous(name="MANTIS AUTO: Turning Debugger", group="Linear OpMode")
public class TurningTestingAUTO extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.setMsTransmissionInterval(10);
        telemetry.addLine("Initialized!");

        waitForStart();

        if (isStopRequested()) return;

        //The initial pose listed below looks like this: https://drive.google.com/file/d/1Utc5_G4_l_5cw0eufCwzscnooBlsDLXZ/view?usp=sharing
        Pose2d initialPose = Config.initialBluePose;
        HyperMecanumDrive hyperMecanumDrive = new HyperMecanumDrive(hardwareMap,initialPose);
        double turnAmountRadians = Math.toRadians(180);
        //Create and start the telemetry thread
        @SuppressLint("DefaultLocale") Thread telemetryThread = new Thread(() -> {
            while (!isStopRequested()) {
                double headingRadians = hyperMecanumDrive.pose.heading.toDouble();
                double x = hyperMecanumDrive.pose.position.x;
                double y = hyperMecanumDrive.pose.position.y;
                telemetry.addLine("Telemetry is running...");
                telemetry.addLine("Attempting to turn to (radians): " + turnAmountRadians);
                telemetry.addLine("Attempting to turn to (degrees): " + Math.toDegrees(turnAmountRadians));
                telemetry.addLine();
                telemetry.addLine("Estimated pose heading (radians): " +  headingRadians);
                telemetry.addLine("Estimated pose heading (degrees): " + Math.toDegrees(headingRadians));
                telemetry.addLine("Estimated pose position: " + "\n("
                        + Utils.roundAsString(x,3) + ","
                        + Utils.roundAsString(y,3) + ")");
                telemetry.update();
            }
        });

        telemetryThread.start();

        //PinkArm pinkArm = new PinkArm(hardwareMap,true);
        TrajectoryActionBuilder drivetrainTrajectory = hyperMecanumDrive.actionBuilder(initialPose)
                .turn(turnAmountRadians,new TurnConstraints(25.0,25.0,25.0))
                .waitSeconds(10);
        Action dtPath = drivetrainTrajectory.build();

        Actions.runBlocking(
                new SequentialAction(
                        dtPath
                )
        );

        //        // Stop the telemetry thread
        //        try {
        //            telemetryThread.join();
        //        } catch (InterruptedException e) {
        //            Thread.currentThread().interrupt();
        //        }

    }


}

package org.firstinspires.ftc.teamcode.Main.OpModes.AutoOps.AutosBlue;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;

@Autonomous(name="MANTIS AUTO: Strafe Left BLUE", group="Autonomous")
public class StrafeLeftAUTO_BLUE extends LinearOpMode {
    @Override
    public void runOpMode() {

        telemetry.addLine("Initialized!");

        waitForStart();

        if (isStopRequested()) return;

        //The initial pose listed below looks like this: https://drive.google.com/file/d/1Utc5_G4_l_5cw0eufCwzscnooBlsDLXZ/view?usp=sharing
        Pose2d initialPose = Config.initialBluePose;
        HyperMecanumDrive hyperMecanumDrive = new HyperMecanumDrive(hardwareMap,initialPose);

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

        //PinkArm pinkArm = new PinkArm(hardwareMap,true);
        TrajectoryActionBuilder drivetrainTrajectory = hyperMecanumDrive.actionBuilder(initialPose)
                .strafeTo(new Vector2d(60,60));
        Action dtPath = drivetrainTrajectory.build();

        Actions.runBlocking(
                new SequentialAction(
                        dtPath
                )
        );


    }


}

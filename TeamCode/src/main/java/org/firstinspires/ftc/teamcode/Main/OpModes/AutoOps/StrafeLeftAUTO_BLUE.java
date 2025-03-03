package org.firstinspires.ftc.teamcode.Main.OpModes.AutoOps;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;

@Autonomous(name="MANTIS AUTO: Strafe Left BLUE", group="Linear OpMode")
public class StrafeLeftAUTO_BLUE extends LinearOpMode {
    @Override
    public void runOpMode() {

        telemetry.addLine("Initialized!");

        waitForStart();

        if (isStopRequested()) return;

        //The initial pose listed below looks like this: https://drive.google.com/file/d/1Utc5_G4_l_5cw0eufCwzscnooBlsDLXZ/view?usp=sharing
        Pose2d initialPose = new Pose2d(20,62,Math.toRadians(-90));
        HyperMecanumDrive hyperMecanumDrive = new HyperMecanumDrive(hardwareMap,initialPose);

        // Create and start the telemetry thread
        Thread telemetryThread = new Thread(() -> {
            while (!isStopRequested()) {
                hyperMecanumDrive.updatePoseEstimate();
                double heading = hyperMecanumDrive.pose.heading.toDouble();
                double x = hyperMecanumDrive.pose.position.x;
                double y = hyperMecanumDrive.pose.position.y;
                telemetry.addLine("Receiving HyperMecanum data");
                telemetry.addLine("Estimated pose heading: " + heading);
                telemetry.addLine("Estimated pose position: " + x + "," + y);
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

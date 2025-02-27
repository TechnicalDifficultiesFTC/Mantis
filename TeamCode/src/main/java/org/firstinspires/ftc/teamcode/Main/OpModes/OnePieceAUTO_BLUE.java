package org.firstinspires.ftc.teamcode.Main.OpModes;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;

@Autonomous(name="MANTIS AUTO: One Piece BLUE", group="Linear OpMode")
public class OnePieceAUTO_BLUE extends LinearOpMode {
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
                double heading = hyperMecanumDrive.pose.heading.toDouble();
                double x = hyperMecanumDrive.pose.position.x;
                double y = hyperMecanumDrive.pose.position.y;
                telemetry.addLine("Telemetry is running...");
                telemetry.addLine("Estimated pose heading: " + heading);
                telemetry.addLine("Estimated pose position: " + x + "," + y);
                telemetry.update();
            }
        });

        telemetryThread.start();

        //PinkArm pinkArm = new PinkArm(hardwareMap,true);
        TrajectoryActionBuilder drivetrainTrajectory = hyperMecanumDrive.actionBuilder(initialPose)
                .setTangent(Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(56,56,Math.toRadians(45)), Math.toRadians(90))
                .waitSeconds(5)
                .splineToSplineHeading(new Pose2d(25,0,Math.toRadians(180)), Math.toRadians(90))
                .waitSeconds(2);
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

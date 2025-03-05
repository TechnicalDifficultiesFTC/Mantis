package org.firstinspires.ftc.teamcode.Main.OpModes.AutoOps;

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

@Autonomous(name="MANTIS AUTO: Three Piece BLUE", group="Linear OpMode")
public class ThreePieceAUTO_BLUE extends LinearOpMode {
    @Override
    public void runOpMode() {
        double actionsDelay = 2.5;
        telemetry.setMsTransmissionInterval(10);
        telemetry.addLine("Initialized!");

        waitForStart();

        if (isStopRequested()) return;

        HyperMecanumDrive hyperMecanumDrive = new HyperMecanumDrive(hardwareMap, Config.initialBluePose);

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
        TrajectoryActionBuilder drivetrainTrajectory = hyperMecanumDrive.actionBuilder(Config.initialBluePose)

                //Initial run to basket
                .splineToSplineHeading(new Pose2d(56,56,Math.toRadians(45)), Math.toRadians(90)) //To basket
                .waitSeconds(actionsDelay) //Dropping preloaded sample

                //Alignment to piece 1
                .strafeTo(new Vector2d(52,52))
                .turn(Math.toRadians(-135))
                .strafeTo(new Vector2d(48,45))
                .waitSeconds(actionsDelay) //Pick up piece infront of us

                //Return to basket
                .splineTo(new Vector2d(56,56),Math.toRadians(45))
                .waitSeconds(actionsDelay)

                //Alignment to piece 2
                .strafeTo(new Vector2d(52,52))
                .turn(Math.toRadians(-135))
                .strafeTo(new Vector2d(58,45))
                .waitSeconds(actionsDelay)

                //Return to basket
                .splineTo(new Vector2d(56,56),Math.toRadians(45))
                .waitSeconds(actionsDelay);

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

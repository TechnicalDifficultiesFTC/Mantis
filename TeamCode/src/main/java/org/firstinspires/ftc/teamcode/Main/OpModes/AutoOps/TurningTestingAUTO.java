package org.firstinspires.ftc.teamcode.Main.OpModes.AutoOps;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;

@Autonomous(name="MANTIS AUTO: Turning Debugger", group="Linear OpMode")
public class TurningTestingAUTO extends LinearOpMode {
    @Override
    public void runOpMode() {
        telemetry.setMsTransmissionInterval(10);
        telemetry.addLine("Initialized!");
        PinkArm pinkArm = new PinkArm(hardwareMap,true);
        waitForStart();

        if (isStopRequested()) return;

        //The initial pose listed below looks like this: https://drive.google.com/file/d/1Utc5_G4_l_5cw0eufCwzscnooBlsDLXZ/view?usp=sharing
        Pose2d initialPose = Config.initialBluePose;
        HyperMecanumDrive hyperMecanumDrive = new HyperMecanumDrive(hardwareMap,initialPose);

        double turnAmountRadians = Math.toRadians(270);

        //Create and start the telemetry thread
        @SuppressLint("DefaultLocale") Thread telemetryThread = new Thread(() -> {
            while (!isStopRequested()) {
                telemetry.addLine("Intake Power: " + pinkArm.intakeServo.getPower());


                telemetry.update();
            }
        });

        telemetryThread.start();
        double heading = Math.toRadians(270);

        TrajectoryActionBuilder drivetrainTrajectory = hyperMecanumDrive.actionBuilder(initialPose);
                //.turnTo(heading); //TODO: Research, this action "fails requirement??"
        //TODO: Mabye we are using the command made for tank? https://rr.brott.dev/docs/v1-0/actions/
        Action dtPath = drivetrainTrajectory.build();

        Action intake = new SequentialAction(
                pinkArm.intake(),
                new SleepAction(.5),
                pinkArm.stopIntake()
        );

        Actions.runBlocking(
              intake
        );

        //        // Stop the telemetry thread
        //        try {
        //            telemetryThread.join();
        //        } catch (InterruptedException e) {
        //            Thread.currentThread().interrupt();
        //        }

    }


}

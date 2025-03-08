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
        telemetry.setMsTransmissionInterval(1);
        telemetry.addLine("Initialized!");
        PinkArm pinkArm = new PinkArm(hardwareMap,true);
        waitForStart();

        if (isStopRequested()) return;

        //The initial pose listed below looks like this: https://drive.google.com/file/d/1Utc5_G4_l_5cw0eufCwzscnooBlsDLXZ/view?usp=sharing
        Pose2d initialPose = Config.initialBluePose;
        HyperMecanumDrive hyperMecanumDrive = new HyperMecanumDrive(hardwareMap,initialPose);

        //Create and start the telemetry thread
        @SuppressLint("DefaultLocale") Thread telemetryThread = new Thread(() -> {
            while (!isStopRequested()) {
                telemetry.addLine("Deviation = " + hyperMecanumDrive.deviation);
                telemetry.addLine("Linear Velocity = " + hyperMecanumDrive.linearVelocity);
                telemetry.addLine("Time = " + Utils.roundAsString(hyperMecanumDrive.time,1));
                telemetry.addLine("Time expected = " + Utils.roundAsString(hyperMecanumDrive.expectedTime,1));
                telemetry.update();
            }
        });

        telemetryThread.start();
        double heading = Math.toRadians(180);

        TrajectoryActionBuilder drivetrainTrajectory = hyperMecanumDrive.actionBuilder(initialPose)
                .turn(heading);

        Action dtPath = drivetrainTrajectory.build();

        Action intake = new SequentialAction(
                pinkArm.intake(),
                new SleepAction(.5),
                pinkArm.stopIntake()
        );

        Actions.runBlocking(
                new SequentialAction(
                        dtPath
                )
        );

    }


}

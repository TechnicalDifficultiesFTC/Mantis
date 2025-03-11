package org.firstinspires.ftc.teamcode.Main.OpModes.AutoOps;

import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;

@Autonomous(name="MANTIS AUTO: Turning Debugger", group="AutonomousAnonymous")
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
                telemetry.addLine("Action triggered: " + pinkArm.actionActive);
                telemetry.addLine("Arm target position: " + pinkArm.towerMotor.getTargetPosition());
                telemetry.update();
            }
        });

        telemetryThread.start();
        double heading = Math.toRadians(180);

        TrajectoryActionBuilder drivetrainTrajectory = hyperMecanumDrive.actionBuilder(initialPose)
                .turn(heading);

        Action dtPath = drivetrainTrajectory.build();

        Action shortIntakeSequence = new SequentialAction(
                pinkArm.intake(),
                new SleepAction(.5),
                pinkArm.stopIntake(),
                new SleepAction(.5)
        );

        Action escapeSequence = pinkArm.bringArmToEscapeAngle();

        /*
        SHOULD ONLY BE CALLED WHEN pinkArm is on the ground
         */
        Action progressiveGrabIntoHold = new SequentialAction(
                new ParallelAction(
                        pinkArm.extendPinkArm(),
                        pinkArm.intakeUntilPieceDetected()
                ),
                pinkArm.intakeToRetainPiece()
        );

        Actions.runBlocking(
                new SequentialAction(
                        escapeSequence
                )
        );

    }


}

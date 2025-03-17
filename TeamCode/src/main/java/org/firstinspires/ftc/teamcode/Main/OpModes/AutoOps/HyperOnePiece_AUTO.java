package org.firstinspires.ftc.teamcode.Main.OpModes.AutoOps;


import android.annotation.SuppressLint;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@Autonomous(name="MANTIS AUTO: ONE PIECE HYPERMEC", group="AutonomousAnonymous")
public class HyperOnePiece_AUTO extends LinearOpMode {
    private MecanumDrivetrain drivetrain;
    private PinkArm pinkArm;
    public static boolean opModeIsActive;


    public void forceForward(double pow, double actionTime) {
        drivetrain.frontLeftMotor.setPower(pow);
        drivetrain.backLeftMotor.setPower(pow);
        drivetrain.frontRightMotor.setPower(pow);
        drivetrain.backRightMotor.setPower(pow);
        Utils.halt((long) actionTime);
    }

    public void forceIntake() {
        pinkArm.intakeServo.setPower(Config.SERVO_INTAKE_POWER);
    }

    public void forceOuttake() {
        pinkArm.intakeServo.setPower(Config.SERVO_OUTTAKE_POWER);
    }


    @Override
    public void runOpMode() {
        pinkArm = new PinkArm(hardwareMap,true);
        drivetrain = new MecanumDrivetrain(hardwareMap);

        telemetry.setMsTransmissionInterval(1);

        waitForStart();
        if (isStopRequested()) return;

        //Create and start the updater thread
        @SuppressLint("DefaultLocale") Thread opModeIsActiveUpdater = new Thread(() -> {
            while (!isStopRequested()) {
                opModeIsActive = opModeIsActive();
                telemetry.addLine("TowerMotor to: " + pinkArm.towerMotor.getTargetPosition());
                telemetry.addLine("SlideMotor to: " + pinkArm.slideMotor.getTargetPosition());
                telemetry.addLine();
                telemetry.addLine("towerMotor pos: " + pinkArm.towerMotor.getCurrentPosition());
                telemetry.addLine();
                telemetry.addLine("slideMotor pos: " + pinkArm.slideMotor.getCurrentPosition());
                telemetry.addLine("slideMotor power: " + pinkArm.slideMotor.getPower());
                telemetry.addLine();
                telemetry.addLine("Intake Power: " + pinkArm.intakeServo.getPower());
                //telemetry.addLine("Intake Power: " + pinkArm.intakeServo.getPower());
                telemetry.update();
            }
        });

        opModeIsActiveUpdater.start();

        forceIntake();

        drivetrain.strafeRight(.5, 250);
        drivetrain.halt(500);

        forceForward(.25,2350);
        drivetrain.halt(500);

        drivetrain.strafeRight(.5,625);
        drivetrain.turnWithTimeAndPower(.5,400);
        drivetrain.goForward(.5, 500);
        drivetrain.halt(500);

        drivetrain.strafeRight(.25,450);
        drivetrain.halt(500);

        drivetrain.goBackwards(.25,350);
        drivetrain.halt(500);

        Actions.runBlocking(
                new SequentialAction(
                            pinkArm.bringArmToEscapeAngle(),
                            pinkArm.extendSlightlyPastBoundary(),
                            pinkArm.raiseArmToHighBasket(),
                            pinkArm.reachTowardsHighBasket()
                        )
        );

        drivetrain.goForward(.25,250);
        drivetrain.halt(500);

        forceOuttake();
        Utils.halt(2500);


    }
}

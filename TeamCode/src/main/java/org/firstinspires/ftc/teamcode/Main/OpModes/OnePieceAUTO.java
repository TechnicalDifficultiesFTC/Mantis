package org.firstinspires.ftc.teamcode.Main.OpModes;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;

@Autonomous(name="MANTIS AUTO: One Piece", group="Linear OpMode")
public class OnePieceAUTO extends LinearOpMode {
    @Override
    public void runOpMode() {
        Pose2d initialPose = new Pose2d(20,62,Math.toRadians(-90));
        HyperMecanumDrive hyperMecanumDrive = new HyperMecanumDrive(hardwareMap,initialPose);

        PinkArm pinkArm = new PinkArm(hardwareMap);

        TrajectoryActionBuilder drivetrainTrajectory = hyperMecanumDrive.actionBuilder(initialPose)
                .setTangent(Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(56,56,Math.toRadians(45)), Math.toRadians(90))
                .waitSeconds(5)
                .splineToSplineHeading(new Pose2d(25,0,Math.toRadians(180)), Math.toRadians(90))
                .waitSeconds(2);
    }
}

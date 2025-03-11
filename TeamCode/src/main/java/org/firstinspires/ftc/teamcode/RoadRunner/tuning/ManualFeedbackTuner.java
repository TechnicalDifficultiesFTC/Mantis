package org.firstinspires.ftc.teamcode.RoadRunner.tuning;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.TankDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.TankDrive;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.TwoDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.RoadRunner.RR1.TwoDeadWheelLocalizer;

public final class ManualFeedbackTuner extends LinearOpMode {
    public static double DISTANCE = 48;

    @Override
    public void runOpMode() throws InterruptedException {
        if (org.firstinspires.ftc.teamcode.RoadRunner.tuning.TuningOpModes.DRIVE_CLASS.equals(HyperMecanumDrive.class)) {
            HyperMecanumDrive drive = new HyperMecanumDrive(hardwareMap, new Pose2d(0, 0, 0));
            
            if (drive.localizer instanceof TwoDeadWheelLocalizer) {
                if (TwoDeadWheelLocalizer.PARAMS.perpXTicks == 0 && TwoDeadWheelLocalizer.PARAMS.parYTicks == 0) {
                    throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
                }
            } else if (drive.localizer instanceof ThreeDeadWheelLocalizer) {
                if (ThreeDeadWheelLocalizer.PARAMS.perpXTicks == 0 && ThreeDeadWheelLocalizer.PARAMS.par0YTicks == 0 && ThreeDeadWheelLocalizer.PARAMS.par1YTicks == 1) {
                    throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
                }
            }
            waitForStart();

            while (opModeIsActive()) {
                Actions.runBlocking(
                    drive.actionBuilder(new Pose2d(0, 0, 0))
                            .lineToX(DISTANCE)
                            .lineToX(0)
                            .build());
            }
        } else if (org.firstinspires.ftc.teamcode.RoadRunner.tuning.TuningOpModes.DRIVE_CLASS.equals(TankDrive.class)) {
            TankDrive drive = new TankDrive(hardwareMap, new Pose2d(0, 0, 0));

            if (drive.localizer instanceof TwoDeadWheelLocalizer) {
                if (TwoDeadWheelLocalizer.PARAMS.perpXTicks == 0 && TwoDeadWheelLocalizer.PARAMS.parYTicks == 0) {
                    throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
                }
            } else if (drive.localizer instanceof ThreeDeadWheelLocalizer) {
                if (ThreeDeadWheelLocalizer.PARAMS.perpXTicks == 0 && ThreeDeadWheelLocalizer.PARAMS.par0YTicks == 0 && ThreeDeadWheelLocalizer.PARAMS.par1YTicks == 1) {
                    throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
                }
            }
            waitForStart();

            while (opModeIsActive()) {
                Actions.runBlocking(
                    drive.actionBuilder(new Pose2d(0, 0, 0))
                            .lineToX(DISTANCE)
                            .lineToX(0)
                            .build());
            }
        } else {
            throw new RuntimeException();
        }
    }
}

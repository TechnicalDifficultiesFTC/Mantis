package com.example.meepmeeptesting.Autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Hypothetical_OnePieceAUTO_BLUE {
    public static void executeAuto() {
        double actionsDelay = 2.5;
        MeepMeep meepMeepApplicationEngine = new MeepMeep(800);
        meepMeepApplicationEngine.setShowFPS(true);
        Pose2d initialPose = new Pose2d(20, 62, Math.toRadians(-90));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeepApplicationEngine)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(
                        drive -> drive.trajectorySequenceBuilder(initialPose)
                                //To basket
                                .setTangent(Math.toRadians(45))
                                .splineToSplineHeading(new Pose2d(58,52,Math.toRadians(45)), Math.toRadians(90))
                                //To climb
                                .turn(Math.toRadians(225))
                                .strafeTo(new Vector2d(0,31))
                                .build()
                );

        Thread telemetryThread = new Thread(() -> {
            while (true) {
                Pose2d botPose = myBot.getDrive().getPoseEstimate();
                double heading = botPose.getHeading();
                //double heading = Math.toDegrees(botPose.getHeading());
                double x = botPose.getX();
                double y = botPose.getY();
                System.out.println("\nEstimated pose heading (radians): " + heading);
                System.out.println("Estimated pose position: " + x + "," + y);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        });

        telemetryThread.start();

        myBot.setDimensions(17.4,17);
        meepMeepApplicationEngine.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
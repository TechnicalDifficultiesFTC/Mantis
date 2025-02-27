package com.example.meepmeeptesting.Autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Hypothetical_OnePieceAUTO {
    public static void executeAuto() {
        MeepMeep meepMeepApplicationEngine = new MeepMeep(400);
        meepMeepApplicationEngine.setShowFPS(true);
        Pose2d initialPose = new Pose2d(20, 62, Math.toRadians(-90));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeepApplicationEngine)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(
                        drive -> drive.trajectorySequenceBuilder(initialPose)
                        .setTangent(Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(56,56,Math.toRadians(45)), Math.toRadians(90)) //To basket
                        .waitSeconds(5)
                        .back(5)
                        .splineToSplineHeading(new Pose2d(25,0,Math.toRadians(180)), Math.toRadians(90)) //To submersible
                        .waitSeconds(2)
                        .build()
                );

        Thread telemetryThread = new Thread(() -> {
            while (true) {
                Pose2d botPose = myBot.getDrive().getPoseEstimate();
                double heading = Math.toDegrees(botPose.getHeading());
                double x = botPose.getX();
                double y = botPose.getY();
                System.out.println("\nEstimated pose heading: " + heading);
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

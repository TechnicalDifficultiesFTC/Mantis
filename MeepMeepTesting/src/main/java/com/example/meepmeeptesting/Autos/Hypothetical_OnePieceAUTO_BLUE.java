package com.example.meepmeeptesting.Autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Hypothetical_OnePieceAUTO_BLUE {
    static final double actionsDelay = 5;
    public static void executeAuto() {
        MeepMeep meepMeepApplicationEngine = new MeepMeep(800);
        meepMeepApplicationEngine.setShowFPS(true);
        Pose2d initialPose = new Pose2d(20, 62, Math.toRadians(-90));
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeepApplicationEngine)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(
                        drive -> drive.trajectorySequenceBuilder(initialPose)
                                //.setTangent(Math.toRadians(0))
                                .splineToSplineHeading(new Pose2d(56,56,Math.toRadians(45)), Math.toRadians(90)) //To basket
                                .waitSeconds(actionsDelay) //Dropping preloaded sample

                                //Alignment to piece 1
                                .back(5)
                                .turn(Math.toRadians(-135))
                                .lineTo(new Vector2d(48,45))
                                .waitSeconds(actionsDelay) //Pick up piece infront of us

                                //Return to basket
                                .splineToSplineHeading(new Pose2d(56,56,Math.toRadians(45)), Math.toRadians(90))

                                //Alignment to piece 2
                                .back(5)
                                .turn(Math.toRadians(-135))
                                .lineTo(new Vector2d(58,45))
                                .waitSeconds(actionsDelay)

                                //Return to basket
                                .splineToSplineHeading(new Pose2d(56,56,Math.toRadians(45)), Math.toRadians(90))
                                .waitSeconds(actionsDelay)
                                //Build
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
                } catch (InterruptedException ignored) {}
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

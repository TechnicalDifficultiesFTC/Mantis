package com.example.meepmeeptesting.Autos;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Auto1 {
    public static void executeAuto() {
        MeepMeep meepMeep = new MeepMeep(800);
        meepMeep.setShowFPS(true);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)

                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(20, 62, Math.toRadians(-90)))
                        .setTangent(Math.toRadians(0))
                        .splineToSplineHeading(new Pose2d(56,56,Math.toRadians(45)), Math.toRadians(90)) //To basket
                        .waitSeconds(5)
                        .back(5)
                        .splineToSplineHeading(new Pose2d(25,0,Math.toRadians(180)), Math.toRadians(90)) //To submersible
                        .waitSeconds(2)
                        //.lineToX()
                        .build());

        myBot.setDimensions(17.4,17);
        meepMeep.setBackground(MeepMeep.Background.FIELD_INTOTHEDEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}

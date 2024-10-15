package org.firstinspires.ftc.teamcode;

import java.util.Random;

public class beaUtils {
    //Utils
    public static void pushDrivetrainPower(double FLM, double BLM, double FRM, double BRM) {
        //Update motor objects with power variables
        MecanumDrive.frontLeftMotor.setPower(FLM);
        MecanumDrive.backLeftMotor.setPower(BLM);
        MecanumDrive.frontRightMotor.setPower(FRM);
        MecanumDrive.backRightMotor.setPower(BRM);
    }

    public static String generateVoiceLine() {
        //Just for fun! Display a "voice line" on initialization
        Random rand = new Random();
        String[] voiceLines = {
                "Your skills are impressive, Pilot. It is good to have you return.",
                "“If brute force isn’t working, you aren’t using enough of it”",
                "Scorch AI Transferring Control to Pilot.",
                "Welcome back, sharpshooter pilot",
                "“The sword is yours”",
                "“Pilot, you are outnumbered by hostile titans, focus your fire on the weakest target.”",
                "Welcome back, cockpit cooling reactivated.",
                "I'm trying to figure out how to move the axle!!",
                "Tanky motor",
                ":3c",
                "No technical difficulties detected!",
                "You got the sun on the moon"
        };
        return voiceLines[rand.nextInt(voiceLines.length)]; //Grabs from a random position in the list
    }

    public static boolean triggerBoolean(double triggerValue) {
        //Compares the float value to threshold
        final double threshold = .1;
        return (triggerValue > threshold); //Evaluates this statement, then returns true if trigger has passed the threshold, false if it hasn't
    }

}

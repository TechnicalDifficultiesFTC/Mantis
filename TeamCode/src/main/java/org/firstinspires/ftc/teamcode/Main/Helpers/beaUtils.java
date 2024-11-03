package org.firstinspires.ftc.teamcode.Main.Helpers;

import java.util.Random;

public class beaUtils {

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
                "Ad Astra Per Aspera",
                ":3c",
                "No Technical Difficulties detected!",
                "You got the sun on the moon",
        };
        return voiceLines[rand.nextInt(voiceLines.length)]; //Grabs from a random position in the list
    }

    public static double getEncoderRevolutions(double encoderTicks, double PPR) {
        final double CPR = PPR * 4;
        return encoderTicks/CPR; //Motor revolutions
    }

    public static double pinkArmEncoderToDegreeConversion(int encoderPosition,double PPR) {
        //Gets motor revolutions
        double currentMotorRevolutions = getEncoderRevolutions(encoderPosition, PPR);
        //Account for 3:1 gear ratio
        double pinkArmRevolutions = currentMotorRevolutions / 3;
        //Turn into revs
        double pinkArmAngle = pinkArmRevolutions * 360;
        //Normalize angle
        return pinkArmAngle % 360;
    }
    public static boolean triggerBoolean(double triggerValue) {
        //Compares the float value to threshold
        final double threshold = .1;
        return (triggerValue > threshold);
    }

}

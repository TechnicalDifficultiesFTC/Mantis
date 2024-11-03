package org.firstinspires.ftc.teamcode.Main.Helpers;

import org.firstinspires.ftc.teamcode.Main.Constants;

import java.util.Random;

public class beaUtils {

    /**
     * Generates a random "voice line" to display on the telemetry feed, just for fun, servos no real purpose
     * @return [String] Semi random cool sounding piece of text
     */
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

    /**
     * Calculates an encoders current revolutions
     * @param encoderTicks Distance an encoder has already traveled (in ticks) can be found with motor.getCurrentPosition()
     * @param PPR Pulses per revolution (find in Constants.java)
     * @return [double] Motor Revolutions
     */
    public static double getEncoderRevolutions(double encoderTicks, double PPR) {
        final double CPR = PPR * 4;
        return encoderTicks/CPR; //Motor revolutions
    }

    /**
     * (Indev)
     * @param encoderPosition Distance an encoder has already traveled (in ticks) can be found with motor.getCurrentPosition()
     * @param PPR Pulses per revolution (find in Constants.java)
     * @return [double] Motor degree in degrees
     */
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

    /**
     * Returns a boolean depending on the amount of depth on a trigger press, used to do an action with the trigger that only requires t/f
     * @param triggerValue Can be found at gamepad.left_trigger() or gamepad.right_trigger()
     * @return [boolean] If trigger has passed threshold
     */
    public static boolean triggerBoolean(double triggerValue) {
        //Compares the float value to threshold
        return (triggerValue > Constants.TRIGGER_THRESHOLD);
    }

}

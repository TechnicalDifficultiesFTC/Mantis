package org.firstinspires.ftc.teamcode.Main.Helpers;

import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;

import java.util.Random;

public class Utils {

    /**
     * Generates a random "voice line" to display on the telemetry feed, just for fun, servos no real purpose
     * @return [String] Semi random cool sounding piece of text
     */
    public static String generateMOTMLine() {
        //Just for fun! Display a "voice line" on initialization
        Random rand = new Random();
        String[] voiceLines = {
                "Your skills are impressive, Pilot. It is good to have you return.",
                "“If brute force isn’t working, you aren’t using enough of it”",
                "Welcome back, sharpshooter pilot",
                "“The sword is yours”",
                "Ad Astra Per Aspera",
                ":3c",
                "No Technical Difficulties detected!",
                "You got the sun on the moon",
                "Alea iacta est: The die is cast.",
                "Am I not merciful?"
        };
        return voiceLines[rand.nextInt(voiceLines.length)]; //Grabs from a random position in the list
    }

    /**
     * Returns a boolean depending on the amount of depth on a trigger press, used to do an action with the trigger that only requires t/f
     * @param triggerValue Can be found at gamepad.left_trigger() or gamepad.right_trigger()
     * @return [boolean] If trigger has passed threshold
     */
    public static boolean triggerBoolean(double triggerValue) {
        //Compares the float value to threshold
        return (triggerValue > Config.TRIGGER_THRESHOLD);
    }
}


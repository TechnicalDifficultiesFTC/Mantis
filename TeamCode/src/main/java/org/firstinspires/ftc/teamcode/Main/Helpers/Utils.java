package org.firstinspires.ftc.teamcode.Main.Helpers;

import org.firstinspires.ftc.teamcode.RoadRunner.RR1.HyperMecanumDrive;

import java.util.Random;

public class Utils {

    /**
     * Rounds the input to the amount of decimal places provided as the (int) second parameter
     * @param input Double to be rounded
     * @param decimalPlacesToRoundTo Decimal places to round to (EX: 4.2456 rounded to 2 would be 4.24)
     * @return Formatted string of double rounded
     */
    public static String roundAsString(double input, int decimalPlacesToRoundTo) {
        return String.format("%."+decimalPlacesToRoundTo+"f", input);
    }
    /**
     * Generates a random "MOTM" to display on the telemetry feed, just for fun, servos no real purpose
     * @return [String]
     */
    public static String generateMOTMLine() {
        //Just for fun! Display a "voice line" on initialization
        Random rand = new Random();
        String[] voiceLines = {
                "Your skills are impressive, Pilot. It is good to have you return.",
                "“If brute force isn’t working, you aren’t using enough of it”",
                "Welcome back, sharpshooter pilot",
                "“The sword is yours”",
                "No Technical Difficulties detected!",
                "You got the sun on the moon.",
                "Alea iacta est: The die is cast.",
                "Am I not merciful?",
                "Maybe I was wrong to consider us allies."
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


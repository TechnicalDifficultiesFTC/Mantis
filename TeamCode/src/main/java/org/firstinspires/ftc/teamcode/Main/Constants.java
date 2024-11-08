package org.firstinspires.ftc.teamcode.Main;

/**
 * Holds static values
 */
public class Constants {

    //Used in beaUtils.triggerBoolean() to determine the amount a trigger needs to be pressed to return true
    public static final double TRIGGER_THRESHOLD = .1;

    //Used for software under clock
    public static final double MIN_SPEED = 0.33;
    public static final double MAX_SPEED = 1;

    //Necessary encoder information
    public static final double TOWER_MOTOR_PPR = 5281.1;
    public static final double SLIDE_MOTOR_PPR = 587.7;

    //Tag funzies :)
    public static final String dasshTag =
            "/======================================\\\\\n" +
            "||  ____                  _       ___  _  ||\n" +
            "|| |  _ \\  __ _ ___ ___| |__   / _ \\/ | ||\n" +
            "|| | | | |/ _` / __/ __| '_ \\| | | | | ||\n" +
            "|| | |_| | (_| \\__ \\__ \\ | | | |_| | | ||\n" +
            "|| |____/ \\__,_|___/___/_| |_| \\___/|_| ||\n" +
            "\\======================================/";
}

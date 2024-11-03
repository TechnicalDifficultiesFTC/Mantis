package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

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
                "Ad Astra Per Aspera",
                ":3c",
                "No Technical Difficulties detected!",
                "You got the sun on the moon",
        };
        return voiceLines[rand.nextInt(voiceLines.length)]; //Grabs from a random position in the list
    }

    public static double scoopEncoderRevolutions(double encoderTicks, double PPR) {
        final double CPR = PPR * 4;
        return encoderTicks/CPR; //Motor revolutions
    }

    public static double scoopEncoderDegree(MotorInformation motorInformation) {
        double encoderRevolutions = scoopEncoderRevolutions(motorInformation.getMotor().getCurrentPosition(),motorInformation.getPPR());
        double angle = encoderRevolutions * 360;
        //Lowkey got this off of gm0, review this logic b4 deploying
        return angle % 360; //Normalize angle
    }

    public static double pinkArmEncoderToDegreeConversion(MotorInformation motorInformation) {
        //Gets motor revolutions
        double currentMotorRevolutions = scoopEncoderRevolutions(motorInformation.getMotor().getCurrentPosition() , motorInformation.getPPR());
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

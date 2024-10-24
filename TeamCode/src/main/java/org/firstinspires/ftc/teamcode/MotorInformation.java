package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MotorInformation {
    private final DcMotor motor;
    private final double PPR;
    public MotorInformation(DcMotor motor, double PPR) {
        this.motor = motor;
        this.PPR = PPR;
    }

    public double getPPR() {
        return PPR;
    }

    public double getMotorEncoderPosition(DcMotor motor) {
        return motor.getCurrentPosition();
    }

    public DcMotor getMotor() {
        return motor;
    }
}

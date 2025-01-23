package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Helpers.Constants;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;

@Autonomous(name="MANTIS AUTO V:A1 EMERGENCY", group="Linear OpMode")
public class AutoRobotContainer extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        final String MOTM = Utils.generateMOTMLine();

        telemetry.addLine(Constants.dasshTag);
        //Grab devices

        //Drivetrain
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get(DeviceRegistry.FRONT_LEFT_MOTOR.str());
        DcMotor backLeftMotor = hardwareMap.dcMotor.get(DeviceRegistry.BACK_LEFT_MOTOR.str());
        DcMotor frontRightMotor = hardwareMap.dcMotor.get(DeviceRegistry.FRONT_RIGHT_MOTOR.str());
        DcMotor backRightMotor = hardwareMap.dcMotor.get(DeviceRegistry.BACK_RIGHT_MOTOR.str());

        MecanumDrivetrain mecanumDrivetrain = new MecanumDrivetrain(frontLeftMotor,backLeftMotor,frontRightMotor,backRightMotor);
        mecanumDrivetrain.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.update();
        waitForStart();
        telemetry.clear();
        while (opModeIsActive()) {
            mecanumDrivetrain.strafeLeft(.5);
            telemetry.addLine("hii ran mecanumdt strafeleft ran");
            telemetry.update();
        }
    }

}


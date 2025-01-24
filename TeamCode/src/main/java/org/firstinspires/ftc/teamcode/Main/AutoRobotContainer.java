package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Helpers.Constants;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Helpers.Utils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;

@Autonomous(name="MANTIS AUTO V:A2 Strafe Left", group="Linear OpMode")
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

        waitForStart();
        telemetry.clear();

        while (opModeIsActive()) {
            mecanumDrivetrain.strafeLeft(Constants.AUTO_DRIVE_SPEED);
            telemetry.addLine("hii ran mecanum dt strafeleft ran at speed: " + Constants.AUTO_DRIVE_SPEED);
            telemetry.update();
        }
    }

}


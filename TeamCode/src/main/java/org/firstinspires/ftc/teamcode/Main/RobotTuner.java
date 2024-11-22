package org.firstinspires.ftc.teamcode.Main;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Helpers.beaUtils;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="Robot Tuning", group="Linear OpMode")
public class RobotTuner extends LinearOpMode {

    //Initialize global variables and local functions
    //By global variables I mean ONLY variables that are gonna get accessed by beaUtils/other classes, not finals and stuff

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("NOTE: ONLY PROCESSING GAMEPAD 2 INPUT");
        //Grab devices

        //Pink Arm
        CRServo intakeServo = hardwareMap.crservo.get(DeviceRegistry.INTAKE_SERVO.str());
        DcMotor towerMotor = hardwareMap.dcMotor.get(DeviceRegistry.TOWER_MOTOR.str());
        DcMotor slideMotor = hardwareMap.dcMotor.get(DeviceRegistry.SLIDE_MOTOR.str());

        PinkArm pinkArm = new PinkArm(towerMotor,slideMotor,intakeServo);

        //PinkArm initial setup
        pinkArm.zeroEncoders(); //WILL KILL MOTORS
        pinkArm.restartMotors(); //Fingers crossed will restart motors

        telemetry.update();

        //Nothing past this point will run until the start button is pressed
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) { //Primary loop

            pinkArm.processInput(gamepad2);

            //Telemetry
            telemetry.addData("Overview: ","Online");

            telemetry.addLine("Slide Motor Power: "+ slideMotor.getPower());
            telemetry.addLine("Tower Motor Power: "+ towerMotor.getPower());
            telemetry.addLine();
            telemetry.addLine("ASSUMED TOWER DEGREES: "+ pinkArm.getTowerDegree()*4.5);
            telemetry.addLine("ASSUMED SLIDE REVOLUTIONS OUT: " + pinkArm.getSlideMotorRevolutions());
            telemetry.update();
        }
    }
}
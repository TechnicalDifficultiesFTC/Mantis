package org.firstinspires.ftc.teamcode.Main.OpModes;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Main.Helpers.Debounce;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="ARM_DEBUGGER", group="Linear OpMode")
public class ArmTesting extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        //Pink Arm init
        CRServo intakeServo = hardwareMap.crservo.get(DeviceRegistry.INTAKE_SERVO.str());
        intakeServo.setDirection(DcMotorSimple.Direction.REVERSE);

        DcMotor towerMotor = hardwareMap.dcMotor.get(DeviceRegistry.TOWER_MOTOR.str());
        DcMotor slideMotor = hardwareMap.dcMotor.get(DeviceRegistry.SLIDE_MOTOR.str());

        Debounce leftButtonDebounce = new Debounce();
        Debounce rightButtonDebounce = new Debounce();

        boolean increasePower = false;

        PinkArm pinkArm = new PinkArm(towerMotor,slideMotor,intakeServo);

        towerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        towerMotor.setTargetPosition(20);
        towerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.addLine("Systems Registered");
        telemetry.update();

        waitForStart();
        telemetry.clear();

        final Encoder towerEncoder;

        //Create overflow encoder information display out of the towerEncoder
        towerEncoder = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, DeviceRegistry.TOWER_MOTOR.str())));

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            //TODO: Implement pinkarm FFL stabilization

            int towerEncoderVelocity = towerEncoder.getPositionAndVelocity().velocity;
            int towerEncoderPosition = towerEncoder.getPositionAndVelocity().position;
            int towerPosIncreasing = pinkArm.towerPosIncreasing;

            telemetry.addLine("Overview: Online");
            telemetry.addLine();
            pinkArm.processInput(gamepad2, DcMotor.RunMode.RUN_TO_POSITION); //Contains telemetry data
            telemetry.addLine("Tower OVERFLOW Encoder status: " +
                    "\nTower Overflow Encoder Velocity: " + towerEncoderVelocity +
                    "\nTower Overflow Encoder Position: " + towerEncoderPosition );
            telemetry.addLine();

            if (towerPosIncreasing == 1) {
                telemetry.addLine("Tower Motor Pos Incremented " +
                        "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                        "\nTower Motor pos (desired) = " + towerMotor.getTargetPosition() +
                        "\nTower Motor pos (actual) = " + towerMotor.getCurrentPosition());
            }
            else if (towerPosIncreasing == 2) {
                telemetry.addLine("Tower Motor Pos Decremented " +
                        "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                        "\nTower Motor pos (desired) = " + towerMotor.getTargetPosition() +
                        "\nTower Motor pos (actual) = " + towerMotor.getCurrentPosition());
            }
            else if (towerPosIncreasing == -1) {
                telemetry.addLine("Tower Motor Pos Neutral " +
                        "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                        "\nTower Motor pos (desired) = " + towerMotor.getTargetPosition() +
                        "\nTower Motor pos (actual) = " + towerMotor.getCurrentPosition());
            }

            if (leftButtonDebounce.isPressed(gamepad2.dpad_up)) {
                increasePower = true;
            }
            else if (rightButtonDebounce.isPressed(gamepad2.dpad_down)) {
                increasePower = false;
            }

            if (increasePower) {
                towerMotor.setPower(1);
            }
            else {
                towerMotor.setPower(0);
            }

            telemetry.update();

        }
    }
}

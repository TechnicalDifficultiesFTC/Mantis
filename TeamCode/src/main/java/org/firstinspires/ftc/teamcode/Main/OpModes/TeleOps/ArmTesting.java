package org.firstinspires.ftc.teamcode.Main.OpModes.TeleOps;

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
import org.firstinspires.ftc.teamcode.Main.Helpers.TowerPosMovementStatus;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="ARM_DEBUGGER", group="Linear OpMode")
public class ArmTesting extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        //TODO: Make sure that in arm testing we are reinitializing encoders
        //Pink Arm init

//        Debounce leftButtonDebounce = new Debounce();
//        Debounce rightButtonDebounce = new Debounce();

        boolean increasePower = false;

        PinkArm pinkArm = new PinkArm(hardwareMap,true);

        telemetry.addLine("Systems Registered");
        telemetry.update();

        waitForStart();
        telemetry.clear();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            //TODO: Implement pinkarm FFL stabilization

            TowerPosMovementStatus towerPosMovementStatus = pinkArm.towerPosMovementStatus;

            telemetry.addLine("Overview: Online");
            telemetry.addLine();

            pinkArm.processInput(gamepad2); //Contains telemetry data

            //Display encoders position and velocity of pinkarm
            telemetry.addLine(pinkArm.getEncodersStatusToString());

            telemetry.addLine();

            if (towerPosMovementStatus == TowerPosMovementStatus.MOVING_UP) {
                telemetry.addLine("Tower Motor Pos Incremented " +
                        "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                        "\nTower Motor pos (desired) = " + pinkArm.towerMotor.getTargetPosition() +
                        "\nTower Motor pos (actual) = " + pinkArm.towerMotor.getCurrentPosition());
            }
            else if (towerPosMovementStatus == TowerPosMovementStatus.MOVING_DOWN) {
                telemetry.addLine("Tower Motor Pos Decremented " +
                        "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                        "\nTower Motor pos (desired) = " + pinkArm.towerMotor.getTargetPosition() +
                        "\nTower Motor pos (actual) = " + pinkArm.towerMotor.getCurrentPosition());
            }
            else if (towerPosMovementStatus == TowerPosMovementStatus.NOT_MOVING) {
                telemetry.addLine("Tower Motor Pos Neutral " +
                        "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                        "\nTower Motor pos (desired) = " + pinkArm.towerMotor.getTargetPosition() +
                        "\nTower Motor pos (actual) = " + pinkArm.towerMotor.getCurrentPosition());
            }

            telemetry.update();

        }
    }
}

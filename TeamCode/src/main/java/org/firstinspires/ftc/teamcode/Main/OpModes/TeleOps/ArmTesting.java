package org.firstinspires.ftc.teamcode.Main.OpModes.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Main.Helpers.TowerPosMovementStatus;
import org.firstinspires.ftc.teamcode.Main.Subsystems.Climb;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="ARM_DEBUGGER", group="Linear OpMode")
public class ArmTesting extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        //TODO: Make sure that in arm testing we are reinitializing encoders
        //Pink Arm init

//        Debounce leftButtonDebounce = new Debounce();
//        Debounce rightButtonDebounce = new Debounce();

        PinkArm pinkArm = new PinkArm(hardwareMap,false);
        Climb climb = new Climb(hardwareMap, false);

        telemetry.addLine("Systems Registered");
        telemetry.update();
        telemetry.setMsTransmissionInterval(5);
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
            telemetry.addLine(pinkArm.toString());
            telemetry.addLine();
            telemetry.addLine(climb.toString());

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

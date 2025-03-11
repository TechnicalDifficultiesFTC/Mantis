package org.firstinspires.ftc.teamcode.Main.OpModes.TeleOps;

import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Main.Helpers.Config;
import org.firstinspires.ftc.teamcode.Main.Helpers.DeviceRegistry;
import org.firstinspires.ftc.teamcode.Main.Helpers.TowerPosMovementStatus;
import org.firstinspires.ftc.teamcode.Main.Subsystems.Climb;
import org.firstinspires.ftc.teamcode.Main.Subsystems.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="Subsystems Testing", group="Secondary OpMode")
public class SubsystemsTesting extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        //TODO: Make sure that in arm testing we are reinitializing encoders
        //Pink Arm init

        PinkArm pinkArm = new PinkArm(hardwareMap,false);
        Climb climb = new Climb(hardwareMap, false);
        MecanumDrivetrain drivetrain = new MecanumDrivetrain(hardwareMap);

        telemetry.addLine("Systems Registered");
        telemetry.update();
        telemetry.setMsTransmissionInterval(5);
        waitForStart();
        telemetry.clear();

        OverflowEncoder par0 = new OverflowEncoder(new RawEncoder(
                (DcMotorEx) hardwareMap.dcMotor.get(DeviceRegistry.LEFT_THROUGHBORE_ENC.str())));
        OverflowEncoder par1 = new OverflowEncoder(new RawEncoder(
                (DcMotorEx) hardwareMap.dcMotor.get(DeviceRegistry.RIGHT_THROUGHBORE_ENC.str())));
        OverflowEncoder perp = new OverflowEncoder(new RawEncoder(
                (DcMotorEx) hardwareMap.dcMotor.get(DeviceRegistry.YAW_THROUGHBORE_ENC.str())));
        par0.setDirection(DcMotorSimple.Direction.FORWARD);
        par1.setDirection(DcMotorSimple.Direction.FORWARD);
        perp.setDirection(DcMotorSimple.Direction.FORWARD);
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            //TODO: Implement pinkarm FFL stabilization

            TowerPosMovementStatus towerPosMovementStatus = pinkArm.towerPosMovementStatus;

            telemetry.addLine("Overview: Online");
            telemetry.addLine();

            pinkArm.processInput(gamepad2); //Contains telemetry data

            //Display encoders position and velocity of pinkarm
            telemetry.addLine(pinkArm.armStatusAsString());
            telemetry.addLine();
            telemetry.addLine(climb.climbStatusAsString());
            telemetry.addLine();
            telemetry.addLine(pinkArm.getVisionStatusAsString());

            if (Config.SHOW_ARM_STATUS) {
                telemetry.addLine();
                telemetry.addLine("Slide Motor Power: " + pinkArm.slideMotor.getPower());
                telemetry.addLine("Tower Motor Power: " + pinkArm.towerMotor.getPower());
                telemetry.addLine();
                telemetry.addLine("Climb positional status: \n" + climb.getClimbArmsPositionAsString());
                telemetry.addLine("HYPOTHETICAL tower pos: " + pinkArm.getTowerMotorPoseHat());
                telemetry.addLine("ACTUAL tower pos: " + pinkArm.towerMotor.getCurrentPosition());
                telemetry.addLine();
            }
            if (Config.SHOW_DT_STATUS) {
                telemetry.addLine();
                telemetry.addLine("Drivetrain Status: \n" + drivetrain.getMotorsStatusAsString());
                telemetry.addLine();
            }
            if (Config.SHOW_ENCODER_DATA) {
                telemetry.addLine();
                try {
                    telemetry.addLine(
                            "Encoders: +\n Perp: " + perp.getPositionAndVelocity().position +
                                    "\nPar0: " + par0.getPositionAndVelocity().position +
                                    "\nPar1: " + par1.getPositionAndVelocity().position
                    );
                }
                catch (NullPointerException e) {
                    telemetry.addLine("Encoders Null! Retrying!");
                }
                telemetry.addLine();
            }
            if (Config.SHOW_CTRL1) {
                telemetry.addLine();
                telemetry.addLine("GAMEPAD1 STATUS: \n" + gamepad1.toString());
                telemetry.addLine();
            }
            if (Config.SHOW_CTRL2) {
                telemetry.addLine();
                telemetry.addLine("GAMEPAD2 STATUS: \n " + gamepad2.toString());
                telemetry.addLine();
            }

            if (Config.SHOW_TOWER_STATUS) {
                if (towerPosMovementStatus == TowerPosMovementStatus.MOVING_UP) {
                    telemetry.addLine("Tower Motor Pos Incremented " +
                            "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                            "\nTower Motor pos (desired) = " + pinkArm.towerMotor.getTargetPosition() +
                            "\nTower Motor pos (actual) = " + pinkArm.towerMotor.getCurrentPosition());
                } else if (towerPosMovementStatus == TowerPosMovementStatus.MOVING_DOWN) {
                    telemetry.addLine("Tower Motor Pos Decremented " +
                            "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                            "\nTower Motor pos (desired) = " + pinkArm.towerMotor.getTargetPosition() +
                            "\nTower Motor pos (actual) = " + pinkArm.towerMotor.getCurrentPosition());
                } else if (towerPosMovementStatus == TowerPosMovementStatus.NOT_MOVING) {
                    telemetry.addLine("Tower Motor Pos Neutral " +
                            "\nTower Motor pos (internal) = " + pinkArm.towerMotorPos +
                            "\nTower Motor pos (desired) = " + pinkArm.towerMotor.getTargetPosition() +
                            "\nTower Motor pos (actual) = " + pinkArm.towerMotor.getCurrentPosition());
                }
            }
            telemetry.update();

        }
    }
}

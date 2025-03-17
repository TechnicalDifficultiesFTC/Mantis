package org.firstinspires.ftc.teamcode.Main.OpModes.TeleOps;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Main.Subsystems.PinkArm;

@TeleOp(name="MANTIS TELEOP: Slide testing", group="AutonomousAnonymous")
public class SlideArmTesting extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        PinkArm pinkArm = new PinkArm(hardwareMap, true);
        telemetry.setMsTransmissionInterval(1);

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            pinkArm.slideMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            pinkArm.slideMotor.setTargetPosition(1000);
            pinkArm.slideMotor.setPower(1);

            telemetry.addLine("Slides target pos: " + pinkArm.slideMotor.getTargetPosition());
            telemetry.addLine("Slides current pow: " + pinkArm.slideMotor.getPower());
            telemetry.addLine();
            telemetry.addLine("Slides motor mode: " + pinkArm.slideMotor.getMode());
            telemetry.addLine("Slides current pos: " + pinkArm.slideMotor.getCurrentPosition());
            telemetry.addLine("(OVERFLOW) Slides current pos: " + pinkArm.slideEncoder.getPositionAndVelocity().position);
            telemetry.update();
        }
    }
}

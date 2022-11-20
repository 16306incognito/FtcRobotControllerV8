package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="ClawMotorTest", group="Linear Opmode")
public class ClawMotorTest extends LinearOpMode {

	private ElapsedTime runtime = new ElapsedTime();
	private Servo clampServo = null;
	private Servo armServo = null;
	private DcMotor shaftMotor = null;

	@Override
	public void runOpMode() {
		clampServo = hardwareMap.get(Servo.class, "Servo1");
		armServo = hardwareMap.get(Servo.class, "Servo2");
		shaftMotor = hardwareMap.get(DcMotor.class, "shaftMotor");

		// Wait for the game to start (driver presses PLAY)
		telemetry.addData("Status", "Initialized");
		telemetry.update();

		waitForStart();
		runtime.reset();

		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {
			double max;

			double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
			double lateral = -gamepad1.left_stick_x;
			double yaw = -gamepad1.right_stick_x;

			double leftFrontPower = axial + lateral + yaw;
			double rightFrontPower = axial - lateral - yaw;
			double leftBackPower = axial - lateral + yaw;
			double rightBackPower = axial + lateral - yaw;

			// Normalize the values so no wheel power exceeds 100%
			// This ensures that the robot maintains the desired motion.
			max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
			max = Math.max(max, Math.abs(leftBackPower));
			max = Math.max(max, Math.abs(rightBackPower));
			if (max > 1.0) {
				leftFrontPower /= max;
				rightFrontPower /= max;
				leftBackPower /= max;
				rightBackPower /= max;
			}
			if (gamepad1.right_bumper) {
				armServo.setPosition(70.0 / 300.0);
//				clampServo.setPosition(70.0 / 300.0);
			} else if (gamepad1.left_bumper) {
				armServo.setPosition(0.0 / 300.0);
//				clampServo.setPosition(0.0 / 300.0);
			}
//			clampServo.setPosition(-gamepad1.left_stick_y);

			double shaftPower = 0.5;
			shaftMotor.setPower(shaftPower);

			telemetry.addLine("Position: " + armServo.getPosition());
			telemetry.update();
		}
	}
}
package org.firstinspires.ftc.teamcode.mech;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystem.SubSystem;

public class ArmSubSystem extends SubSystem {

	// servo and motors
	private Servo armServo = null;
	private Servo wristServo = null;
	private Servo clawServo = null;

	private DcMotor shaftMotor = null;

	// arm
	private static final double ARM_MIN_POS = 0.0328;
	private static final double ARM_MAX_POS = 0.6;

	private double armCounter = ARM_MIN_POS;

	// wrist
	private double wristCounter = 0.3892;
	private static final double WRIST_MAX = 0.6468;
	private static final double WRIST_MIN = 0.12765;

	// claw
	private ElapsedTime clawTimer;
	private static final double CLAW_TOGGLE_COOLDOWN = 0.5;  // 0.5 sec

	// slow mode
	private ElapsedTime slowModeTimer;
	private static final double SLOW_TOGGLE_COOLDOWN = 0.3;
	private boolean slowMode = false;

	private double clamp(double a, double min, double max) {
		if (a < min) return min;
		if (a > max) return max;
		return a;
	}

	@Override
	public void init() {
		// set the timer
		clawTimer = new ElapsedTime();
		clawTimer.reset();
		slowModeTimer = new ElapsedTime();
		slowModeTimer.reset();

		// create servos
		clawServo = hardwareMap.get(Servo.class, "Servo1");
		armServo = hardwareMap.get(Servo.class, "Servo0");
		wristServo = hardwareMap.get(Servo.class, "Servo2");

		// create motors
		shaftMotor = hardwareMap.get(DcMotor.class, "shaftMotor");
	}

	@Override
	public void update() {
		// toggle slow mode
		if (gamepad1.x) {
			if (slowModeTimer.seconds() > SLOW_TOGGLE_COOLDOWN) {
				slowMode = !slowMode;
				slowModeTimer.reset();
			}
		}
		final double speedFactor = slowMode ? 1.0 : 2.0;

		// arm servo
		final double armSpeed = 0.0005 * speedFactor;
		armCounter += armSpeed * gamepad1.right_stick_y;
		armCounter = clamp(armCounter, ARM_MIN_POS, ARM_MAX_POS);
		armServo.setPosition(armCounter);

		// wrist servo
		final double wristSpeed = 0.001 * speedFactor;
		wristCounter += wristSpeed * gamepad1.left_stick_y;
		// clamp the wrist position
		wristCounter = clamp(wristCounter, WRIST_MIN, WRIST_MAX);

		wristServo.setPosition(wristCounter);

		// claw servo
		// if pressed
		if (gamepad1.a) {
			if (clawTimer.seconds() > CLAW_TOGGLE_COOLDOWN) {
				if (clawServo.getPosition() < 35.0 / 300.0) {
					clawServo.setPosition(70.0 / 300.0);
				} else {
					clawServo.setPosition(0.0 / 300.0);
				}
				clawTimer.reset();
			}
		}

		// shaft motor
		final double shaftSpeed = 0.5 * speedFactor;
		if (gamepad1.left_bumper) {
			// go down
			shaftMotor.setPower(shaftSpeed);
		} else if (gamepad1.right_bumper) {
			// go up
			shaftMotor.setPower(-shaftSpeed);
		} else {
			shaftMotor.setPower(0.0);
		}
	}
}
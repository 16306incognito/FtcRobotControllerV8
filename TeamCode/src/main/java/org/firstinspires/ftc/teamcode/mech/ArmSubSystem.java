package org.firstinspires.ftc.teamcode.mech;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystem.SubSystem;

public class ArmSubSystem extends SubSystem {

	private Servo armServo = null;
	private Servo wristServo = null;
	private Servo clawServo = null;

	private static final double MIN_COUNTER = 0.3892;

	private double armCounter = MIN_COUNTER;
	private double wristCounter = 0.3892;

	private static final double WRIST_MAX = 0.6468;
	private static final double WRIST_MIN = 0.12765;

	@Override
	public void init() {
		clawServo = hardwareMap.get(Servo.class, "Servo1");
		armServo = hardwareMap.get(Servo.class, "Servo0");
		wristServo = hardwareMap.get(Servo.class, "Servo2");
	}

	@Override
	public void update() {
		// arm servo
		final double speed = 0.0005;
		armCounter += speed * gamepad1.right_stick_y;
//		if (counter < MIN_COUNTER) counter = MIN_COUNTER;
		armServo.setPosition(armCounter);


		// wrist servo
		wristCounter += speed * gamepad1.left_stick_y;
		// clamp the wrist position
		if (wristCounter < WRIST_MIN) wristCounter = WRIST_MIN;
		else if (wristCounter > WRIST_MAX) wristCounter = WRIST_MAX;

		wristServo.setPosition(wristCounter);
		telemetry.addLine("wrist Servo: " + wristServo.getPosition());
		telemetry.update();

		// claw servo
		// if pressed
		if (gamepad1.a) {
			if (clawServo.getPosition() < 35.0 / 300.0) {
				clawServo.setPosition(70.0 / 300.0);
			} else {
				clawServo.setPosition(0.0 / 300.0);
			}
		}
	}
}
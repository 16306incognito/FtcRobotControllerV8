package org.firstinspires.ftc.teamcode.mech;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystem.SubSystem;

public class ArmSubSystem extends SubSystem {

	private ElapsedTime runtime = new ElapsedTime();
	private Servo clampServo = null;
	private Servo armServo = null;
	private DcMotor shaftMotor = null;

	@Override
	public void init() {
		clampServo = hardwareMap.get(Servo.class, "Servo1");
		armServo = hardwareMap.get(Servo.class, "Servo2");
		shaftMotor = hardwareMap.get(DcMotor.class, "shaftMotor");
	}

	@Override
	public void update() {
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

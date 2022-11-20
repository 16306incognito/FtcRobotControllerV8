package org.firstinspires.ftc.teamcode.mech;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystem.SubSystem;

public class ClawSubSystem extends SubSystem {

	//private ElapsedTime runtime = new ElapsedTime();
	private Servo clawServo = null;

	@Override
	public void init() {
		clawServo = hardwareMap.get(Servo.class, "Servo1");
	}

	@Override
	public void update() {
		if (gamepad1.right_bumper) {
			clawServo.setPosition(70.0 / 300.0);
		} else if (gamepad1.left_bumper) {
			clawServo.setPosition(0.0 / 300.0);
		}

//		telemetry.addLine("ClawPosition: " + clawServo.getPosition());
//		telemetry.update();
	}
}
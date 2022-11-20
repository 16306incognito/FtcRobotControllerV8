package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;

public class SubSystemOpMode extends OpMode {
	protected ArrayList<SubSystem> subSystems = new ArrayList<>();

	public SubSystemOpMode() {
		SubSystem.initSubSystems(telemetry, hardwareMap, gamepad1);
	}

	public void addSubSystem(SubSystem subSystem) {
		subSystems.add(subSystem);
	}

	@Override
	public void init() {
		for (SubSystem subSystem : subSystems) {
			subSystem.init();
		}
	}

	@Override
	public void loop() {
		for (SubSystem subSystem : subSystems) {
			subSystem.update();
		}
	}
}

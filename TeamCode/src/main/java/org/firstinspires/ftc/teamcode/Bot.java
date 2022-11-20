package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mech.ArmSubSystem;
import org.firstinspires.ftc.teamcode.subsystem.SubSystemOpMode;

@TeleOp(name="Bot", group="Linear Opmode")
public class Bot extends SubSystemOpMode {

	private ArmSubSystem armSubSystem;

	public Bot() {
		super();
	}

	@Override
	public void init() {
		armSubSystem = new ArmSubSystem();
		addSubSystem(armSubSystem);

		super.init();
	}
}

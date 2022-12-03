package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mech.ArmSubSystem;
import org.firstinspires.ftc.teamcode.mech.MovementSubSystem;
import org.firstinspires.ftc.teamcode.subsystem.SubSystemOpMode;

@TeleOp(name="FinalTeleOpBot", group="Linear Opmode")
public class FinalTeleOpBot extends SubSystemOpMode {

	private ArmSubSystem armSubSystem;
	private MovementSubSystem movementSubSystem;

	public FinalTeleOpBot() {
		super();
	}

	@Override
	public void init() {
		armSubSystem = new ArmSubSystem();
		movementSubSystem = new MovementSubSystem();
		// add the subsystems
		addSubSystem(armSubSystem);
		addSubSystem(movementSubSystem);

		super.init();
	}
}

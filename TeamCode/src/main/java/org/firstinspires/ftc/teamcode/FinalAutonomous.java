/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.vision.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.vision.Camera;

@Autonomous
public class FinalAutonomous extends LinearOpMode {

	private enum Phase {
		NONE, FORWARD, SIDE, END
	}

	private Camera camera;
	private AprilTagDetectionPipeline aprilTagDetectionPipeline;

	// motors
	private DcMotor leftFrontDrive = null;
	private DcMotor leftBackDrive = null;
	private DcMotor rightFrontDrive = null;
	private DcMotor rightBackDrive = null;

	private Servo clawServo = null;
	private Servo armServo = null;

	/*before 30*/
	private static final double FORWARD_DISTANCE = 28.0;
	private static final double SIDE_DISTANCE = 18.0;

	private AprilTagDetectionPipeline.Instruction instruction = AprilTagDetectionPipeline.Instruction.NONE;
	private Phase phase = Phase.NONE;

	private void forward() {
		// moving forward phase
		SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
		Trajectory trajectory = drive.trajectoryBuilder(new Pose2d())
				.forward(FORWARD_DISTANCE)
				.build();
		drive.followTrajectory(trajectory);
	}

	private void side() {
			// moving forward phase
			SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
			Trajectory trajectory = null;
			if (instruction == AprilTagDetectionPipeline.Instruction.ONE) {
				// left
				trajectory = drive.trajectoryBuilder(new Pose2d())
						.strafeLeft(-SIDE_DISTANCE)
						.build();
			} else if (instruction == AprilTagDetectionPipeline.Instruction.THREE) {
				// right
				trajectory = drive.trajectoryBuilder(new Pose2d())
						.strafeRight(-SIDE_DISTANCE)
						.build();
			}
			if (trajectory != null) {
				drive.followTrajectory(trajectory);
			}
	}

	@Override
	public void runOpMode() {
		aprilTagDetectionPipeline = new AprilTagDetectionPipeline(telemetry, Camera.FX, Camera.FY, Camera.CX, Camera.CY);
		camera = new Camera(telemetry, hardwareMap, "webcam1", aprilTagDetectionPipeline);

		leftFrontDrive = hardwareMap.get(DcMotor.class, "left_front_drive");
		leftBackDrive = hardwareMap.get(DcMotor.class, "left_back_drive");
		rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
		rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");

		clawServo = hardwareMap.get(Servo.class, "Servo1");
		armServo = hardwareMap.get(Servo.class, "Servo0");

		leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
		leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
		rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
		rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

		waitForStart();
		telemetry.setMsTransmissionInterval(50);

		while (opModeIsActive()) {
			if (phase == Phase.NONE) {
				//clawServo.setPosition(0.0);
				aprilTagDetectionPipeline.updateInLoop();
				instruction = aprilTagDetectionPipeline.getCurrentInstruction();

				if (instruction != AprilTagDetectionPipeline.Instruction.NONE) {
					phase = Phase.FORWARD;
					//armServo.setPosition(0.54);
					forward();
				}

				telemetry.addLine("Detected instruction: " + instruction);
				telemetry.update();
			} else if (phase == Phase.FORWARD) {
				// when the bot has finished moving forward, move to the side
				if (leftBackDrive.getPower() == 0.0 && rightBackDrive.getPower() == 0.0 && leftFrontDrive.getPower() == 0.0 && rightFrontDrive.getPower() == 0.0) {
					/*armServo.setPosition(0.6);*/
					phase = Phase.SIDE;
					side();
				}
			} else if (phase == Phase.SIDE) {
				// when the bot is done moving
				if (leftBackDrive.getPower() == 0.0 && rightBackDrive.getPower() == 0.0 && leftFrontDrive.getPower() == 0.0 && rightFrontDrive.getPower() == 0.0) {
					phase = Phase.END;
				}
			}

			if (phase == Phase.END) {
				break;
			}

			sleep(20);
		}
	}
}
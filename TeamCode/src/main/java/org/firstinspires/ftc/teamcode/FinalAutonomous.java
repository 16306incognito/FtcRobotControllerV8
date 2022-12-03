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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.vision.AprilTagDetectionPipeline;
import org.firstinspires.ftc.teamcode.vision.Camera;

@TeleOp
public class FinalAutonomous extends LinearOpMode {
	private Camera camera;
	private AprilTagDetectionPipeline aprilTagDetectionPipeline;

	// motors
	private DcMotor leftFrontDrive = null;
	private DcMotor leftBackDrive = null;
	private DcMotor rightFrontDrive = null;
	private DcMotor rightBackDrive = null;

	private boolean moving = false;

	private boolean atEndPosition = false;

	private void moveTo(AprilTagDetectionPipeline.Instruction instruction) {
		// moving forward phase
		
		if (instruction == AprilTagDetectionPipeline.Instruction.ONE) {
			// left

		} else if (instruction == AprilTagDetectionPipeline.Instruction.TWO) {
			// middle
		} else {
			// right
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

		leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
		leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
		rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
		rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

		waitForStart();
		telemetry.setMsTransmissionInterval(50);

		while (opModeIsActive() || !atEndPosition) {
			if (!moving) {
				aprilTagDetectionPipeline.updateInLoop();
				AprilTagDetectionPipeline.Instruction instruction = aprilTagDetectionPipeline.getCurrentInstruction();

				if (instruction != AprilTagDetectionPipeline.Instruction.NONE) {
					moving = true;
					moveTo(instruction);
				}

				telemetry.addLine("Detected instruction: " + instruction);
				telemetry.update();
			} else {
				// when the bot is moving to the target position

			}

			sleep(20);
		}
	}
}
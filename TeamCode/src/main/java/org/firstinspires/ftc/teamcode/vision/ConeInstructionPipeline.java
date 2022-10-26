package org.firstinspires.ftc.teamcode.vision;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;

public class ConeInstructionPipeline extends CustomOpenCVPipeline {
	public static enum Instruction {
		ONE, TWO, THREE, NONE
	}

	private Scalar topAvg, middleAvg, bottomAvg;

	private Mat topBoundingBox;
	private Mat middleBoundingBox;
	private Mat bottomBoundingBox;

	private Rect coneBoundingBox;

	public ConeInstructionPipeline(Telemetry telemetry, Rect coneBoundingBox) {
		super(telemetry);
		this.coneBoundingBox = coneBoundingBox;
	}

	@Override
	public void init(Mat firstFrame) {
		int colStart = coneBoundingBox.x;
		int colEnd = coneBoundingBox.x + coneBoundingBox.width;
		// create the 3 bounding boxes for the cone instruction detection
		// by splitting the bounding rectangle into 3 different sections
		topBoundingBox = firstFrame.submat(
				coneBoundingBox.y,
				coneBoundingBox.y + coneBoundingBox.height / 3,
				colStart,
				colEnd);
		middleBoundingBox = firstFrame.submat(
				coneBoundingBox.y + coneBoundingBox.height / 3,
				coneBoundingBox.y + coneBoundingBox.height * 2 / 3,
				colStart,
				colEnd);
		bottomBoundingBox = firstFrame.submat(
				coneBoundingBox.y + coneBoundingBox.height * 2/ 3,
				coneBoundingBox.y + coneBoundingBox.height,
				colStart,
				colEnd);
	}

	@Override
	public Mat processFrame(Mat input) {
		topAvg = Core.mean(topBoundingBox);
		middleAvg = Core.mean(middleBoundingBox);
		bottomAvg = Core.mean(bottomBoundingBox);

		boolean topInstruction = isColored(topAvg);
		boolean middleInstruction = isColored(middleAvg);
		boolean bottomInstruction = isColored(bottomAvg);

		Instruction a = Instruction.NONE;
		if (topInstruction) {
			a = Instruction.ONE;
		} else if (middleInstruction) {
			a = Instruction.TWO;
		} else if (bottomInstruction) {
			a = Instruction.THREE;
		}
		telemetry.addLine("Detected Instruction: " + a);

		return input;
	}

	private boolean isColored(Scalar a) {
		int avg = (int) (a.val[0] + a.val[1] + a.val[2]) / 3;
		final int blackThreshold = 100;
		return avg < blackThreshold;
	}
}

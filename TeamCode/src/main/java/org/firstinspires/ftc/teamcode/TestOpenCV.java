package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

@Autonomous
public class TestOpenCV extends OpMode {

    public static int CAMERA_WIDTH = 640;
    public static int CAMERA_HEIGHT = 360;
    // black, green, blue
    public static Scalar[] colors = new Scalar[]{new Scalar(0, 0, 0), new Scalar(0, 255, 0), new Scalar(0, 0, 255)};
    static public enum Instruction {
        ONE, TWO, THREE, NONE
    }

    OpenCvCamera webcam1 = null;
    @Override
    public void init() {
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam1");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam1 = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam1.setPipeline(new MyPipeline());

        webcam1.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam1.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
            }
        });
    }

    class MyPipeline extends  OpenCvPipeline {

        Scalar leftAvg, rightAvg;

        Mat left, right;
        @Override
        public void init(Mat firstFrame) {
            left = firstFrame.submat(1, CAMERA_HEIGHT, 1, CAMERA_WIDTH / 2);
            right = firstFrame.submat(1, CAMERA_HEIGHT, CAMERA_WIDTH / 2, CAMERA_WIDTH);
        }

        @Override
        public Mat processFrame(Mat input) {
            leftAvg = Core.mean(left);
            rightAvg = Core.mean(right);

            // check left and right
            Instruction left = getInstruction(leftAvg);
            Instruction right = getInstruction(rightAvg);
            if (left == Instruction.NONE && right == Instruction.NONE) {
                telemetry.addLine("no color detected");
            } else {
                Instruction a = left;
                if (left == Instruction.NONE) {
                    a = right;
                } else if (right == Instruction.NONE) {
                    a = left;
                }
                telemetry.addLine("v1: detected instruction:  " + a);
            }
            return input;
        }

        private Instruction getInstruction(Scalar a) {
            int r = (int) a.val[0];
            int g = (int) a.val[1];
            int b = (int) a.val[2];
            telemetry.addLine("rgb: " + r + " " + g + " " + b);
            int blackRange = 100;
            // black
            if (r < blackRange && g < blackRange && b < blackRange) {
                return Instruction.ONE;
            }
            // green
            if (g - b > 20 && g - r > 20) {
                return Instruction.TWO;
            }
            // blue
            if (b - g > 20 && b - r > 20) {
                return Instruction.THREE;
            }
            return Instruction.NONE;
        }
    }

    @Override
    public void loop() {

    }

}
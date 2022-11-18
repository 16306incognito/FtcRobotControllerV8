package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.vision.Camera;
import org.firstinspires.ftc.teamcode.vision.ConeDetectionPipeline;
import org.opencv.core.Rect;

@Autonomous
public class TestOpenCV extends OpMode {

    private Camera camera;
    private ConeDetectionPipeline coneDetectionPipeline;

    @Override
    public void init() {
        coneDetectionPipeline = new ConeDetectionPipeline(telemetry);
        camera = new Camera(telemetry, hardwareMap, "webcam1", coneDetectionPipeline);
    }

    @Override
    public void loop() {
        Rect rect = coneDetectionPipeline.getCurrentBoundingBox();
        if (rect != null) {
            if (rect.width > rect.height) {
                telemetry.addLine("Follow the cone!");
            } else {
                telemetry.addLine("NO!");
            }
        }

        telemetry.update();
    }
}
import java.util.ArrayList;

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Vision extends VisionModule {
    public void run(Mat frame) {
        postImage(frame, "Camera Feed");
    }
}

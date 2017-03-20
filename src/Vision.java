import java.util.ArrayList;

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;
import stuyvision.gui.IntegerSliderVariable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Vision extends VisionModule {
    public void run(Mat frame) {
        postImage(frame, "Camera Feed");

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();

        Core.split(frame, channels);

        postImage(channels.get(0), "Hue channel");

        Mat filtered = new Mat();

        Core.inRange(channels.get(0), new Scalar(150), new Scalar(230), filtered);

        postImage(filtered, "Filtered for teel");
    }
}

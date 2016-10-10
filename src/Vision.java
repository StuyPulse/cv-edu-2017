import java.util.ArrayList;

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;
import stuyvision.gui.IntegerSliderVariable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Vision extends VisionModule {
    public IntegerSliderVariable minRed = new IntegerSliderVariable("Min Red", 0,  0, 255);
    public IntegerSliderVariable maxRed = new IntegerSliderVariable("Max Red", 70, 0, 255);

    public void run(Mat frame) {
        postImage(frame, "Camera Feed");

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(frame, channels);

        Mat redChannel = channels.get(2);

        Core.inRange(redChannel, new Scalar(minRed.value()), new Scalar(maxRed.value()), frame);

        postImage(frame, "Filtered Frame");
    }
}

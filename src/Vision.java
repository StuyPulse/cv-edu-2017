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
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Hue Min", 64, 0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Hue Max", 100, 0, 255);   
    public IntegerSliderVariable minSat = new IntegerSliderVariable("Sat Min", 64, 0, 255);
    public void run(Mat frame) {
        postImage(frame, "Camera Feed");

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(frame, channels);
        postImage(channels.get(0), "Hui Channel");
        postImage(channels.get(1), "Saturation Channel");
        postImage(channels.get(2), "Value Channel");

        Mat grayscale = new Mat();
        Core.bitwise_not(channels.get(1), grayscale);
        postImage(grayscale, "Grayscale?");
        
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        postImage(channels.get(0), "Hue-Filtered Frame");
    }
}

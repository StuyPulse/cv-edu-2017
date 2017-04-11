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
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Hue Min", 0, 0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Hue Max", 100, 0, 255);      

    public IntegerSliderVariable minSat = new IntegerSliderVariable("Sat Min", 0, 0, 255);
    public IntegerSliderVariable maxSat = new IntegerSliderVariable("Sat Max", 100, 0, 255);

    public IntegerSliderVariable minVal = new IntegerSliderVariable("Val Min", 0, 0, 255);
    public IntegerSliderVariable maxVal = new IntegerSliderVariable("Val Max", 100, 0, 255);

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
	
	Core.inRange(channels.get(1), new Scalar(minSat.value()), new Scalar(maxSat.value()), channels.get(1));
	postImage(channels.get(1), "Saturation-Filtered Frame");
	
	Core.inRange(channels.get(2), new Scalar(minVal.value()), new Scalar(maxVal.value()), channels.get(2));
	postImage(channels.get(2), "Value-Filtered Frame");
	
	Mat HuiSatFiltered = new Mat();
	Mat HSVFilteredBlue = new Mat();
	void Core.bitwise_and(channels.get(0), channels.get(1), HuiSatFiltered);
	postImage(HuiSatFiltered, "Hui and Saturation Filtered");
	void Core.bitwise_and(HuiSatFiltered, channels.get(2), HSVFilteredBlue);
	postImage(HSVFilteredBlue, "HSV Filtered for Blue?");
    }
}

import java.util.ArrayList;

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;
import stuyvision.gui.IntegerSliderVariable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import stuyvision.gui.IntegerSliderVariable;
import stuyvision.gui.IntegerSliderVariable;

public class Vision extends VisionModule {
	
	public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 101, 0, 255);
	public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 143, 0, 255);
	public IntegerSliderVariable minSat = new IntegerSliderVariable("Min Sat", 179, 0, 255);
	public IntegerSliderVariable maxSat = new IntegerSliderVariable("Max Sat", 237, 0, 255);
	public IntegerSliderVariable minVal = new IntegerSliderVariable("Min Val", 255, 0, 255);
	public IntegerSliderVariable maxVal = new IntegerSliderVariable("Max Val", 255, 0, 255);
	
	public void run(Mat frame) {
    	
        postImage(frame, "Camera Feed");
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();

        Core.split(frame, channels);
        //postImage(channels.get(0), "Hue channel");

        Mat filterHue = new Mat();
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), filterHue);
        postImage(filterHue, "Darwinism");
        
        Mat filterSat = new Mat();
        Core.inRange(channels.get(1), new Scalar(minSat.value()), new Scalar(maxSat.value()), filterSat);
        postImage(filterSat, "Bryan has no Friends");
        
        Mat filterVal = new Mat();
        Core.inRange(channels.get(2), new Scalar(minVal.value()), new Scalar(maxVal.value()), filterVal);
        postImage(filterVal, "Glados");
        
        Mat filterUpdate = new Mat();
        
        Core.bitwise_and(filterVal, filterSat, filterUpdate);
        Core.bitwise_and(filterUpdate, filterHue, filterUpdate);
        postImage(filterUpdate, "HSV filter");
        
        Mat HSVFilterNColor = new Mat();
        ArrayList<Mat> filterUpdateSandwich = new ArrayList<Mat>();
        
        filterUpdateSandwich.add(filterUpdate);
        filterUpdateSandwich.add(filterUpdate);
        filterUpdateSandwich.add(filterUpdate);
        Core.merge(filterUpdateSandwich, filterUpdate);
        Imgproc.cvtColor(filterUpdate,filterUpdate, Imgproc.COLOR_GRAY2BGR);
        Imgproc.cvtColor(filterUpdate,filterUpdate, Imgproc.COLOR_BGR2HSV);
        Core.bitwise_and(filterUpdate, frame, HSVFilterNColor);
        postImage(HSVFilterNColor, "Racist HSV");
    }
}

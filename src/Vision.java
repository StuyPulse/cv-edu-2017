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
	public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 110, 0, 255);
	public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 116, 0, 255);
	public IntegerSliderVariable minSat = new IntegerSliderVariable("Min Sat", 100, 0, 255);
	public IntegerSliderVariable maxSat = new IntegerSliderVariable("Max Sat", 101, 0, 255);
	public IntegerSliderVariable minVal = new IntegerSliderVariable("Min Val", 101, 0, 255);
	public IntegerSliderVariable maxVal = new IntegerSliderVariable("Max Val", 101, 0, 255);
	public IntegerSliderVariable dilateKernelSize = new IntegerSliderVariable("dilateKernelSize", 9, 1, 30);
	public IntegerSliderVariable erodeKernelSize = new IntegerSliderVariable("erodeKernelSize", 9, 1, 30);
	
	
	public void run(Mat frame) {
   	if (dilateKernelSize%2 == 0){
   	}else{ ? dilateKernelSize ++ : dilateKernelSize;
    	
  	erodeKernelSize = (erodeKernelSize%2 == 0) ? erodeKernelSize++ : erodeKernelSize;
    	
        postImage(frame, "Camera Feed");

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();

        Core.split(frame, channels);

        postImage(channels.get(0), "Hue channel");

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
        
        Mat kernelSize = new Mat();
        
        
        
    }
}

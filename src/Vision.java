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
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Hui Min", 98, 0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Hui Max", 136, 0, 255);
    public IntegerSliderVariable minSat = new IntegerSliderVariable("Sat Min", 205, 0, 255);
    public IntegerSliderVariable maxSat = new IntegerSliderVariable("Sat Max", 255, 0, 255);
    public IntegerSliderVariable minVal = new IntegerSliderVariable("Val Min", 193, 0, 255);
    public IntegerSliderVariable maxVal = new IntegerSliderVariable("Val Max", 255, 0, 255);

    public void run(Mat frame) {
        Mat huiSatFiltered = new Mat();
        Mat hsvFilteredBlue = new Mat();
        Mat huiFiltered = new Mat();
    	Mat satFiltered = new Mat();
        Mat valFiltered = new Mat();
    	Mat bGrayChannel = new Mat();
        Mat merged = new Mat();

        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
        
        postImage(frame, "Camera Feed");

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(frame, channels);
        /*postImage(channels.get(0), "Hui Channel");
        postImage(channels.get(1), "Saturation Channel");
        postImage(channels.get(2), "Value Channel");*/
     
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), huiFiltered);
        Imgproc.erode(huiFiltered, huiFiltered, erodeKernel);
        Imgproc.dilate(huiFiltered, huiFiltered, dilateKernel);
        postImage(huiFiltered, "Hui-Filtered Frame");
	
    	Core.inRange(channels.get(1), new Scalar(minSat.value()), new Scalar(maxSat.value()), satFiltered);
    	Imgproc.erode(satFiltered, satFiltered, erodeKernel);
        Imgproc.dilate(satFiltered, satFiltered, dilateKernel);
        postImage(satFiltered, "Saturation-Filtered Frame");
	
    	Core.inRange(channels.get(2), new Scalar(minVal.value()), new Scalar(maxVal.value()), valFiltered);
    	Imgproc.erode(valFiltered, valFiltered, erodeKernel);
        Imgproc.dilate(valFiltered, valFiltered, dilateKernel);
        postImage(valFiltered, "Value-Filtered Frame");

	    Core.bitwise_and(huiFiltered, satFiltered, huiSatFiltered);
	    Core.bitwise_and(huiSatFiltered, valFiltered, hsvFilteredBlue);
	    postImage(hsvFilteredBlue, "HSV Filtered for Blue?");

    	Core.bitwise_and(channels.get(1), hsvFilteredBlue, bGrayChannel);
        Imgproc.erode(bGrayChannel, bGrayChannel, erodeKernel);
        Imgproc.dilate(bGrayChannel, bGrayChannel, dilateKernel);
        postImage(bGrayChannel, "Black and Grayscale Channel");

        ArrayList<Mat> mergeChannels = new ArrayList<Mat>();
        mergeChannels.add(channels.get(0));
        mergeChannels.add(bGrayChannel);
        mergeChannels.add(channels.get(2));
        Core.merge(mergeChannels, merged);
        postImage(merged, "Merged channel");

        Imgproc.cvtColor(merged, merged, Imgproc.COLOR_HSV2BGR);
        postImage(merged, "Merged w/ color");
        
    }
}

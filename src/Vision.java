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
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 0, 0, 180);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 180, 0, 180);
    public IntegerSliderVariable maxSat = new IntegerSliderVariable("Min Sat", 0, 0, 255);
    public IntegerSliderVariable minSat = new IntegerSliderVariable("Max Sat", 255, 0, 255);
    public IntegerSliderVariable minVal = new IntegerSliderVariable("Min Val", 0, 0, 255);
    public IntegerSliderVariable maxVal = new IntegerSliderVariable("Max Val", 255, 0, 255);
    
    public void run(Mat frame) {
        
        postImage(frame, "Camera Feed");
        
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
        
        Mat primaryFilteredChannel = new Mat();
        Mat secondaryFilteredChannel = new Mat();
        Mat hueFilteredFrame = new Mat();
        Mat filteredHueOnBlack = new Mat();
        
        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(frame, channels);
        
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        Core.inRange(channels.get(1), new Scalar(maxSat.value()), new Scalar(minSat.value()), channels.get(1));
        Core.inRange(channels.get(2), new Scalar(minVal.value()), new Scalar(maxVal.value()), channels.get(2));
        
        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
        
        Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);
        Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);
        postImage(channels.get(0), "Hues");
        
        Imgproc.erode(channels.get(1), channels.get(1), erodeKernel);
        Imgproc.dilate(channels.get(1), channels.get(1), dilateKernel);
        postImage(channels.get(1), "Saturation");

        postImage(channels.get(2), "Value");
        
        Core.bitwise_and(channels.get(1), channels.get(0), primaryFilteredChannel);
        postImage(primaryFilteredChannel, "Primary Filtered Channel");
        
        Core.bitwise_and(primaryFilteredChannel, channels.get(2), secondaryFilteredChannel);
        postImage(secondaryFilteredChannel, "Secondary Filtered Channel");
        
        ArrayList<Mat> hueFilteredChannels = new ArrayList<Mat>();
        hueFilteredChannels.add(secondaryFilteredChannel);
        hueFilteredChannels.add(secondaryFilteredChannel);
        hueFilteredChannels.add(secondaryFilteredChannel);
        Core.merge(hueFilteredChannels, hueFilteredFrame);
        
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_HSV2BGR);
        Core.bitwise_and(frame, hueFilteredFrame, filteredHueOnBlack);
        postImage(filteredHueOnBlack, "Filtered Hue on Black");
        
        ArrayList<Mat> valuesChannels = new ArrayList<Mat>();
        valuesChannels.add(channels.get(2));
        //valuesChannels.add(channels.get(2));
        //valuesChannels.add(channels.get(2));
        
        Mat valuesFrame = new Mat();
        
        Core.merge(valuesChannels, valuesFrame);
        
        /*Core.bitwise_not(valuesFrame, valuesFrame);
        Core.bitwise_xor(valuesFrame, filteredHueOnBlack, valuesFrame);

        postImage(valuesFrame, "Filtered Hue on GrayScale"); */
        
        Mat invertedFilter = new Mat();
        
        Core.bitwise_not(filteredHueOnBlack, invertedFilter);
        
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
        
        ArrayList<Mat> grayChannel = new ArrayList<Mat>();
        grayChannel.add(frame);
        grayChannel.add(frame);
        grayChannel.add(frame);

        Mat grayFrame = new Mat();
        
        Core.merge(grayChannel, grayFrame);
        
        postImage(grayFrame, "Gray Frame");

        //Core.bitwise_not(grayFrame, grayFrame);m
        
        Core.bitwise_not(hueFilteredFrame, hueFilteredFrame);
        
        Core.bitwise_and(grayFrame, hueFilteredFrame, grayFrame);
        
        Core.bitwise_not(grayFrame, grayFrame);
        
        postImage(grayFrame, "Special Gray Frame");
        
        postImage(invertedFilter, "Inverted Filter");
        
        //Core.add(grayFrame, invertedFilter, frame);
        
        Core.bitwise_xor(filteredHueOnBlack, grayFrame, frame);
        
        Core.bitwise_not(frame, frame);
        
        postImage(frame, "Filtered hue on Gray");
        
    }
}

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
        
        
        //Instatialixation of Mats used throughout the code onwards.
        //These may not necessarily be in order of use.
        Mat primaryFilteredChannel = new Mat();
        Mat secondaryFilteredChannel = new Mat();
        Mat hueFilteredFrame = new Mat();
        Mat filteredHueOnBlack = new Mat();
        Mat valuesFrame = new Mat();
        Mat grayFrame = new Mat();
        Mat invertedFilter = new Mat();
        
        //Used to split the main frame into Channels for parameterization and scaling.
        //Yes, that is a word now.
        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(frame, channels);
        
        //Using slider data in to change the scaling on each of the 3 channels of HSV.
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        Core.inRange(channels.get(1), new Scalar(maxSat.value()), new Scalar(minSat.value()), channels.get(1));
        Core.inRange(channels.get(2), new Scalar(minVal.value()), new Scalar(maxVal.value()), channels.get(2));
        
        //These functions are used to erode and dilate the blacks and whites of the seperate channels.
        //By enlargening the blacks, and then the whites, we are able to get rid of any small noise that may cause problems.
        //The ize instance dictates how large the erosion/dilation factor will be.
        Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        
        //Erodes and Dilates the Hue channel for remove Noise.
        Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);
        Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);
        postImage(channels.get(0), "Hues");
        
        //Erodes and dilates the Saturation channel to remove noise.
        Imgproc.erode(channels.get(1), channels.get(1), erodeKernel);
        Imgproc.dilate(channels.get(1), channels.get(1), dilateKernel);
        postImage(channels.get(1), "Saturation");

        //Erodes and Dilates the Values channel to remove noise.
        Imgproc.erode(channels.get(2), channels,get(2), erodeKernel);
        Imgproc.dilate()channels.get(2), channels.get(2), dilateKernel;
        postImage(channels.get(2), "Value");
        
        //Merges the scaled Hue and Saturation channel for a primary filter.
        Core.bitwise_and(channels.get(1), channels.get(0), primaryFilteredChannel);
        postImage(primaryFilteredChannel, "Primary Filtered Channel");
        
        //Merges the primary filter and the value channel to create the final, most accurate, filter.
        Core.bitwise_and(primaryFilteredChannel, channels.get(2), secondaryFilteredChannel);
        postImage(secondaryFilteredChannel, "Secondary Filtered Channel");
        
        //Stores the secondary filter into a Mat with three channels.
        //In order to operate on a frame with bitwise operations, the frames must have the same number of channels.
        //By making a Mat with three channels of the same thing, we are able to use this in bitwise operations with the original frame.
        ArrayList<Mat> hueFilteredChannels = new ArrayList<Mat>();
        hueFilteredChannels.add(secondaryFilteredChannel);
        hueFilteredChannels.add(secondaryFilteredChannel);
        hueFilteredChannels.add(secondaryFilteredChannel);
        //The merge method is used to mix channel into one frame.
        Core.merge(hueFilteredChannels, hueFilteredFrame);
        
        //The original frame must be converted back into BGR in order for the color to show properly.
        //This is because the computer operates on images using BGR, and the HSV data will not be read properly.
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_HSV2BGR);
        //This uses an "and" operator on the filter, and the original frame.
        //The result is the isolated color, displayed on a black background.
        //If viewed on the color wheel on the second tab, it will appear as a slice of color on a square of black.
        Core.bitwise_and(frame, hueFilteredFrame, filteredHueOnBlack);
        postImage(filteredHueOnBlack, "Filtered Hue on Black");
        
        //This stores the opposite of the desired frame into the mat specified.
        //i.e: the opposite of filteredHueOnBlack is stored in the Mat invertedFilter.
        Core.bitwise_not(filteredHueOnBlack, invertedFilter);
        
        //This converts the original frame into grayscale, for later operations.
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
        
        
        //Grayscale has only one channel, so in rder to operate on it, a three channel Mat must be created.
        ArrayList<Mat> grayChannel = new ArrayList<Mat>();
        grayChannel.add(frame);
        grayChannel.add(frame);
        grayChannel.add(frame);
        Core.merge(grayChannel, grayFrame);
        
        postImage(grayFrame, "Gray Frame");
        
        //This finds the opposite of the secondaryFilteredChannel, which was stored threesome into the hueFilteredFrame Mat.
        Core.bitwise_not(hueFilteredFrame, hueFilteredFrame);
        
        //By merging the hueFilteredFrame's opposite into grayframe, we get a grayframe that shows the isolated color as black.
        //This is not displayed.
        Core.bitwise_and(grayFrame, hueFilteredFrame, grayFrame);
        
        //This finds the inverse, which will be useful later on.
        Core.bitwise_not(grayFrame, grayFrame);
        
        //This just postss the Gray Frame after editing for use later.
        postImage(grayFrame, "Special Gray Frame");
        
        //This is the Inverted form of filteredHueOnBlack, which is the opposite of the hue we want, on white.
        postImage(invertedFilter, "Inverted Filter");
        
        //By using bitwise_xor, we are able blend the colors into a suitable mixture.
        //bitwise_xor is exclusive or, and works as an or statement that returns false when both statements are true.
        //Therefore, we are able to blend the two seperate pies together due to the presence of white.
        //The white on white is simply replace by black, which forces the image to be inverted.
        Core.bitwise_xor(filteredHueOnBlack, grayFrame, frame);
        
        //We need to apply this because the image is inverted after using the exculsive or statement.
        Core.bitwise_not(frame, frame);
        
        postImage(frame, "Filtered hue on Gray");
        
    }
}

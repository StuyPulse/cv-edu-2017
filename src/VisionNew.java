import java.util.ArrayList;
import java.util.List;

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;
import stuyvision.gui.IntegerSliderVariable;
import stuyvision.gui.DoubleSliderVariable;
import stuyvision.gui.BooleanVariable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.MatOfPoint;

public class VisionNew extends VisionModule {
    // Static in theory should make one change all
    public static IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 94,  0, 255);

    public static IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 255, 0, 255);

    public static IntegerSliderVariable minSat = new IntegerSliderVariable("Min Sat", 96, 0, 255);
    public static IntegerSliderVariable maxSat = new IntegerSliderVariable("Max Sat", 255, 0, 255);
    
    public static IntegerSliderVariable minVal = new IntegerSliderVariable("Min Val", 127, 0, 255);
    public static IntegerSliderVariable maxVal = new IntegerSliderVariable("Max Val", 255, 0, 255);

    public static DoubleSliderVariable blurFactor = new DoubleSliderVariable("Blur", 0.5, 0, 10);

    public static IntegerSliderVariable erodeFactor = new IntegerSliderVariable("erode factor",6,0,10);
    public static IntegerSliderVariable dilateFactor = new IntegerSliderVariable("dilate factor",7,0,10);

    public static BooleanVariable erodeFirst = new BooleanVariable("Erode First?", false);
    
    private Mat morphKernel;
    private final int dilate_MORPH_SHAPE = Imgproc.MORPH_RECT;
    private final int ERODE_MORPH_SHAPE = Imgproc.MORPH_RECT;

    private void separateHSV(Mat frame) {
        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(frame, channels);

        // Get hsv channels
        Mat hueChannel = channels.get(0);
        Mat satChannel = channels.get(1);
        Mat valChannel = channels.get(2);

        Core.inRange(hueChannel, new Scalar(minHue.value()), new Scalar(maxHue.value()), hueChannel);
        Core.inRange(satChannel, new Scalar(minSat.value()), new Scalar(maxSat.value()), satChannel);
        Core.inRange(valChannel, new Scalar(minVal.value()), new Scalar(maxVal.value()), valChannel);
    
        // Bitwise and to only include parts that match
        Core.bitwise_and(hueChannel,satChannel,frame);
        Core.bitwise_and(frame,valChannel,frame);
    }

    private void diluteErode(Mat frame) {
        // Create erode and dilate kernal based on dilate/erode factor 
        // Depending on erodeFirst boolean slider, erode or dilate first.

        double dsize;
        if (erodeFirst.getValue()) {
            dsize = dilateFactor.value() * 2 + 1;
            morphKernel = Imgproc.getStructuringElement(ERODE_MORPH_SHAPE, new Size(dsize,dsize));
            Imgproc.erode(frame, frame, morphKernel);

            dsize = erodeFactor.value() * 2 + 1;
            morphKernel = Imgproc.getStructuringElement(dilate_MORPH_SHAPE, new Size(dsize,dsize));
            Imgproc.dilate(frame, frame, morphKernel);
        } else {
            dsize = erodeFactor.value() * 2 + 1;
            morphKernel = Imgproc.getStructuringElement(dilate_MORPH_SHAPE, new Size(dsize,dsize));
            Imgproc.dilate(frame, frame, morphKernel);

            dsize = dilateFactor.value() * 2 + 1;
            morphKernel = Imgproc.getStructuringElement(ERODE_MORPH_SHAPE, new Size(dsize,dsize));
            Imgproc.erode(frame, frame, morphKernel);    
        }
    }

    private void blur(Mat frame) {
        Imgproc.GaussianBlur(frame,frame,new Size(0,0),blurFactor.value());
    }

    private void drawContours(Mat frame) {
        
    }

    public void run(Mat frame) {
        postImage(frame, "Camera Feed");
        Imgproc.cvtColor(frame,frame,Imgproc.COLOR_BGR2HSV);

        //blur(frame);
        separateHSV(frame);
        //diluteErode(frame);
        drawContours(frame);

        postImage(frame, "Filtered Frame");
    }
}

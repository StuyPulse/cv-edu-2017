import java.util.ArrayList;

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;
import stuyvision.gui.IntegerSliderVariable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Vision extends VisionModule {
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 64,  0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 92, 0, 255);

    public IntegerSliderVariable minSaturation = new IntegerSliderVariable("Min Saturation", 0, 0, 255);
    public IntegerSliderVariable maxSaturation = new IntegerSliderVariable("Max Saturation", 255, 0, 255);

    public IntegerSliderVariable minValue = new IntegerSliderVariable("Min Value", 0, 0, 255);
    public IntegerSliderVariable maxValue = new IntegerSliderVariable("Max Value", 255, 0, 255);

    public void run(Mat frame) {
        postImage(frame, "Camera Feed");

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(frame, channels);

        // channels.get(0) is the hue channel
        // channels.get(1) is the saturation channel
        // channels.get(2) is the value channel

        Mat hueChannel = channels.get(0);

        Core.inRange(hueChannel, new Scalar(minHue.value()), new Scalar(maxHue.value()), hueChannel);

        postImage(hueChannel, "Hue-Filtered Frame");

        Mat saturationChannel = channels.get(1);

        Core.inRange(saturationChannel, new Scalar(minSaturation.value()), new Scalar(maxSaturation.value()), saturationChannel);

        postImage(saturationChannel, "Saturation-Filtered Frame");

        Mat valueChannel = channels.get(2);

        Core.inRange(valueChannel, new Scalar(minValue.value()), new Scalar(maxValue.value()), valueChannel);

        postImage(valueChannel, "Value-Filtered Frame");
    }
}

import java.util.ArrayList;

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;
import stuyvision.gui.IntegerSliderVariable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Simple extends VisionModule {
    public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 75, 0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 120, 0, 255);

    public IntegerSliderVariable minSaturation = new IntegerSliderVariable("Min Saturation", 43, 0, 255);
    public IntegerSliderVariable maxSaturation = new IntegerSliderVariable("Max Saturation", 175, 0, 255);

    public IntegerSliderVariable minValue = new IntegerSliderVariable("Min Value", 0, 0, 255);
    public IntegerSliderVariable maxValue = new IntegerSliderVariable("Max Value", 224, 0, 255);

    private ArrayList<Mat> hsvThreshold(Mat in, Mat out) {
        Imgproc.cvtColor(in, out, Imgproc.COLOR_BGR2HSV);
        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(out, channels);

        // Now binarize the Hue channel to reflect the [minHue, maxHue] range
        Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
        // The saturation channel
        Core.inRange(channels.get(1), new Scalar(minSaturation.value()), new Scalar(maxSaturation.value()), channels.get(1));
        // The value channel
        Core.inRange(channels.get(2), new Scalar(minValue.value()), new Scalar(maxValue.value()), channels.get(2));

        // `and` them into `out`
        Core.bitwise_and(channels.get(0), channels.get(1), out);
        Core.bitwise_and(channels.get(2), out, out);

        return channels;
    }

    public void run(Mat frame) {
        Mat frameCopy = null;
        if (hasGuiApp()) {
            frameCopy = frame.clone(); // Contours will be drawn on this
            postImage(frame, "Camera Feed");
        }

        ArrayList<Mat> channels = hsvThreshold(frame, frame);
        if (hasGuiApp()) {
            postImage(channels.get(0), "Hue threshold");
            postImage(channels.get(1), "Saturation threshold");
            postImage(channels.get(2), "Value threshold");
            postImage(frame, "Combined threshold");
        }
    }
}

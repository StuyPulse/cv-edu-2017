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

	public IntegerSliderVariable minHue = new IntegerSliderVariable("Min Hue", 0,  0, 255);
    public IntegerSliderVariable maxHue = new IntegerSliderVariable("Max Hue", 255, 0, 255);

	public void run(Mat frame) {
		postImage(frame, "Camera Feed");

		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

		ArrayList<Mat> channels = new ArrayList<Mat>();
		Core.split(frame, channels);

		Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
		postImage(channels.get(0), "Hue-Filtered Frame");

		postImage(channels.get(2), "grayscale");

		ArrayList<Mat> hueChannels = new ArrayList<Mat>();
		hueChannels.add(channels.get(0));
		hueChannels.add(channels.get(0));
		hueChannels.add(channels.get(0));

		Mat hueFilterColor = new Mat();

		Core.merge(hueChannels, hueFilterColor);

		Imgproc.cvtColor(frame, frame, Imgproc.COLOR_HSV2BGR);

		Core.bitwise_and(frame, hueFilterColor, hueFilterColor);
		postImage(hueFilterColor, "Hue-Filtered Frame (color)");

		Core.bitwise_not(channels.get(0), channels.get(0));
		postImage(channels.get(0), "not(Hue-Filtered Frame)");

		Core.bitwise_and(channels.get(0), channels.get(2), channels.get(2));
		postImage(channels.get(2), "value but there's a hole");

		ArrayList<Mat> chaseBankChannels = new ArrayList<Mat>();
		chaseBankChannels.add(channels.get(2));
		chaseBankChannels.add(channels.get(2));
		chaseBankChannels.add(channels.get(2));

		Mat chaseBankChannel = new Mat();

		Core.merge(chaseBankChannels, chaseBankChannel);

		Core.bitwise_or(chaseBankChannel, hueFilterColor, chaseBankChannel);

		postImage(chaseBankChannel, "chaseBankChannel xd");
	}
}

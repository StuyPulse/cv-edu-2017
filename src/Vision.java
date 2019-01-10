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

  }
}

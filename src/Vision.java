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
    public void run(Mat frame) {
        Mat huiSatFiltered = new Mat();
        Mat hsvFilteredBlue = new Mat();
	Mat satFiltered = new Mat();
	Mat bGrayChannel = new Mat();

        postImage(frame, "Camera Feed");

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

        ArrayList<Mat> channels = new ArrayList<Mat>();
        Core.split(frame, channels);
        postImage(channels.get(0), "Hui Channel");
        postImage(channels.get(1), "Saturation Channel");
        postImage(channels.get(2), "Value Channel");
     
        Core.inRange(channels.get(0), new Scalar(98), new Scalar(136), channels.get(0));
        postImage(channels.get(0), "Hui-Filtered Frame");
	
	Core.inRange(channels.get(1), new Scalar(216), new Scalar(255), satFiltered);
	postImage(satFiltered, "Saturation-Filtered Frame");
	
	Core.inRange(channels.get(2), new Scalar(193), new Scalar(255), channels.get(2));
	postImage(channels.get(2), "Value-Filtered Frame");
	
	Core.bitwise_and(channels.get(0), channels.get(1), huiSatFiltered);
	Core.bitwise_and(huiSatFiltered, channels.get(2), hsvFilteredBlue);
	postImage(hsvFilteredBlue, "HSV Filtered for Blue?");

	Core.bitwise_and(channels.get(1), satFiltered, bGrayChannel);

    }
}

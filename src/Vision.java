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
        postImage(frame, "Camera Feed");
   	
	Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

	ArrayList<Mat> channels = new ArrayList<Mat>();
	Core.split(frame, channels);
	postImage(channels.get(0), "Hui Channel");
	postImage(channels.get(1), "Saturation Channel");
	postImage(channels.get(2), "Value Channel");
	
	Mat merged = new Mat();
	Core.merge(channels, merged);
	Imgproc.cvtColor(merged, merged, Imgproc.COLOR_HSV2BGR);
	postImage(merged, "Merged channel");
   	//should be outputing the same form as CameraFeed
	
	//should dilate
	Mat dilateKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
	Imgproc.dilate(channels.get(0), channels.get(0), dilateKernel);
	postImage(channels.get(0), "Dilated hui");
	
	//should erode
	Mat erodeKernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(7, 7));
	Imgproc.erode(channels.get(0), channels.get(0), erodeKernel);
	postImage(channels.get(0), "Eroded hui");

	//should blur the merged frame
	Imgproc.medianBlur(merged, merged, 5);
	postImage(merged, "Merged RGB channel");
   }
}

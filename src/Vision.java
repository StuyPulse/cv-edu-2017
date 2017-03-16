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
       //postImage(channels.get(0), "Hue channel");
       Core.inRange(channels.get(0), new Scalar(110), new Scalar(112), channels.get(0));
       //postImage(channels.get(0), "Ranged Hue channel");
       
       Imgproc.medianBlur(channels.get(0), channels.get(0), 5);
       //postImage(channels.get(0), "Blurred Hue channel");
       
       //postImage(channels.get(1), "Sat channel");
       Core.inRange(channels.get(1), new Scalar(200), new Scalar(255), channels.get(1));
       //postImage(channels.get(1), "Sat channel-Test");
       
       
       Core.bitwise_or(channels.get(0), channels.get(1), channels.get(2));
       //postImage(channels.get(2), "Find Battery");
       
       Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5,5));
       Imgproc.erode(frame, frame, kernel);
       Imgproc.dilate(frame, frame, kernel);
       postImage(channels.get(2), "Find Battery");
     
    }
}

import stuyvision.VisionModule;
import stuyvision.gui.VisionGui;
import org.opencv.core.Mat;

public class NoOp extends VisionModule {
    public void run(Mat frame) {
        postImage(frame, "Camera Feed");
    }
}

import stuyvision.VisionModule;
import stuyvision.ModuleRunner;
import stuyvision.capture.CaptureSource.ResizeDimension;
import stuyvision.capture.DeviceCaptureSource;
import stuyvision.capture.VideoCaptureSource;
import stuyvision.capture.ImageCaptureSource;
import stuyvision.gui.VisionGui;

public class Main {
    public static void main(String[] args) {
        ModuleRunner runner = new ModuleRunner();
        runner.addMapping(new DeviceCaptureSource(0), new Simple(), new NoOp());
        VisionGui.begin(args, runner);
    }

    private static void runSingle(String[] args, int videoN, VisionModule module) {
        ModuleRunner runner = new ModuleRunner();
        runner.addMapping(new DeviceCaptureSource(videoN), module);
        VisionGui.begin(args, runner);
    }
}

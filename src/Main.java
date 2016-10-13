import java.io.File;

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
        processSamples(runner);
        VisionGui.begin(args, runner);
    }

    public static void processCamera(ModuleRunner runner) {
        runner.addMapping(new DeviceCaptureSource(0), new Vision());
    }

    public static void processSamples(ModuleRunner runner) {
        String imagesDir = Main.class.getResource("").getPath() + "../sampleImages/";
        System.out.println("Getting images from " + imagesDir);
        File directory = new File(imagesDir);
        File[] directoryListing = directory.listFiles();
        for (int i = 0; i < directoryListing.length && i < 10; i++) {
            if (i == 1 || i == 2) {
                // There is no 1.jpg or 2.jpg in the sample images
                continue;
            }
            String path = imagesDir + directoryListing[i].getName();
            runner.addMapping(new ImageCaptureSource(path), new Vision());
        }
        String colorwheel = imagesDir + "colorwheel.png";
        runner.addMapping(new ImageCaptureSource(colorwheel), new Vision());
    }
}

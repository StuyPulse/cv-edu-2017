# cv-edu-2017

CV Education for the 2017 season

OpenCV needs to be installed at `lib/opencv-3.0.0`.

On Linux, you can install OpenCV with the
`install-opencv-unix.sh` script.  There are more detailed
instructions on this in the README of
[https://github.com/Team694/stuyvision-lib]()). Once CV is
setup, run `ant` to compile and run the project. (Apache Ant
must be installed.)

To use on Windows, see `windows-setup.md` for instructions.

# Lesson summaries

# Dec 15

In this lesson we discussed methods for smoothing out
images.

The sample images provided by FIRST for 2016 CV are quite
suboptimal in several ways.

![Sample Image 188.jpg](sampleImages/188.jpg)

You might already notice that the reflexite's coloring is
weird, and that the whole image is slightly blue. We can see
this better by displaying the hue channel:

````java
public void run(Mat frame) {
    postImage(frame, "Camera Feed");

    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

    ArrayList<Mat> channels = new ArrayList<Mat>();
    Core.split(frame, channels);

--> postImage(channels.get(0), "Hue channel");

    Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
    postImage(channels.get(0), "Hue-Filtered Frame");
}
````

We then see this:

![Hue channel](res/dec-15-hue-channel.png)

The shade of gray corresponds to the value of the hue.

Color wheel for context:

![Hue channel color wheel](res/dec-15-hue-channel-wheel.png)

(1) The hue all over the image is blue, and (2) the hue
at the goal is nasty. The reflexite may have been dirty
or damaged, but it is *not* a solid color.

These are serious problems (note that we get better images
than this from the LifeCam), and leads us to a common tool
in computer vision for improving image quality: image
smoothing.

## Smoothing an image

There are many algorithms for blurring an image. The ones we
will talk about have a consistent underlying methodology:
for every pixel in the image, look at a rectangular window
of pixels around it, and change the center pixel based on
the values of the pixels in that rectangular window
(terminology: the operation is 'convolution' of a 'kernel'
over an image; [more at
wikipedia](https://en.wikipedia.org/wiki/Kernel_(image_processing))).

Common types of blurs:

- **Box blur**: set the center pixel to the mean of the
  pixels in the window. This is the simplest blur.

- **Median blur**: set the center pixel to the median, of
  the pixels in the window.  This has more desirable
  behavior at edges.  Whereas a box blur will smear
  everything indiscriminately, a median blur can maintain
  greater sharpness around edges.  Think about how a median
  works: if two thirds of the pixels around a pixel are
  green, and 1/3 is black, the median will just be green.

- **Gaussian blur**: set the center pixel to a weighted mean
  of the pixels in the window, giving more weight to closer
  pixels. Gaussian blurring is very often used for removing
  noise/static from an image.

*(Aside: There are more, including the __bilateral filter__,
which extends a gaussian blur by also giving more weight to
pixels with a similar color. This makes bilateral filtering
quite effective in retaining edges while smoothing out an
image (if you crank up its values, you can sort of
cartoon-ify an image), but is the slowest of all of these,
and overkill for our purposes.)*

OpenCV provide the following functions for these blurs:

````java
// Blur mat in-place (overwrite the original image with
// the blurred image), with a "window" size of 3-by-3.
// The arguments to Size must be odd numbers, so that
// there is a specific center pixel of the window.
Imgproc.blur(mat, mat, new Size(3, 3));

// Here, `3` is the "window" side-length
Imgproc.medianBlur(mat, mat, 3);

// Ignore the `0`. The last two parameters (indirectly)
// determine the dimensions of the window in X and Y.
Imgproc.GaussianBlur(mat, mat, 0, 5, 5);
````

JavaDocs for these functions
[here](http://docs.opencv.org/java/3.1.0/?org/opencv/imgproc/Imgproc.html).
The [C OpenCV
docs](http://docs.opencv.org/3.1.0/d4/d13/tutorial_py_filtering.html)
on image smoothing have much more detail and rigor.


We'll apply a median blur to the hue channel of our image.
We do this, rather than applying it to the whole original
image, because we are only having problems with the hue
channel. (The value channel, for example, looks just fine.)

````java
public void run(Mat frame) {
    postImage(frame, "Camera Feed");

    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);

    ArrayList<Mat> channels = new ArrayList<Mat>();
    Core.split(frame, channels);

    postImage(channels.get(0), "Hue channel");

    // Blur the hue channel with a 5-by-5 median blur.
--> Imgproc.medianBlur(channels.get(0), channels.get(0), 5);
--> postImage(channels.get(0), "Blurred hue channel");

    Core.inRange(channels.get(0), new Scalar(minHue.value()), new Scalar(maxHue.value()), channels.get(0));
    postImage(channels.get(0), "Hue-Filtered Frame");
}
````

From this, we get:

![Hue channel blurred](res/dec-15-hue-channel-blurred.png)

Notice that the hue of the goal is now quite smooth, and
is still measurably different from the hue of the rest of
the image. As a result, our hue-filtered channel is much
more useful.


## Erode and dilate

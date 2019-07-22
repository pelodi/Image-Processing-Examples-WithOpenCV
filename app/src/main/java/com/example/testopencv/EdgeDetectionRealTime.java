package com.example.testopencv;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ColorDetection extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "ColorDetection";
    private CameraBridgeViewBase cameraBridgeViewBase;
    private CameraBridgeViewBase.CvCameraViewListener2 cameraViewListener;
    private TextView textViewValue;

    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "OpenCV loaded successfully");
                    cameraBridgeViewBase.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, ColorDetection.class);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ColorDetection.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_open_cvcamera);

        Log.d("ColorDetectionClass", "onCreate");

        cameraBridgeViewBase = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        textViewValue = findViewById(R.id.text_value);
    }

    @Override
    public void onPause() {
        super.onPause();

        //textDetectAsyncTask.cancel(true);

        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, baseLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        //textDetectAsyncTask.cancel(true);


        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }

    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final Mat color = inputFrame.rgba();
        final Mat edges = inputFrame.gray();
        Imgproc.Canny(edges,edges,10,50,3);
        /*Mat lines = new Mat();
        //*********red color extraction
        Mat mask = new Mat();
        Mat hsv = new Mat();
        Mat color_range_red = new Mat();
        //Imgproc.GaussianBlur(srcImg, blurImg, new Size(5,5), 0);
        Imgproc.cvtColor(color, hsv, Imgproc.COLOR_BGR2HSV);
        Core.inRange(hsv, new Scalar(30,150,50), new Scalar(255,255,180), color_range_red);
        Core.bitwise_and(color, color, mask, color_range_red);
        //***********convert to grayscale
        Mat gray_mask = new Mat();
        Imgproc.cvtColor(mask, gray_mask, Imgproc.COLOR_BGR2GRAY);
        //return gray_mask;
        Imgproc.HoughLinesP(gray_mask, lines, 1, Math.PI / 180, 2, 0, 0);
        int lineCount = lines.rows();
        double[] l = lines.get(0, 0);
        Point p1 = new Point(l[0], l[1]);
        Point p2 = new Point(l[2], l[3]);
        Imgproc.line(gray_mask, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        double[] slopeArray = new double[lines.rows()];
        double slope = (p2.y - p1.y) / (double) (p2.x - p1.x);
        Log.d("slope", "slope" + slope);
        Log.d("point", "p1" + p1);
        Log.d("point", "p2" + p2);
        Log.d("Lines","Line rows: " + gray_mask.row(0));
        if(slope == 0){
            lineCount++;
        }
        Log.d("Lines","Line count: " + lineCount);
        return mask;
        //*************Draw the line_picture
        /*for (int x = 0; x < line_picture.rows(); x++) {
                double[] l = line_picture.get(x, 0);
                Imgproc.line(gray_mask, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
        return gray_mask;*/
        return edges;
    }
}

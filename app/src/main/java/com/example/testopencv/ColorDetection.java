package com.example.testopencv;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class ExImage extends AppCompatActivity {

    private static final String TAG = "DENEME";
    private Button takePicture;
    private ImageView picture;
    private ImageView picture2;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        takePicture = (Button) findViewById(R.id.take_picture);
        picture = (ImageView) findViewById(R.id.picture);
        picture2 = findViewById(R.id.picture2);


        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    processImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }


    private void processImage() throws IOException {
        Mat img = null;
        Mat lines = new Mat();
        Mat hsv = new Mat();
        Mat color_range_red = new Mat();
        Mat mask = new Mat();
        try {
            img = Utils.loadResource(this, R.drawable.lines);
            //Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2GRAY);
            Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
            Core.inRange(hsv, new Scalar(0,250,250), new Scalar(5,255,255), color_range_red);
            Core.bitwise_and(img, img, mask, color_range_red);
            //***********convert to grayscale
            Mat gray_mask = new Mat();
            Imgproc.cvtColor(mask, gray_mask, Imgproc.COLOR_BGR2GRAY);
            showImg(mask);
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    private void showImg(Mat img) {

        Bitmap bm = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img, bm);
        picture2.setImageBitmap(bm);
    }
}

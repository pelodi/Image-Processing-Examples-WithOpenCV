package com.example.testopencv;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {


    private Button buttonOpenCv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonOpenCv = findViewById(R.id.button_open_cv);
        buttonOpenCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int MyVersion = Build.VERSION.SDK_INT;
                if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {


                    if (PermissionsUtil.isCameraGranted(MainActivity.this)) {


                        ColorDetection.start(MainActivity.this);

                    } else {
                        PermissionsUtil.requestCaneraPermission(MainActivity.this);
                    }

                }

            }
        });


    }


    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        if (PermissionsUtil.isRequestCodeForCamera(requestCode)) {
            if (PermissionsUtil.isCameraGranted(this)) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

                ColorDetection.start(this);


            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(this, R.string.camera_permission_denied, Toast.LENGTH_LONG).show();
            }
        }
    }


}

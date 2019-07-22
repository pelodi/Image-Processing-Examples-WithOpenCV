package com.example.testopencv;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public final class PermissionsUtil {

    private static final int REQUEST_CODE_CAMERA = 1;

    private PermissionsUtil() {
        //no-op
    }

    public static boolean isCameraGranted(@NonNull final Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCaneraPermission(@NonNull final Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
    }

    public static boolean isRequestCodeForCamera(final int requestCode) {
        return REQUEST_CODE_CAMERA == requestCode;
    }

}

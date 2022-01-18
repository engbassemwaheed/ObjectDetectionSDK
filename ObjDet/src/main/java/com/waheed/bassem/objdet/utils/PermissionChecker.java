package com.waheed.bassem.objdet.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

public class PermissionChecker {

    public static boolean checkCameraPermissions(Context context) {
        int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }
}

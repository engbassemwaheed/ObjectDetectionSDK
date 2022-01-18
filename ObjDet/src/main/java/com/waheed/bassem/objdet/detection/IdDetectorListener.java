package com.waheed.bassem.objdet.detection;

import android.graphics.Bitmap;

import com.waheed.bassem.objdet.BaseListener;

public interface IdDetectorListener extends BaseListener {

    void onIdDetected (Bitmap detectedIdBitmap);

}

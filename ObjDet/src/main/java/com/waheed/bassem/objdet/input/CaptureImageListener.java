package com.waheed.bassem.objdet.input;

import android.graphics.Bitmap;

import com.waheed.bassem.objdet.BaseListener;

public interface CaptureImageListener extends BaseListener {
    void onCaptureResult(Bitmap bitmap);
    void onError(int errorCode);
}

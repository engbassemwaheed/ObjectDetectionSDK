package com.waheed.bassem.objdet;

import android.graphics.Bitmap;

public interface IdVerificationListener extends BaseListener {

    void onVerified (Bitmap detectedIdBitmap, String ocrResult);
    void onImageCaptured (Bitmap bitmap);

}

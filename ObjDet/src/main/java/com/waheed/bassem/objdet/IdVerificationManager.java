package com.waheed.bassem.objdet;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.activity.ComponentActivity;

import com.waheed.bassem.objdet.detection.IdDetector;
import com.waheed.bassem.objdet.detection.IdDetectorListener;
import com.waheed.bassem.objdet.input.CaptureImageHandler;
import com.waheed.bassem.objdet.input.CaptureImageListener;
import com.waheed.bassem.objdet.utils.ErrorCode;

public class IdVerificationManager implements CaptureImageListener, IdDetectorListener {

    private static final String TAG = "IdVerificationManager";

    private static IdVerificationManager idVerificationManager;
    private final IdVerificationListener idVerificationListener;
    private final CaptureImageHandler captureImageHandler;
    private final IdDetector idDetector;
    private final Context context;


    private IdVerificationManager(ComponentActivity activity, IdVerificationListener idVerificationListener) {
        this.captureImageHandler = CaptureImageHandler.getInstance(activity, this);
        this.context = activity.getApplicationContext();
        this.idVerificationListener = idVerificationListener;
        this.idDetector = IdDetector.getInstance();
    }

    public static IdVerificationManager getInstance(ComponentActivity activity,
                                                    IdVerificationListener idVerificationListener) {
        if (idVerificationManager == null) {
            idVerificationManager = new IdVerificationManager(activity, idVerificationListener);
        }
        return idVerificationManager;
    }

    public void startVerification() {
        captureImageHandler.takePicture();
    }

    @Override
    public void onCaptureResult(Bitmap bitmap) {
        idVerificationListener.onImageCaptured(bitmap);
        if (bitmap != null) {
            idDetector.detectIdCardBitmap(context, bitmap, this);
        } else {
            idVerificationListener.onError(ErrorCode.ID_DETECTION_ERROR);
        }
    }

    @Override
    public void onIdDetected(Bitmap detectedIdBitmap) {
        //TODO: call the api end points to run OCR or Verification
        idVerificationListener.onVerified(detectedIdBitmap, "-");
    }

    @Override
    public void onError(int errorCode) {
        idVerificationListener.onError(errorCode);
    }
}

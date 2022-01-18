package com.waheed.bassem.objdet.input;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.waheed.bassem.objdet.utils.ErrorCode;
import com.waheed.bassem.objdet.utils.ImageEditor;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CaptureImageHandler {

    private static CaptureImageHandler captureImageHandler;
    private final CaptureImageListener captureImageListener;
    private final ActivityResultLauncher<Intent> takePictureActivityResultLauncher;
    private final ComponentActivity activity;
    private String imageFilePath;


    private CaptureImageHandler(ComponentActivity activity, CaptureImageListener captureImageListener) {
        this.activity = activity;
        this.captureImageListener = captureImageListener;
        takePictureActivityResultLauncher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::handleActivityResult);
    }

    public static CaptureImageHandler getInstance(ComponentActivity componentActivity,
                                                  CaptureImageListener captureImageListener) {
        if (captureImageHandler == null) {
            captureImageHandler = new CaptureImageHandler(componentActivity, captureImageListener);
        }
        return captureImageHandler;
    }

    public void takePicture() {
        try {
            Intent captureIntent = buildCameraIntent(activity);
            boolean canProcessCameraIntent = captureIntent.resolveActivity(activity.getPackageManager()) != null;
            if (canProcessCameraIntent) {
                takePictureActivityResultLauncher.launch(captureIntent);
            } else {
                captureImageListener.onError(ErrorCode.CAMERA_CANT_TAKE_PIC);
            }
        } catch (IOException e) {
            e.printStackTrace();
            captureImageListener.onError(ErrorCode.CAMERA_CANT_TAKE_PIC);
        }
    }

    private Intent buildCameraIntent(Activity activity) throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = createImageFile(activity);
        Uri photoURI = FileProvider.getUriForFile(activity, "com.waheed.bassem.objdet.fileprovider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        return intent;
    }

    private File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                "JPEG_" + timeStamp, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */);
        imageFilePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void handleActivityResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            if (imageFilePath != null) {
                try {
                    Bitmap bitmap = getCapturedImage(imageFilePath);
                    if (bitmap != null) {
                        captureImageListener.onCaptureResult(bitmap);
                    } else {
                        captureImageListener.onError(ErrorCode.CAMERA_IMAGE_NULL);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    captureImageListener.onError(ErrorCode.CAMERA_IMAGE_NULL);
                }
                imageFilePath = null;
            } else {
                captureImageListener.onError(ErrorCode.CAMERA_IMAGE_NULL);
            }
        } else {
            captureImageListener.onError(ErrorCode.CAMERA_IMAGE_NULL);
        }
    }

    private Bitmap getCapturedImage(String currentPhotoPath) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exifInterface = new ExifInterface(currentPhotoPath);

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return ImageEditor.rotateImage(bitmap, 90f);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return ImageEditor.rotateImage(bitmap, 180f);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return ImageEditor.rotateImage(bitmap, 270f);
            default:
                return bitmap;
        }
    }


}

package com.waheed.bassem.objdet.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import static com.waheed.bassem.objdet.utils.Constants.PREFIX;


public class DataConverter {


    public static String imageToBase64 (Bitmap bitmap, int maxSize) {
        Bitmap resizedBitmap = getResizedBitmap(bitmap, maxSize);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream); //bm is the bitmap object
        byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
        String base64String = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        String base64Fixed = base64String.replace("\n", "");

        return PREFIX + base64Fixed;
    }


    private static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}

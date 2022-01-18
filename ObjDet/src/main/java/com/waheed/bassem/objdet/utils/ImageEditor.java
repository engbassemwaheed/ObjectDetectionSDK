package com.waheed.bassem.objdet.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

public class ImageEditor {

    public static Bitmap cropImage(Bitmap bitmap, RectF rectF) {
        int x = (int) rectF.left;
        int y = (int) rectF.top;
        int width = (int) (rectF.right - rectF.left);
        int height = (int) (rectF.bottom - rectF.top);
        if (x >= 0
                && y >= 0
                && width > 0
                && height > 0
                && x + width <= bitmap.getWidth()
                && y + height <= bitmap.getHeight())
            return Bitmap.createBitmap(bitmap, x, y, width, height);
        else return bitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(
                source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true
        );
    }

    public static Bitmap cropImage(Context context,
                                   FrameLayout frameLayout,
                                   Bitmap bitmap,
                                   int rectWidthPX,
                                   int rectHeightDIP) {

        int frameWidth = frameLayout.getWidth();
        int frameHeight = frameLayout.getHeight();

        int rectHeightPX = (int) convertDpToPixel(context, rectHeightDIP);

        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();


        int rectWidthNew = (rectWidthPX * imageWidth) / frameWidth;
        int rectHeightNew = (rectHeightPX * imageHeight) / frameHeight;


        int x = (imageWidth / 2) - (rectWidthNew / 2);
        int y = (imageHeight / 2) - (rectHeightNew / 2);

        return Bitmap.createBitmap(bitmap, x, y, rectWidthNew, rectHeightNew);
    }

    private static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}

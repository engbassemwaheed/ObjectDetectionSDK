package com.waheed.bassem.objdet.detection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.waheed.bassem.objdet.utils.ErrorCode;
import com.waheed.bassem.objdet.utils.ImageEditor;

import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.vision.detector.Detection;
import org.tensorflow.lite.task.vision.detector.ObjectDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IdDetector {

    private static final String TAG = "IdDetector";

    private static final int MAX_RESULTS = 1;
    private static final float SCORE_THRESHOLD = 0.5f;
    private static final String MODEL_PATH = "salad.tflite";
    private static boolean isAvailable;
    private static IdDetector idDetector;
    private static Handler handler;

    private IdDetector() {
        isAvailable = true;
        handler = new Handler();
    }

    public static IdDetector getInstance() {
        if (idDetector == null) {
            idDetector = new IdDetector();
        }
        return idDetector;
    }

    public void detectIdCardBitmap(Context context, Bitmap bitmap, IdDetectorListener idDetectorListener) {
        if (isAvailable) {
            isAvailable = false;
        } else {
            handler.post(() -> {});
        }

        handler.post(() -> {
            try {
                Detection detection = detect(context, bitmap);
                if (detection != null) {
                    //for debugging
//            ArrayList<Detection> detectionArrayList = new ArrayList<>();
//            detectionArrayList.add(detection);
//            return drawDetectionResult(bitmap, detectionArrayList);
                    RectF rectF = detection.getBoundingBox();
                    idDetectorListener.onIdDetected(ImageEditor.cropImage(bitmap, rectF));
                } else {
                    idDetectorListener.onError(ErrorCode.ID_DETECTION_ERROR);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "detectIdCardBitmap: exception = " + e.getMessage());
                idDetectorListener.onError(ErrorCode.ID_DETECTION_ERROR);
            } finally {
                isAvailable = true;
            }
        });


    }

    private Detection detect(Context context, Bitmap bitmap) throws IOException {
        TensorImage tensorImage = TensorImage.fromBitmap(bitmap);

        ObjectDetector.ObjectDetectorOptions options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(MAX_RESULTS)
                .setScoreThreshold(SCORE_THRESHOLD)
                .build();

        ObjectDetector detector = ObjectDetector.createFromFileAndOptions(
                context,
                MODEL_PATH,
                options);

        List<Detection> resultList = detector.detect(tensorImage);
        //for debugging
//        for (Detection detection : resultList) {
//            Log.e(TAG, "detectIdCard: : ==============");
//            RectF rectF = detection.getBoundingBox();
//
//            Log.e(TAG, "detectIdCard: rectF = " + rectF);
//
//            List<Category> categoryList = detection.getCategories();
//
//            for (Category category : categoryList) {
//                Log.e(TAG, "detectIdCard: category = " + category);
//            }
//        }

        if (resultList.size() > 0) return resultList.get(0);
        else return null;
    }

//for debugging
//    private Bitmap drawDetectionResult(
//            Bitmap bitmap,
//            List<Detection> detectionResultList){
//        Bitmap outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(outputBitmap);
//        Paint pen = new Paint();
//        pen.setTextAlign(Paint.Align.LEFT);
//
//
//        for (Detection detection : detectionResultList) {
//            // draw bounding box
//            pen.setColor(Color.RED);
//            pen.setStrokeWidth(8F);
//            pen.setStyle(Paint.Style.STROKE);
//
//            RectF box = detection.getBoundingBox();
//            canvas.drawRect(box, pen);
//
//            Rect tagSize = new Rect(0, 0, 0, 0);
//
//            // calculate the right font size
//            pen.setStyle(Paint.Style.FILL_AND_STROKE);
//            pen.setColor(Color.YELLOW);
//            pen.setStrokeWidth(2F);
//
//            pen.setTextSize(96F);
//            List<Category> categoryList = detection.getCategories();
//            if (categoryList.size()>0) {
//                String name = detection.getCategories().get(0).getDisplayName();
//
//                pen.getTextBounds(name, 0, name.length(), tagSize);
//                float fontSize = pen.getTextSize() * box.width() / tagSize.width();
//
//                // adjust the font size so texts are inside the bounding box
//                if (fontSize < pen.getTextSize()) pen.setTextSize(fontSize);
//
//                float margin = (box.width() - tagSize.width()) / 2.0F;
//                if (margin < 0F) margin = 0F;
//                canvas.drawText(
//                        name, box.left + margin,
//                        box.top + tagSize.height()*(1F), pen);
//            }
//        }
//        return outputBitmap;
//    }
}

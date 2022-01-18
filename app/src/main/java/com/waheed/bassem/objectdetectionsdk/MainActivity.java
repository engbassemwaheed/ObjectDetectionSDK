package com.waheed.bassem.objectdetectionsdk;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.waheed.bassem.objdet.IdVerificationListener;
import com.waheed.bassem.objdet.IdVerificationManager;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton materialButton = findViewById(R.id.start_button);
        ImageView imageView = findViewById(R.id.result_image_view);
        TextView textView = findViewById(R.id.result_text_view);

        IdVerificationManager idVerificationManager = IdVerificationManager.getInstance(this,
                new IdVerificationListener() {
                    @Override
                    public void onVerified(Bitmap detectedIdBitmap, String ocrResult) {
                        String temp = "detected text = " + ocrResult;
                        textView.setText(temp);
                        if (detectedIdBitmap!=null) imageView.setImageBitmap(detectedIdBitmap);
                    }

                    @Override
                    public void onImageCaptured(Bitmap bitmap) {
                        String temp = "This is the captured image";
                        textView.setText(temp);
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(int errorCode) {
                        String temp = "An error occurred, error code = " + errorCode;
                        textView.setText(temp);
                    }
                });

        materialButton.setOnClickListener(v -> idVerificationManager.startVerification());

    }
}
package com.waheed.bassem.objdet.utils;

import android.util.Log;

import static com.waheed.bassem.objdet.utils.Constants.PREFIX;


public class DataValidator {

    private static final String TAG = "DataValidator";

    public static boolean validateApiKey (String apiKey) {
        if (apiKey == null) {
            Log.e(TAG, "validateApiKey: apiKey is null");
            return false;
        } else if (apiKey.isEmpty()) {
            Log.e(TAG, "validateApiKey: apiKey is empty");
            return false;
        } else {
            return true;
        }
    }
    public static boolean validateBase64Image (String base64Image) {
        if (base64Image == null) {
            Log.e(TAG, "validateBase64Image: base64Image is null");
            return false;
        } else if (base64Image.isEmpty()) {
            Log.e(TAG, "validateBase64Image: base64Image is empty");
            return false;
        } else if (!base64Image.startsWith(PREFIX)) {
            Log.e(TAG, "validateBase64Image: base64Image should start with " + PREFIX);
            return false;
        } else {
            return true;
        }
    }


}

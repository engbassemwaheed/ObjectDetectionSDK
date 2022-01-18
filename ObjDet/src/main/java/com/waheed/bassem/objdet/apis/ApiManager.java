package com.waheed.bassem.objdet.apis;


import com.waheed.bassem.objdet.utils.DataValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    private static ApiManager apiManager;
    private String apiKey;
    private final OcrAPI ocrAPI;

    private ApiManager(OcrAPI ocrAPI) {
        this.ocrAPI = ocrAPI;
    }

    public static ApiManager getInstance() {
        if (apiManager == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_OCR_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            apiManager = new ApiManager(retrofit.create(OcrAPI.class));
        }

        return apiManager;
    }

    public boolean setApiKey(String apiKey) {
        if (DataValidator.validateApiKey(apiKey)) {
            this.apiKey = apiKey;
            return true;
        } else {
            return false;
        }
    }

    public boolean parseText(String base64Image, Callback<OcrResultContainer> callback) {
        if (DataValidator.validateBase64Image(base64Image)) {
            Call<OcrResultContainer> call = ocrAPI.parseText(apiKey, base64Image);
            call.enqueue(callback);
            return true;
        }

        return false;
    }


    public int getMaxFileSize() {
        return Constants.MAX_FILE_SIZE;
    }
}

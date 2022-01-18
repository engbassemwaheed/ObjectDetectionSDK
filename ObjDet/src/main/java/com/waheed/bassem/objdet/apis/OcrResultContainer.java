package com.waheed.bassem.objdet.apis;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OcrResultContainer {

    @SerializedName("ParsedResults")
    private final ArrayList<OcrResult> parsedOCRResults;

    public OcrResultContainer(ArrayList<OcrResult> parsedOCRResults) {
        this.parsedOCRResults = parsedOCRResults;
    }

    public ArrayList<OcrResult> getParsedOCRResults() {
        return parsedOCRResults;
    }

    public ArrayList<String> getParsedText () {
        ArrayList<String> data = new ArrayList<>();
        if (parsedOCRResults != null && parsedOCRResults.size()>0) {
            for (OcrResult ocrResult : parsedOCRResults) {
                data.add(ocrResult.getParsedText());
            }
        }
        return data;
    }
}

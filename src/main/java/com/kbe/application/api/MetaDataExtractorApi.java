package com.kbe.application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class MetaDataExtractorApi {

    public MetaDataResponse getMetaDataByUrl(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://metadata-extractor.p.rapidapi.com/?url=" + url)
                .get()
                .addHeader("x-rapidapi-host", "metadata-extractor.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "7bd69c14a7mshc879deb016ad94fp13b7f4jsn3a75820ba405")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper om = new ObjectMapper();

        String jsonString = response.body().string();

        MetaDataResponse metaDataResponse = om.readValue(jsonString, MetaDataResponse.class);
        return metaDataResponse;
    }
}

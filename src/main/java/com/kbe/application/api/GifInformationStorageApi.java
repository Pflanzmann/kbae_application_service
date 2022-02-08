package com.kbe.application.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.application.model.GifInformation;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class GifInformationStorageApi {

    public GifInformation postNewGifInformation(GifInformation gifInformation) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String bodyString = objectMapper.writeValueAsString(gifInformation);

        RequestBody requestBody = RequestBody.create(bodyString, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url("http://localhost:8083/api/information")
                .post(requestBody)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        String jsonString = response.body().string();
        GifInformation gifInformationResponse = objectMapper.readValue(jsonString, GifInformation.class);

        return gifInformationResponse;
    }

    public List<GifInformation> getAllGifInformations() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8083/api/information")
                .get()
                .build();

        Response response = okHttpClient.newCall(request).execute();
        ObjectMapper om = new ObjectMapper();

        String jsonString = response.body().string();

        List<GifInformation> gifInformations = om.readValue(jsonString, new TypeReference<List<GifInformation>>() {
        });

        return gifInformations;
    }

    public GifInformation getGifInformation(UUID id) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8083/api/information/" + id)
                .get()
                .build();

        Response response = okHttpClient.newCall(request).execute();
        ObjectMapper om = new ObjectMapper();

        String jsonString = response.body().string();

        GifInformation gifInformation = om.readValue(jsonString, GifInformation.class);

        return gifInformation;
    }

    public boolean getToStartExport() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8083/api/information/export")
                .get()
                .build();

        Response response = okHttpClient.newCall(request).execute();
        return response.isSuccessful();
    }
}

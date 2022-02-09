package com.kbe.application.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.application.model.GifInformation;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class GifInformationStorageApi {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public GifInformation postNewGifInformation(GifInformation gifInformation) throws IOException {
        logger.info("Post new gif information to storage for: [{}]", gifInformation.getId());

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
        logger.info("Get all gif information from all gifs");

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
        logger.info("Get gif information for [{}]", id);

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
        logger.info("Get to start ping start importing on the storage");

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8083/api/information/export")
                .get()
                .build();

        Response response = okHttpClient.newCall(request).execute();
        return response.isSuccessful();
    }

    public boolean deleteGifInformation(UUID id) throws IOException {
        logger.info("Delete call to delete all information of gif with id: [{}]", id);

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://localhost:8083/api/information/delete")
                .delete()
                .build();

        Response response = okHttpClient.newCall(request).execute();
        return response.isSuccessful();
    }
}

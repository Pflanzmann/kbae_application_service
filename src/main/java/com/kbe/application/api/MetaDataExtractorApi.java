package com.kbe.application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.application.model.Gif;
import com.kbe.application.model.GifDetails;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MetaDataExtractorApi {

    public GifDetails getMetaDataByUrl(Gif gif) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://metadata-extractor.p.rapidapi.com/?url=" + gif.getUrl())
                .get()
                .addHeader("x-rapidapi-host", "metadata-extractor.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "7bd69c14a7mshc879deb016ad94fp13b7f4jsn3a75820ba405")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        ObjectMapper om = new ObjectMapper();

        String jsonString = response.body().string();

        MetaDataResponse metaDataResponse = om.readValue(jsonString, MetaDataResponse.class);

        return new GifDetails(
                gif.getId(),
                metaDataResponse.fileMetaData.fileSize,
                metaDataResponse.gIFMetaData.imageWidth,
                metaDataResponse.gIFMetaData.imageHeight,
                metaDataResponse.gIFMetaData.frameCount,
                metaDataResponse.gIFMetaData.duration
        );
    }
}

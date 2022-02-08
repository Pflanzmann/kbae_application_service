package com.kbe.application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.application.model.Gif;
import com.kbe.application.model.GifDetails;
import com.kbe.application.model.external.MetaDataResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("MetaDataExtractorApi")
@ConditionalOnProperty(value = "feature.debug", havingValue = "false")
public class MetaDataExtractorApi implements MetaDataExtractorApiType {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public MetaDataExtractorApi() {
        logger.info("Started DebugMataDataExtractorApi");
    }

    @Override
    public GifDetails getMetaDataByUrl(Gif gif) throws IOException {
        logger.info("Get meta data for [{}]", gif.getId());

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




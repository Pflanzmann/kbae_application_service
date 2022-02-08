package com.kbe.application.api;

import com.kbe.application.model.Gif;
import com.kbe.application.model.GifDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("MetaDataExtractorApi")
@ConditionalOnProperty(value = "feature.debug", havingValue = "true")
public class DebugMataDataExtractorApi implements MetaDataExtractorApiType {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DebugMataDataExtractorApi() {
        logger.info("Started DebugMataDataExtractorApi");
    }

    @Override
    public GifDetails getMetaDataByUrl(Gif gif) throws IOException {
        logger.info("Generate fake meta data for [{}]", gif.getId());

        return new GifDetails(
                gif.getId(),
                gif.getUrl().length(),
                gif.getUpvotes() * 10,
                gif.getDownvotes() * 10,
                gif.getId().toString().length(),
                "debug"
        );
    }
}

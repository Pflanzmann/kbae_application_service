package com.kbe.application.api;

import com.kbe.application.model.Gif;
import com.kbe.application.model.GifDetails;

import java.io.IOException;

public interface MetaDataExtractorApiType {
    GifDetails getMetaDataByUrl(Gif gif) throws IOException;
}

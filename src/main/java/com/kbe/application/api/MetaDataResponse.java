package com.kbe.application.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kbe.application.api.FileMetaData;
import com.kbe.application.api.GifMetaData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaDataResponse {
    @JsonProperty("File")
    public FileMetaData fileMetaData;
    @JsonProperty("GIF")
    public GifMetaData gIFMetaData;
}


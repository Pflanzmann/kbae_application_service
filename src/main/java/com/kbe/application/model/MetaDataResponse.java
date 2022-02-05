package com.kbe.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetaDataResponse {
    @JsonProperty("File")
    public FileMetaData fileMetaData;
    @JsonProperty("GIF")
    public GifMetaData gIFMetaData;
}


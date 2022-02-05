package com.kbe.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMetaData {
    @JsonProperty("Url")
    public String url;
    @JsonProperty("FileName")
    public String fileName;
    @JsonProperty("FileSize")
    public int fileSize;
    @JsonProperty("FileModifyDate")
    public String fileModifyDate;
    @JsonProperty("FileAccessDate")
    public String fileAccessDate;
    @JsonProperty("FileInodeChangeDate")
    public String fileInodeChangeDate;
    @JsonProperty("FileType")
    public String fileType;
    @JsonProperty("FileTypeExtension")
    public String fileTypeExtension;
    @JsonProperty("MIMEType")
    public String mIMEType;
}

package com.kbe.application.model;

import java.util.UUID;

public class GifDetails {

    private UUID id;
    private int fileSize;
    private int imageWidth;
    private int imageHeight;
    private int frameCount;
    private String duration;

    public GifDetails(UUID id, int fileSize, int imageWidth, int imageHeight, int frameCount, String duration) {
        this.id = id;
        this.fileSize = fileSize;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.frameCount = frameCount;
        this.duration = duration;
    }

    public GifDetails() {
    }

    public UUID getId() {
        return id;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public String getDuration() {
        return duration;
    }
}

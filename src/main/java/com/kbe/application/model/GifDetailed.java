package com.kbe.application.model;

public class GifDetailed extends Gif {
    private int fileSize;
    private int imageWidth;
    private int imageHeight;
    private int frameCount;
    private String duration;

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

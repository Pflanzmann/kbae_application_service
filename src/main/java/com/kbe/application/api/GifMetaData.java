package com.kbe.application.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GifMetaData {
    @JsonProperty("GIFVersion")
    public String gIFVersion;
    @JsonProperty("ImageWidth")
    public int imageWidth;
    @JsonProperty("ImageHeight")
    public int imageHeight;
    @JsonProperty("HasColorMap")
    public String hasColorMap;
    @JsonProperty("ColorResolutionDepth")
    public int colorResolutionDepth;
    @JsonProperty("BitsPerPixel")
    public int bitsPerPixel;
    @JsonProperty("BackgroundColor")
    public int backgroundColor;
    @JsonProperty("AnimationIterations")
    public String animationIterations;
    @JsonProperty("FrameCount")
    public int frameCount;
    @JsonProperty("Duration")
    public String duration;
}

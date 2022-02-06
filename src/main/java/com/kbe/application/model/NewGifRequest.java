package com.kbe.application.model;

public class NewGifRequest {

    private String url;
    private String title;
    private String author;
    private String description;
    private String topic;

    public NewGifRequest(String url, String title, String author, String description, String topic) {
        this.url = url;
        this.title = title;
        this.author = author;
        this.description = description;
        this.topic = topic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String pictureUrl) {
        this.url = pictureUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}

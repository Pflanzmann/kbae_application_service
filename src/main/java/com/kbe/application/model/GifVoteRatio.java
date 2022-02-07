package com.kbe.application.model;

import java.util.UUID;

public class GifVoteRatio {
    private UUID id;
    private float upvoteRate;
    private float downvoteRate;

    public GifVoteRatio(UUID id, float upvoteRate, float downvoteRate) {
        this.id = id;
        this.upvoteRate = upvoteRate;
        this.downvoteRate = downvoteRate;
    }

    public UUID getId() {
        return id;
    }

    public float getUpvoteRate() {
        return upvoteRate;
    }

    public float getDownvoteRate() {
        return downvoteRate;
    }
}

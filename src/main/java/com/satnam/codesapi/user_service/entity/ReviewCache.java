package com.satnam.codesapi.user_service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.io.Serializable;

@RedisHash("ReviewCache") // Redis will store with this key prefix
public class ReviewCache implements Serializable {

    @Id
    private String id; // reviewId or restaurantId
    private String reviewText;
    private int rating;
    private String userId;

    public ReviewCache() {}

    public ReviewCache(String id, String reviewText, int rating, String userId) {
        this.id = id;
        this.reviewText = reviewText;
        this.rating = rating;
        this.userId = userId;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}

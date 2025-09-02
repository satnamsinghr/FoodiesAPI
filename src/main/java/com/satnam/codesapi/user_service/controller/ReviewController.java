package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.entity.Review;
import com.satnam.codesapi.user_service.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ✅ Create a new review
    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }

    // ✅ Get reviews for a food item
    @GetMapping("/food/{foodItemId}")
    public List<Review> getReviewsByFoodItem(@PathVariable Long foodItemId) {
        return reviewService.getReviewsByFoodItem(foodItemId);
    }

    // ✅ Get all reviews (optional)
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }
}

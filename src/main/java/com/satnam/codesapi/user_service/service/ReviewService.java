package com.satnam.codesapi.user_service.service;

import com.satnam.codesapi.user_service.entity.Review;
import com.satnam.codesapi.user_service.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByFoodItem(Long foodItemId) {
        return reviewRepository.findByFoodItemId(foodItemId);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }
}

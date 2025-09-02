package com.satnam.codesapi.user_service.repository;

import com.satnam.codesapi.user_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFoodItemId(Long foodItemId);
}

package com.satnam.codesapi.user_service.repository;

import com.satnam.codesapi.user_service.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    List<FoodItem> findAllByRestaurant_Id(Long restaurantId);

    void deleteAllByRestaurant_Id(Long restaurantId);
}

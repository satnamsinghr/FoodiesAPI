package com.satnam.codesapi.user_service.service;

import com.satnam.codesapi.user_service.entity.FoodItem;
import com.satnam.codesapi.user_service.repository.FoodItemRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final FoodItemRepository foodItemRepository;

    public MenuService(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    // ✅ Cache menu items by restaurantId
    @Cacheable(value = "menus", key = "#restaurantId")
    public List<FoodItem> getMenuByRestaurant(Long restaurantId) {
        System.out.println("👉 Fetching menu from DB for restaurantId=" + restaurantId);
        return foodItemRepository.findAllByRestaurant_Id(restaurantId);
    }

    // ✅ Clear cache if menu is updated
    @CacheEvict(value = "menus", key = "#restaurantId")
    public void clearMenuCache(Long restaurantId) {
        System.out.println("👉 Cache cleared for restaurantId=" + restaurantId);
    }
}

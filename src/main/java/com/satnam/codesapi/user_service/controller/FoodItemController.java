package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.entity.FoodItem;
import com.satnam.codesapi.user_service.service.FoodItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-items") // clearer & consistent path
public class FoodItemController {

    private final FoodItemService foodItemService;

    public FoodItemController(FoodItemService foodItemService) {
        this.foodItemService = foodItemService;
    }

    // ✅ Get all food items
    @GetMapping
    public List<FoodItem> getAllFoodItems() {
        return foodItemService.getAllFoodItems();
    }

    // ✅ Get single food item by ID
    @GetMapping("/{id}")
    public FoodItem getFoodItemById(@PathVariable Long id) {
        return foodItemService.getFoodItemById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found with id " + id));
    }

    // ✅ Create new food item
    @PostMapping
    public FoodItem createFoodItem(@RequestBody FoodItem foodItem) {
        return foodItemService.saveFoodItem(foodItem);
    }

    // ✅ Update existing food item
    @PutMapping("/{id}")
    public FoodItem updateFoodItem(@PathVariable Long id, @RequestBody FoodItem foodItem) {
        foodItem.setId(id);
        return foodItemService.saveFoodItem(foodItem);
    }

    // ✅ Delete food item
    @DeleteMapping("/{id}")
    public void deleteFoodItem(@PathVariable Long id) {
        foodItemService.deleteFoodItem(id);
    }
}

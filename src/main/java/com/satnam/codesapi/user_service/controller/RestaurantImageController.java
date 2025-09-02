package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.entity.FoodItem;
import com.satnam.codesapi.user_service.repository.FoodItemRepository;
import com.satnam.codesapi.user_service.service.S3Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantImageController {

    private final S3Service s3Service;
    private final FoodItemRepository foodItemRepository;

    public RestaurantImageController(S3Service s3Service, FoodItemRepository foodItemRepository) {
        this.s3Service = s3Service;
        this.foodItemRepository = foodItemRepository;
    }

    // ============================
    // Upload image for a FoodItem
    // ============================
    @PostMapping("/{restaurantId}/food-items/{foodItemId}/image")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long restaurantId,
            @PathVariable Long foodItemId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            Optional<FoodItem> optionalFoodItem = foodItemRepository.findById(foodItemId);
            if (optionalFoodItem.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Food item not found");
            }

            FoodItem foodItem = optionalFoodItem.get();

            // Build S3 key: restaurants/{restaurantId}/food-items/{foodItemId}/{filename}
            String key = "restaurants/" + restaurantId + "/food-items/" + foodItemId + "/" + file.getOriginalFilename();

            // Upload file to S3
            s3Service.uploadFile(key, file.getInputStream(), file.getSize(), file.getContentType());

            // Get S3 URL
            URL fileUrl = s3Service.getFileUrl(key);

            // Save URL in database
            foodItem.setImageUrl(fileUrl.toString());
            foodItemRepository.save(foodItem);

            return ResponseEntity.ok(fileUrl.toString());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    // ============================
    // Delete image for a FoodItem
    // ============================
    @DeleteMapping("/{restaurantId}/food-items/{foodItemId}/image")
    public ResponseEntity<String> deleteImage(
            @PathVariable Long restaurantId,
            @PathVariable Long foodItemId,
            @RequestParam String filename
    ) {
        Optional<FoodItem> optionalFoodItem = foodItemRepository.findById(foodItemId);
        if (optionalFoodItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Food item not found");
        }

        FoodItem foodItem = optionalFoodItem.get();

        // Build S3 key
        String key = "restaurants/" + restaurantId + "/food-items/" + foodItemId + "/" + filename;

        // Delete file from S3
        s3Service.deleteFile(key);

        // Remove URL from database
        foodItem.setImageUrl(null);
        foodItemRepository.save(foodItem);

        return ResponseEntity.ok("File deleted successfully");
    }
}

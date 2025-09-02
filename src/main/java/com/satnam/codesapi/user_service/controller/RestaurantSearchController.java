package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.model.RestaurantDocument;
import com.satnam.codesapi.user_service.service.RedisService;
import com.satnam.codesapi.user_service.service.RestaurantSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class RestaurantSearchController {

    private final RestaurantSearchService restaurantSearchService;
    private final RedisService redisService;

    // Store any payload into Redis
    @PostMapping("/redis/set/{key}")
    public ResponseEntity<?> setRedis(
            @PathVariable String key,
            @RequestBody Map<String, Object> payload,   // changed from Object → Map
            @RequestParam(value = "ttl", required = false) Long ttlSeconds
    ) {
        if (ttlSeconds != null && ttlSeconds > 0) {
            redisService.setValue(key, payload, Duration.ofSeconds(ttlSeconds));
        } else {
            redisService.setValue(key, payload);
        }
        return ResponseEntity.ok(Map.of("key", key, "stored", true, "ttlSeconds", ttlSeconds));
    }

    // Retrieve Redis value by key
    @GetMapping("/redis/get/{key}")
    public ResponseEntity<?> getRedis(@PathVariable String key) {
        Object value = redisService.getValue(key);
        if (value == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(value);
    }

    // Delete key from Redis
    @DeleteMapping("/redis/delete/{key}")
    public ResponseEntity<?> deleteRedis(@PathVariable String key) {
        redisService.deleteValue(key);
        return ResponseEntity.noContent().build();
    }

    // Get restaurant either from Redis cache or from DB/Elasticsearch
    @GetMapping("/restaurants/{id}/cached")
    public ResponseEntity<?> getRestaurantCached(@PathVariable String id) {
        String redisKey = "restaurant:" + id;

        Object cached = redisService.getValue(redisKey);
        if (cached != null) {
            return ResponseEntity.ok(cached);
        }

        Optional<RestaurantDocument> found = restaurantSearchService.findById(id);
        if (found.isPresent()) {
            // cache for 10 minutes using Duration
            redisService.setValue(redisKey, found.get(), Duration.ofMinutes(10));
            return ResponseEntity.ok(found.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

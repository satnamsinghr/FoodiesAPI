package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.entity.Restaurant;
import com.satnam.codesapi.user_service.model.RestaurantDocument;
import com.satnam.codesapi.user_service.service.RestaurantService;
import com.satnam.codesapi.user_service.service.RestaurantSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantSearchService restaurantSearchService;

    public RestaurantController(RestaurantService restaurantService,
                                RestaurantSearchService restaurantSearchService) {
        this.restaurantService = restaurantService;
        this.restaurantSearchService = restaurantSearchService;
    }

    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(restaurant);
    }

    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant saved = restaurantService.saveRestaurant(restaurant);
        restaurantSearchService.saveRestaurantDocument(saved);
        return saved;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Restaurant> updateRestaurant(@PathVariable Long id,
                                                       @RequestBody Restaurant restaurant) {
        Restaurant existing = restaurantService.getRestaurantById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        restaurant.setId(id);
        Restaurant updated = restaurantService.saveRestaurant(restaurant);
        restaurantSearchService.saveRestaurantDocument(updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        Restaurant existing = restaurantService.getRestaurantById(id);
        if (existing == null) return ResponseEntity.notFound().build();

        restaurantService.deleteRestaurant(id);
        restaurantSearchService.deleteRestaurantDocument(String.valueOf(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<RestaurantDocument> searchRestaurants(@RequestParam String name) {
        return restaurantSearchService.searchByName(name);
    }

    @GetMapping("/search/cuisine")
    public List<RestaurantDocument> searchByCuisine(@RequestParam String cuisine) {
        return restaurantSearchService.searchByCuisine(cuisine);
    }

    @GetMapping("/es/{id}")
    public ResponseEntity<RestaurantDocument> getByElasticsearchId(@PathVariable String id) {
        Optional<RestaurantDocument> docOpt = restaurantSearchService.findById(id);
        if (docOpt.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(docOpt.get());
    }
}

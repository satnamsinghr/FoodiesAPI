package com.satnam.codesapi.user_service.service;

import com.satnam.codesapi.user_service.entity.Restaurant;
import com.satnam.codesapi.user_service.model.RestaurantDocument;
import com.satnam.codesapi.user_service.repository.RestaurantSearchRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantSearchService {

    private final RestaurantSearchRepository repository;

    public RestaurantSearchService(RestaurantSearchRepository repository) {
        this.repository = repository;
    }

    // Save/update restaurant in Elasticsearch
    public RestaurantDocument saveRestaurantDocument(Restaurant restaurant) {
        RestaurantDocument doc = RestaurantDocument.builder()
                .id(String.valueOf(restaurant.getId()))
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .cuisine("") // default empty
                .rating(0.0) // default rating
                .build();
        return repository.save(doc);
    }

    // Delete restaurant in Elasticsearch
    public void deleteRestaurantDocument(String id) {
        repository.deleteById(id);
    }

    // Search by name
    public List<RestaurantDocument> searchByName(String name) {
        return repository.findByName(name);
    }

    // Search by cuisine
    public List<RestaurantDocument> searchByCuisine(String cuisine) {
        return repository.findByCuisine(cuisine);
    }

    // Find by ID
    public Optional<RestaurantDocument> findById(String id) {
        return repository.findById(id);
    }
}

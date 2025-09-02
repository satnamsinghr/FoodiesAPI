package com.satnam.codesapi.user_service.service;

import com.satnam.codesapi.user_service.entity.Restaurant;
import com.satnam.codesapi.user_service.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Cacheable(value = "restaurants")
    public List<Restaurant> getAllRestaurants() {
        System.out.println("👉 Fetching restaurants from DB");
        return restaurantRepository.findAll();
    }

    @Cacheable(value = "restaurant", key = "#id")
    public Restaurant getRestaurantById(Long id) {
        System.out.println("👉 Fetching restaurant from DB for id=" + id);
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        return restaurant.orElse(null);
    }

    @CacheEvict(value = { "restaurants", "restaurant" }, allEntries = true)
    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @CacheEvict(value = { "restaurants", "restaurant" }, allEntries = true)
    public void deleteRestaurant(Long id) {
        restaurantRepository.deleteById(id);
    }
}

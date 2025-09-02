package com.satnam.codesapi.user_service.repository;

import com.satnam.codesapi.user_service.model.FoodItemDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodItemSearchRepository extends ElasticsearchRepository<FoodItemDocument, String> {
    List<FoodItemDocument> findByName(String name);
    List<FoodItemDocument> findByRestaurantId(String restaurantId);
}

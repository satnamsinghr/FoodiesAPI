package com.satnam.codesapi.user_service.repository;

import com.satnam.codesapi.user_service.model.RestaurantDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RestaurantSearchRepository extends ElasticsearchRepository<RestaurantDocument, String> {
    List<RestaurantDocument> findByName(String name);
    List<RestaurantDocument> findByCuisine(String cuisine);
}

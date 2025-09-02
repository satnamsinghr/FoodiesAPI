package com.satnam.codesapi.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "food_items")
public class FoodItemDocument {

    @Id
    private String id;            // store JPA id as string

    private String name;

    private Double price;

    private String description;

    private String imageUrl;

    private String restaurantId;  // reference to Restaurant id (string)
}

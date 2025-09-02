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
@Document(indexName = "restaurants")
public class RestaurantDocument {

    @Id
    private String id;

    private String name;

    private String address;

    @Builder.Default
    private String cuisine = "";

    @Builder.Default
    private double rating = 0.0;
}

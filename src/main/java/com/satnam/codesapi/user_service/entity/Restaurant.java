package com.satnam.codesapi.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("restaurant")
    private List<FoodItem> foodItems = new ArrayList<>();

    public Restaurant() {}

    public Restaurant(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<FoodItem> getFoodItems() { return foodItems; }
    public void setFoodItems(List<FoodItem> foodItems) { this.foodItems = foodItems; }

    // convenience helpers
    public void addFoodItem(FoodItem item) {
        foodItems.add(item);
        item.setRestaurant(this);
    }

    public void removeFoodItem(FoodItem item) {
        foodItems.remove(item);
        item.setRestaurant(null);
    }
}

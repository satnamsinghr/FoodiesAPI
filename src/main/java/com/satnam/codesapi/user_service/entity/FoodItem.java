package com.satnam.codesapi.user_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_items")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private String description;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @JsonIgnoreProperties("foodItems")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("foodItem")
    private List<Review> reviews = new ArrayList<>();

    public FoodItem() {}

    public FoodItem(String name, String description, String imageUrl, Restaurant restaurant) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.restaurant = restaurant;
    }

    public FoodItem(String name, Double price, String description, String imageUrl, Restaurant restaurant) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.restaurant = restaurant;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Restaurant getRestaurant() { return restaurant; }
    public void setRestaurant(Restaurant restaurant) { this.restaurant = restaurant; }
    public List<Review> getReviews() { return reviews; }
    public void setReviews(List<Review> reviews) { this.reviews = reviews; }

    public void addReview(Review review) {
        reviews.add(review);
        review.setFoodItem(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setFoodItem(null);
    }
}

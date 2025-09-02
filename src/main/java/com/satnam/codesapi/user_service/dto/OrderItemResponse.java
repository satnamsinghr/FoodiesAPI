package com.satnam.codesapi.user_service.dto;

public class OrderItemResponse {
    private Long foodItemId;
    private String foodItemName;
    private int quantity;
    private double price;

    // constructor
    public OrderItemResponse(Long foodItemId, String foodItemName, int quantity, double price) {
        this.foodItemId = foodItemId;
        this.foodItemName = foodItemName;
        this.quantity = quantity;
        this.price = price;
    }

    // getters
    public Long getFoodItemId() { return foodItemId; }
    public String getFoodItemName() { return foodItemName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}

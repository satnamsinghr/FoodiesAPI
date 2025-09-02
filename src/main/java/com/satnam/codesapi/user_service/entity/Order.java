package com.satnam.codesapi.user_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // which user placed the order
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // items in the order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private Double totalAmount;
    private String status; // e.g., NEW, PROCESSING, COMPLETED
    private LocalDateTime createdAt;

    public Order() {}

    public Order(User user) {
        this.user = user;
        this.status = "NEW";
        this.createdAt = LocalDateTime.now();
    }

    // helper
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

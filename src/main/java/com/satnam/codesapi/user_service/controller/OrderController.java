package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.dto.OrderRequest;
import com.satnam.codesapi.user_service.dto.OrderResponse;
import com.satnam.codesapi.user_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping
    public List<OrderResponse> getAllOrders() { return orderService.getAllOrders(); }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) { return orderService.getOrderById(id); }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse saved = orderService.createOrder(request);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}

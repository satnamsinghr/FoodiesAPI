package com.satnam.codesapi.user_service.controller;

import com.satnam.codesapi.user_service.entity.Order;
import com.satnam.codesapi.user_service.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test-orders")
public class OrderStatusTestController {

    private final OrderService orderService;

    public OrderStatusTestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/broadcast/{orderId}")
    public void testBroadcast(@PathVariable Long orderId) {
        // ✅ fetch the order entity from service
        Order order = orderService.getOrderEntity(orderId);

        // ✅ broadcast via service
        orderService.broadcastOrderStatus(order);
    }
}

package com.satnam.codesapi.user_service.service;

import com.satnam.codesapi.user_service.dto.*;
import com.satnam.codesapi.user_service.entity.*;
import com.satnam.codesapi.user_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;

    // ✅ CREATE ORDER
    public OrderResponse createOrder(OrderRequest request) {
        log.info("Creating new order for userId={}", request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    log.error("User not found: userId={}", request.getUserId());
                    return new RuntimeException("User not found");
                });

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PLACED");

        // convert request items → entity items
        for (OrderItemRequest itemReq : request.getItems()) {
            FoodItem foodItem = foodItemRepository.findById(itemReq.getFoodItemId())
                    .orElseThrow(() -> {
                        log.error("Food item not found: foodItemId={}", itemReq.getFoodItemId());
                        return new RuntimeException("Food item not found");
                    });

            OrderItem orderItem = new OrderItem();
            orderItem.setFoodItem(foodItem);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setOrder(order);

            order.getItems().add(orderItem);
        }

        Order saved = orderRepository.save(order);

        log.info("Order created successfully: orderId={}, status={}", saved.getId(), saved.getStatus());

        // Broadcast status update
        broadcastOrderStatus(saved);

        return mapToResponse(saved);
    }

    // ✅ GET ALL
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ GET ONE (response)
    public OrderResponse getOrderById(Long orderId) {
        log.info("Fetching order by id={}", orderId);
        Order order = getOrderEntity(orderId);
        return mapToResponse(order);
    }

    // ✅ GET ONE (entity, for internal use)
    public Order getOrderEntity(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found: orderId={}", orderId);
                    return new RuntimeException("Order not found");
                });
    }

    // ✅ DELETE
    public void deleteOrder(Long orderId) {
        log.warn("Deleting order: orderId={}", orderId);
        orderRepository.deleteById(orderId);
    }

    // ✅ BROADCAST to WebSocket (for now just log)
    public void broadcastOrderStatus(Order order) {
        log.info("Broadcasting order update: orderId={}, status={}", order.getId(), order.getStatus());
    }

    // ✅ MAP ENTITY → RESPONSE
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .userName(order.getUser().getName())
                .items(order.getItems().stream()
                        .map(i -> OrderItemDTO.builder()
                                .foodItemName(i.getFoodItem().getName())
                                .quantity(i.getQuantity())
                                .price(i.getFoodItem().getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}

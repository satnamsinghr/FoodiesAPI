package com.satnam.codesapi.user_service.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class OrderWebSocketController {

    // ✅ When client sends to /app/orderStatus → broadcast to /topic/status
    @MessageMapping("/orderStatus")
    @SendTo("/topic/status")
    public String sendOrderStatus(String status) {
        return "Order status: " + status;
    }
}

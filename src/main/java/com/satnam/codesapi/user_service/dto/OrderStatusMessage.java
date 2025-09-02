package com.satnam.codesapi.user_service.dto;

public class OrderStatusMessage {
    private Long orderId;
    private String status;

    public OrderStatusMessage() {}

    public OrderStatusMessage(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

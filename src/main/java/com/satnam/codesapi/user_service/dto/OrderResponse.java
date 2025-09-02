package com.satnam.codesapi.user_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String status;
    private String userName;
    private List<OrderItemDTO> items;
}

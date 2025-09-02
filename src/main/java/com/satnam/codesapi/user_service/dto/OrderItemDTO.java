package com.satnam.codesapi.user_service.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private String foodItemName;
    private Integer quantity;
    private Double price;
}

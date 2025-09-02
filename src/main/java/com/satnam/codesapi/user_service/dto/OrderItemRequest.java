package com.satnam.codesapi.user_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {
    @NotNull
    private Long foodItemId;

    @Min(1)
    private int quantity;
}

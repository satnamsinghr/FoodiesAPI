package com.satnam.codesapi.user_service.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private String status;
    private String userName;
    private List<OrderItemDTO> items;
}

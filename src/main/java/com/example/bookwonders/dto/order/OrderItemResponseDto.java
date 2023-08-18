package com.example.bookwonders.dto.order;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Long id;
    private Long bookId;
    private Integer quantity;
}

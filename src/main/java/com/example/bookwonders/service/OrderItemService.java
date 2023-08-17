package com.example.bookwonders.service;

import com.example.bookwonders.dto.order.OrderItemResponseDto;
import com.example.bookwonders.model.OrderItem;

public interface OrderItemService {
    OrderItemResponseDto findOrderItemByOrderIdAndId(Long orderId, Long itemId);

    OrderItem save(OrderItem orderItem);
}

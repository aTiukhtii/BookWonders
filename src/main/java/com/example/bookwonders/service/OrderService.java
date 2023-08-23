package com.example.bookwonders.service;

import com.example.bookwonders.dto.order.OrderItemResponseDto;
import com.example.bookwonders.dto.order.OrderResponseDto;
import com.example.bookwonders.dto.order.PlaceOrderDto;
import com.example.bookwonders.dto.order.UpdateOrderStatusDto;
import com.example.bookwonders.model.ShoppingCart;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(PlaceOrderDto placeOrderDto);

    void updateOrderStatus(Long orderId, UpdateOrderStatusDto updateOrderStatusDto);

    List<OrderResponseDto> getOrderHistory(Pageable pageable);

    List<OrderItemResponseDto> getOrderItems(Long orderId);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);

    void completePurchase(ShoppingCart shoppingCart);
}

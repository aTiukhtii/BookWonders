package com.example.bookwonders.service.impl;

import com.example.bookwonders.dto.order.OrderItemResponseDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.mapper.OrderItemMapper;
import com.example.bookwonders.model.OrderItem;
import com.example.bookwonders.repository.order.OrderItemRepository;
import com.example.bookwonders.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderItemResponseDto findOrderItemByOrderIdAndId(Long orderId, Long itemId) {
        return orderItemMapper.toDto(orderItemRepository
                .findOrderItemByOrderIdAndId(orderId, itemId).orElseThrow(() ->
                        new EntityNotFoundException("can't find item with order id: "
                        + orderId + " and item id: " + itemId)));
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}

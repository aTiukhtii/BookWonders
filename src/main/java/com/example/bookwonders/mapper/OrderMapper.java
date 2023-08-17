package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.order.OrderResponseDto;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.Order;
import com.example.bookwonders.model.ShoppingCart;
import com.example.bookwonders.model.Status;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public abstract class OrderMapper {
    @Autowired
    private OrderItemMapper orderItemMapper;

    @Mapping(target = "id", ignore = true)
    public abstract Order toOrderFromCart(ShoppingCart shoppingCart);

    @Mapping(target = "orderItems", ignore = true)
    public abstract OrderResponseDto toDto(Order order);

    @AfterMapping
    public void setOrderInfo(@MappingTarget Order order, ShoppingCart shoppingCart) {
        order.setTotal(getTotal(shoppingCart));
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
    }

    @AfterMapping
    public void setOrderItemsToDto(@MappingTarget OrderResponseDto orderResponseDto, Order order) {
        orderResponseDto.setOrderItems(order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet()));
    }

    @AfterMapping
    public void setUserIdToDto(@MappingTarget OrderResponseDto orderResponseDto, Order order) {
        orderResponseDto.setUserId(order.getUser().getId());
    }

    private BigDecimal getTotal(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems()
                .stream()
                .map(CartItem::getBook)
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

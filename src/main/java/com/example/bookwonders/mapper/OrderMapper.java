package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.order.OrderResponseDto;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.Order;
import com.example.bookwonders.model.ShoppingCart;
import java.math.BigDecimal;
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

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "userId", source = "order.user.id")
    public abstract OrderResponseDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "status", expression = "java(com.example.bookwonders.model.Status.PENDING)")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    public abstract Order toOrderFromCart(ShoppingCart shoppingCart);

    @AfterMapping
    public void setOrderInfo(@MappingTarget Order order, ShoppingCart shoppingCart) {
        order.setTotal(getTotal(shoppingCart));
    }

    @AfterMapping
    public void setOrderItemsToDto(@MappingTarget OrderResponseDto orderResponseDto, Order order) {
        orderResponseDto.setOrderItems(order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet()));
    }

    private BigDecimal getTotal(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems()
                .stream()
                .map(CartItem::getBook)
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

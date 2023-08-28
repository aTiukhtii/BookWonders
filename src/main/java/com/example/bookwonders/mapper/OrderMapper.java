package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.order.OrderResponseDto;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.Order;
import com.example.bookwonders.model.ShoppingCart;
import java.math.BigDecimal;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapperImpl.class)
public interface OrderMapper {

    @Mapping(target = "orderItems", source = "orderItems")
    @Mapping(target = "userId", source = "order.user.id")
    OrderResponseDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "status", expression = "java(com.example.bookwonders.model.Status.PENDING)")
    @Mapping(target = "orderDate", expression = "java(java.time.LocalDateTime.now())")
    Order toOrderFromCart(ShoppingCart shoppingCart);

    @AfterMapping
    default void setOrderTotal(@MappingTarget Order order, ShoppingCart shoppingCart) {
        order.setTotal(getTotal(shoppingCart));
    }

    private BigDecimal getTotal(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems()
                .stream()
                .map(CartItem::getBook)
                .map(Book::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

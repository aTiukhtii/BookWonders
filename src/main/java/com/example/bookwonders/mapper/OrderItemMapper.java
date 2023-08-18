package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.order.OrderItemResponseDto;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public abstract class OrderItemMapper {
    @Mapping(target = "bookId", source = "orderItem.book.id")
    public abstract OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "deleted", ignore = true)
    public abstract OrderItem cartItemToOrderItem(CartItem cartItem, Book book);
}

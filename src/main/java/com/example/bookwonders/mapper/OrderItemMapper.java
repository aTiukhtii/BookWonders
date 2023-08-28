package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.order.OrderItemResponseDto;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "orderItem.book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem cartItemToOrderItem(CartItem cartItem, Book book);
}

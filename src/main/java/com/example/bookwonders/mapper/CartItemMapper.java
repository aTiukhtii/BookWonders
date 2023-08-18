package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.dto.cart.CartItemResponseDto;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
@RequiredArgsConstructor
public abstract class CartItemMapper {
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    public abstract CartItem toModel(AddCartItemRequestDto requestDto, Book book,
                                     ShoppingCart shoppingCart);

    @Mapping(target = "bookId", source = "cartItem.book.id")
    @Mapping(target = "bookTitle", source = "cartItem.book.title")
    public abstract CartItemResponseDto toDto(CartItem cartItem);
}

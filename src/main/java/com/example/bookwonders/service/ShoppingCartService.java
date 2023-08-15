package com.example.bookwonders.service;

import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.dto.cart.ShoppingCartResponseDto;
import com.example.bookwonders.dto.cart.UpdateBookQuantityInCartDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    ShoppingCartResponseDto addCartItem(AddCartItemRequestDto requestDto);

    ShoppingCartResponseDto updateQuantityOfBooks(Long cartItemId,
                                                  UpdateBookQuantityInCartDto quantity);

    ShoppingCartResponseDto deleteCartItem(Long cartItemId);
}

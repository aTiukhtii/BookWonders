package com.example.bookwonders.service;

import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.model.CartItem;

public interface CartItemService {
    CartItem save(AddCartItemRequestDto requestDto);

    CartItem getById(Long id);

    void delete(Long cartItemId);

    void deleteAllFromCart();
}

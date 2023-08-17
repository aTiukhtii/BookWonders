package com.example.bookwonders.service;

import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.dto.cart.ShoppingCartResponseDto;
import com.example.bookwonders.dto.cart.UpdateBookQuantityInCartDto;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.ShoppingCart;
import java.util.Set;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    ShoppingCartResponseDto addCartItem(AddCartItemRequestDto requestDto);

    ShoppingCartResponseDto updateQuantityOfBooks(Long cartItemId,
                                                  UpdateBookQuantityInCartDto quantity);

    ShoppingCartResponseDto deleteCartItem(Long cartItemId);

    ShoppingCart getShoppingCartModel();

    void completePurchase(Set<CartItem> cartItems);
}

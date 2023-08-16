package com.example.bookwonders.dto.cart;

import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}

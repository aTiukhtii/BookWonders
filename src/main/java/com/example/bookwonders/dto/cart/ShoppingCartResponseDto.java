package com.example.bookwonders.dto.cart;

import java.util.List;
import lombok.Data;

@Data
public class ShoppingCartResponseDto {
    private Long userId;
    private List<CartItemResponseDto> cartItems;
}

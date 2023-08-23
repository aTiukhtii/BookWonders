package com.example.bookwonders.dto.cart;

import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartResponseDto {
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}

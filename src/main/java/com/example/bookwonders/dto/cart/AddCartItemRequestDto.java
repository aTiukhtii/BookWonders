package com.example.bookwonders.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddCartItemRequestDto {
    @NotBlank
    @Positive
    private Long bookId;
    @NotBlank
    @Positive
    private int quantity;
}

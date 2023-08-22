package com.example.bookwonders.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AddCartItemRequestDto {
    @NotNull
    @Positive
    private Long bookId;
    @NotNull
    @Positive
    private Integer quantity;
}

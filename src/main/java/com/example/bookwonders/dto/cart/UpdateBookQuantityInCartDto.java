package com.example.bookwonders.dto.cart;

import jakarta.validation.constraints.Positive;

public record UpdateBookQuantityInCartDto(@Positive int quantity) {
}

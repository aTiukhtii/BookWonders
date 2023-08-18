package com.example.bookwonders.dto.order;

import com.example.bookwonders.model.Status;
import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatusDto(@NotBlank Status status) {
}

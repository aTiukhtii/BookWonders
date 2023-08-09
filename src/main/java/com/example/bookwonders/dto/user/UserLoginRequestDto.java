package com.example.bookwonders.dto.user;

import com.example.bookwonders.lib.PasswordValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @Size(min = 7, max = 25)
        @Email
        String email,
        @PasswordValidator
        String password
) {
}

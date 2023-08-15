package com.example.bookwonders.service;

import com.example.bookwonders.dto.user.UserRegistrationRequestDto;
import com.example.bookwonders.dto.user.UserResponseDto;
import com.example.bookwonders.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    User getUser();
}

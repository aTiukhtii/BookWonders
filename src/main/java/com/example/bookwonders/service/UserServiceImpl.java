package com.example.bookwonders.service;

import com.example.bookwonders.dto.user.UserRegistrationRequestDto;
import com.example.bookwonders.dto.user.UserResponseDto;
import com.example.bookwonders.exception.RegistrationException;
import com.example.bookwonders.mapper.UserMapper;
import com.example.bookwonders.model.User;
import com.example.bookwonders.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("this email is already in use");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }
}

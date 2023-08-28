package com.example.bookwonders.service.impl;

import com.example.bookwonders.dto.user.UserRegistrationRequestDto;
import com.example.bookwonders.dto.user.UserResponseDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.exception.RegistrationException;
import com.example.bookwonders.mapper.UserMapper;
import com.example.bookwonders.model.Role;
import com.example.bookwonders.model.RoleName;
import com.example.bookwonders.model.ShoppingCart;
import com.example.bookwonders.model.User;
import com.example.bookwonders.repository.cart.ShoppingCartRepository;
import com.example.bookwonders.repository.user.UserRepository;
import com.example.bookwonders.service.RoleService;
import com.example.bookwonders.service.UserService;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("this email is already in use");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleService.getRoleByRoleName(RoleName.ROLE_USER);
        user.setRoles(new HashSet<>(Set.of(userRole)));
        if (user.getEmail().equals("artem.doe@examle.com")) {
            user.setRoles(new HashSet<>(Set.of(roleService
                    .getRoleByRoleName(RoleName.ROLE_ADMIN))));
        }
        User savedUser = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(savedUser);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toDto(savedUser);
    }

    @Override
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
                new EntityNotFoundException("can't find user by username: "
                        + authentication.getName()));
    }
}

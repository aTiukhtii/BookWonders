package com.example.bookwonders.service;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.mapper.CartItemMapper;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.repository.cart.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItem save(AddCartItemRequestDto requestDto) {
        return cartItemRepository.save(cartItemMapper.toModel(requestDto));
    }

    @Override
    public CartItem getById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("can't find cart item by id: " + id));
    }

    @Override
    public void delete(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new EntityNotFoundException("can't delete cart item by id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
    }
}

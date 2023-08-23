package com.example.bookwonders.service;

import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.mapper.CartItemMapper;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.ShoppingCart;
import com.example.bookwonders.model.User;
import com.example.bookwonders.repository.book.BookRepository;
import com.example.bookwonders.repository.cart.CartItemRepository;
import com.example.bookwonders.repository.cart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartItem save(AddCartItemRequestDto requestDto) {
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(() ->
                new EntityNotFoundException("can't find book by id: " + requestDto.getBookId()));
        return cartItemRepository.save(cartItemMapper.toModel(requestDto, book,
                getShoppingCartModel()));
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

    private ShoppingCart getShoppingCartModel() {
        User user = userService.getUser();
        return shoppingCartRepository.findById(user.getId()).get();
    }
}

package com.example.bookwonders.service.impl;

import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.dto.cart.ShoppingCartResponseDto;
import com.example.bookwonders.dto.cart.UpdateBookQuantityInCartDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.mapper.CartItemMapper;
import com.example.bookwonders.mapper.ShoppingCartMapper;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.ShoppingCart;
import com.example.bookwonders.model.User;
import com.example.bookwonders.repository.book.BookRepository;
import com.example.bookwonders.repository.cart.CartItemRepository;
import com.example.bookwonders.repository.cart.ShoppingCartRepository;
import com.example.bookwonders.service.ShoppingCartService;
import com.example.bookwonders.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartMapper.toDto(getShoppingCartModel());
    }

    @Override
    public ShoppingCartResponseDto addCartItem(AddCartItemRequestDto requestDto) {
        ShoppingCart cart = getShoppingCartModel();
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(() ->
                new EntityNotFoundException("can't find book by id: " + requestDto.getBookId()));
        CartItem cartItem = cartItemMapper.toModel(requestDto, book);
        cartItem.setShoppingCart(cart);
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        cart.addCartItem(savedCartItem);
        return getShoppingCart();
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto updateQuantityOfBooks(Long cartItemId,
                                                         UpdateBookQuantityInCartDto requestDto) {
        cartItemRepository.findById(cartItemId).orElseThrow(() ->
                new EntityNotFoundException("can't find cart item by id: " + cartItemId))
                .setQuantity(requestDto.quantity());
        return getShoppingCart();
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new EntityNotFoundException("can't delete cart item by id: " + cartItemId);
        }
        cartItemRepository.deleteById(cartItemId);
        return getShoppingCart();
    }

    @Override
    public ShoppingCart getShoppingCartModel() {
        User user = userService.getUser();
        return shoppingCartRepository.findById(user.getId()).get();
    }
}

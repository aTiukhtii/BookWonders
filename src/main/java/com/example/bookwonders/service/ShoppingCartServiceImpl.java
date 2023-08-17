package com.example.bookwonders.service;

import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.dto.cart.ShoppingCartResponseDto;
import com.example.bookwonders.dto.cart.UpdateBookQuantityInCartDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.mapper.ShoppingCartMapper;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.ShoppingCart;
import com.example.bookwonders.model.User;
import com.example.bookwonders.repository.cart.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCart save(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartMapper.toDto(getShoppingCartModel());
    }

    @Override
    public ShoppingCartResponseDto addCartItem(AddCartItemRequestDto requestDto) {
        ShoppingCart cart = getShoppingCartModel();
        CartItem cartItem = cartItemService.save(requestDto);
        cart.addCartItem(cartItem);
        return getShoppingCart();
    }

    @Override
    public ShoppingCartResponseDto updateQuantityOfBooks(Long cartItemId,
                                                         UpdateBookQuantityInCartDto requestDto) {
        cartItemService.getById(cartItemId).setQuantity(requestDto.quantity());
        return getShoppingCart();
    }

    @Override
    public ShoppingCartResponseDto deleteCartItem(Long cartItemId) {
        cartItemService.delete(cartItemId);
        return getShoppingCart();
    }

    @Override
    public ShoppingCart getShoppingCartModel() {
        User user = userService.getUser();
        return shoppingCartRepository.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("can't find cart by id: " + user.getId()));
    }

    @Override
    public void completePurchase(ShoppingCart shoppingCart) {
        shoppingCartRepository.delete(shoppingCart);
        ShoppingCart shoppingCartNew = new ShoppingCart();
        shoppingCartNew.setUser(shoppingCart.getUser());
        shoppingCartRepository.save(shoppingCartNew);
    }
}

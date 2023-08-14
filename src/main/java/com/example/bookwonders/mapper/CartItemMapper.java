package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.cart.CartItemRequestDto;
import com.example.bookwonders.dto.cart.CartItemResponseDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.User;
import com.example.bookwonders.repository.cart.ShoppingCartRepository;
import com.example.bookwonders.repository.user.UserRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(config = MapperConfig.class)
public abstract class CartItemMapper {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private UserRepository userRepository;

    public abstract CartItem toModel(CartItemRequestDto requestDto);

    public abstract CartItemResponseDto toDto(CartItem cartItem);

    @AfterMapping
    public void setBookInfoToModel(@MappingTarget CartItem cartItem,
                                   CartItemRequestDto requestDto) {
        cartItem.setBook(bookMapper.bookFromId(requestDto.getBookId()));
    }

    @AfterMapping
    public void setBookInfoToDto(@MappingTarget CartItemResponseDto responseDto,
                                 CartItem cartItem) {
        responseDto.setBookId(cartItem.getBook().getId());
        responseDto.setBookTitle(cartItem.getBook().getTitle());
    }

    @AfterMapping
    public void setShoppingCart(@MappingTarget CartItem cartItem,
                                CartItemRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() ->
                new EntityNotFoundException("can't find user by username: "
                        + authentication.getName()));
        cartItem.setShoppingCart(shoppingCartRepository.findById(user).orElseThrow());
    }
}

package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.cart.AddCartItemRequestDto;
import com.example.bookwonders.dto.cart.CartItemResponseDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.ShoppingCart;
import com.example.bookwonders.model.User;
import com.example.bookwonders.repository.cart.ShoppingCartRepository;
import com.example.bookwonders.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
@RequiredArgsConstructor
public abstract class CartItemMapper {
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public abstract CartItem toModel(AddCartItemRequestDto requestDto);

    public abstract CartItemResponseDto toDto(CartItem cartItem);

    @AfterMapping
    public void setBookInfoToModel(@MappingTarget CartItem cartItem,
                                   AddCartItemRequestDto requestDto) {
        cartItem.setBook(bookMapper.bookFromId(requestDto.getBookId()));
    }

    @AfterMapping
    public void setBookInfoToDto(@MappingTarget CartItemResponseDto responseDto,
                                 CartItem cartItem) {
        responseDto.setBookId(cartItem.getBook().getId());
        responseDto.setBookTitle(cartItem.getBook().getTitle());
    }

    @AfterMapping
    public void setShoppingCartToModel(@MappingTarget CartItem cartItem) {
        User user = userService.getUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("can't find shopping cart by id: " + user.getId()));
        cartItem.setShoppingCart(shoppingCart);
    }
}

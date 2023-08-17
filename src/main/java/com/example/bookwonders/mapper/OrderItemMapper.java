package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.order.OrderItemResponseDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.CartItem;
import com.example.bookwonders.model.OrderItem;
import com.example.bookwonders.repository.book.BookRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public abstract class OrderItemMapper {
    @Autowired
    private BookRepository bookRepository;

    public abstract OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    public abstract OrderItem cartItemToOrderItem(CartItem cartItemResponseDto);

    @AfterMapping
    public void setBookToOrderItem(@MappingTarget OrderItem orderItem,
                                            CartItem cartItem) {
        Book book = bookRepository.findById(cartItem.getBook().getId()).orElseThrow(() ->
                new EntityNotFoundException("can't find book by id: "
                        + cartItem.getBook().getId()));
        orderItem.setPrice(book.getPrice());
        orderItem.setBook(book);
    }

    @AfterMapping
    public void setBookIdToOrderDto(@MappingTarget OrderItemResponseDto responseDto,
                                    OrderItem orderItem) {
        responseDto.setBookId(orderItem.getBook().getId());
    }
}

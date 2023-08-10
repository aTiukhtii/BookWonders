package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import com.example.bookwonders.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toModel(CreateBookRequestDto requestDto);

    BookResponseDto toDto(Book book);
}

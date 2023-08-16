package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.Category;
import com.example.bookwonders.repository.book.BookRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public abstract class BookMapper {
    @Autowired
    private BookRepository bookRepository;

    public abstract Book toModel(CreateBookRequestDto requestDto);

    public abstract BookResponseDto toDto(Book book);

    public abstract BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    public void setCategoryIds(@MappingTarget BookResponseDto bookDto, Book book) {
        bookDto.setCategoryIds(book.getCategories().stream().map(Category::getId).toList());
    }

    @Named("bookFromId")
    public Book bookFromId(Long id) {
        return bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("can't find book by id: " + id));
    }
}

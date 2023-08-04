package com.example.bookwonders.service;

import com.example.bookwonders.dto.BookResponseDto;
import com.example.bookwonders.dto.CreateBookRequestDto;
import com.example.bookwonders.model.Book;
import java.util.List;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    public BookResponseDto getBookById(Long id);

    List<BookResponseDto> findAll();
}

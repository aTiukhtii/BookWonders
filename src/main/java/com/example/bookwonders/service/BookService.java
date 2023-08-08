package com.example.bookwonders.service;

import com.example.bookwonders.dto.BookResponseDto;
import com.example.bookwonders.dto.BookSearchParametersDto;
import com.example.bookwonders.dto.CreateBookRequestDto;
import com.example.bookwonders.model.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    BookResponseDto getBookById(Long id);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto update(Long id, CreateBookRequestDto requestDto);

    List<BookResponseDto> search(BookSearchParametersDto bookSearchParameters);

    void delete(Long id);
}

package com.example.bookwonders.service;

import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.BookSearchParametersDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    BookResponseDto getBookById(Long id);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto update(Long id, CreateBookRequestDto requestDto);

    List<BookResponseDto> search(BookSearchParametersDto bookSearchParameters, Pageable pageable);

    void delete(Long id);
}

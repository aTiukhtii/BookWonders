package com.example.bookwonders.service;

import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto save(CreateBookRequestDto requestDto);

    BookResponseDto getBookById(Long id);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto update(Long id, CreateBookRequestDto requestDto);

    List<BookResponseDto> search(Map<String, String> param, Pageable pageable);

    void delete(Long id);
}

package com.example.bookwonders.service;

import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.BookSearchParametersDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import com.example.bookwonders.exception.EntityNotFoundException;
import com.example.bookwonders.mapper.BookMapper;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.Category;
import com.example.bookwonders.repository.book.BookRepository;
import com.example.bookwonders.repository.book.BookSpecificationBuilder;
import com.example.bookwonders.repository.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookResponseDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        getCategoriesByIds(requestDto.getCategoryIds())
                .forEach(category -> category.addBook(book));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookResponseDto getBookById(Long id) {
        return bookMapper.toDto(bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("couldn't find book by id: " + id)));
    }

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookResponseDto update(Long id, CreateBookRequestDto requestDto) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("can't update book by id: " + id);
        }
        Book book = bookMapper.toModel(requestDto);
        getCategoriesByIds(requestDto.getCategoryIds())
                .forEach(category -> category.addBook(book));
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookResponseDto> search(BookSearchParametersDto searchParametersDto,
                                        Pageable pageable) {
        return bookRepository.findAll(
                bookSpecificationBuilder.build(searchParametersDto), pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("can't delete book by id: " + id);
        }
        bookRepository.deleteById(id);
    }

    private Set<Category> getCategoriesByIds(List<Long> ids) {
        return ids.stream()
                .map(categoryRepository::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }
}

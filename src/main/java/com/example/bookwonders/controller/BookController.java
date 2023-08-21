package com.example.bookwonders.controller;

import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.BookSearchParametersDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import com.example.bookwonders.dto.book.UpdateBookRequestDto;
import com.example.bookwonders.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
@Tag(name = "Books", description = "API for managing books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books")
    public List<BookResponseDto> getAllBooks(@ParameterObject Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new book (Only for admin)")
    @ApiResponse(responseCode = "201",
            description = "Book created successfully",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponseDto.class))})
    public BookResponseDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update a book by ID (Only for admin)")
    @ApiResponse(responseCode = "200",
            description = "Book updated successfully",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponseDto.class))})
    public BookResponseDto updateBook(@PathVariable Long id,
                                      @RequestBody @Valid UpdateBookRequestDto requestDto) {
        return bookService.update(id, requestDto);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books")
    public List<BookResponseDto> search(BookSearchParametersDto bookSearchParameters) {
        return bookService.search(bookSearchParameters);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book by id (Only for admin)")
    @ApiResponse(responseCode = "204",
            description = "Book deleted successfully")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}

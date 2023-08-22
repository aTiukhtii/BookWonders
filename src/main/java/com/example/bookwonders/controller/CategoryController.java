package com.example.bookwonders.controller;

import com.example.bookwonders.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookwonders.dto.category.CategoryResponseDto;
import com.example.bookwonders.dto.category.CreateCategoryDto;
import com.example.bookwonders.service.CategoryService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "API for managing categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories")
    public List<CategoryResponseDto> getAllCategories(@ParameterObject Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category (Only for admin)")
    @ApiResponse(responseCode = "201",
            description = "Category created successfully",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryResponseDto.class))})
    public CategoryResponseDto createCategory(@RequestBody @Valid CreateCategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category by ID (Only for admin)")
    @ApiResponse(responseCode = "200",
            description = "Category updated successfully",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CategoryResponseDto.class))})
    public CategoryResponseDto updateCategory(@PathVariable Long id,
                                              @RequestBody @Valid CreateCategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by id")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category id")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return categoryService.getBooksByCategoriesId(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a category by id (Only for admin)")
    @ApiResponse(responseCode = "204",
            description = "Category deleted successfully")
    public void delete(@PathVariable Long id) {
        categoryService.deleteById(id);
    }
}

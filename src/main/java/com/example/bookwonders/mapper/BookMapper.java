package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "mapCategoriesToIds")
    BookResponseDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Named("mapCategoriesToIds")
    default List<Long> mapCategoriesToIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }
}

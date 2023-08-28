package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.category.CategoryResponseDto;
import com.example.bookwonders.dto.category.CreateCategoryDto;
import com.example.bookwonders.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "books", ignore = true)
    Category toModel(CreateCategoryDto requestDto);

    CategoryResponseDto toDto(Category category);
}

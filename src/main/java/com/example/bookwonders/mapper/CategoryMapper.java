package com.example.bookwonders.mapper;

import com.example.bookwonders.config.MapperConfig;
import com.example.bookwonders.dto.category.CategoryResponseDto;
import com.example.bookwonders.dto.category.CreateCategoryDto;
import com.example.bookwonders.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toModel(CreateCategoryDto requestDto);

    CategoryResponseDto toDto(Category category);
}

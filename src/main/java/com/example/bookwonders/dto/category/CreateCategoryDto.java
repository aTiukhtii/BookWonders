package com.example.bookwonders.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryDto {
    @NotBlank
    @Size(max = 70)
    private String name;
    @NotBlank
    private String description;
}


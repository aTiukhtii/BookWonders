package com.example.bookwonders.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateCategoryDto {
    @NotBlank
    @Size(max = 70)
    private String name;
    @NotBlank
    private String description;
}


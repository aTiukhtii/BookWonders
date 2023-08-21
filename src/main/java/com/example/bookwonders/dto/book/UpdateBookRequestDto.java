package com.example.bookwonders.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateBookRequestDto {
    @NotBlank
    @Size(max = 100)
    private String title;
    @NotBlank
    @Size(max = 100)
    private String author;
    @NotNull
    @Min(0)
    private BigDecimal price;
    private List<Long> categoryIds;
    private String description;
    private String coverImage;
}

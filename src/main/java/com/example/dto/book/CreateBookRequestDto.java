package com.example.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank
    @Size(min = 1, max = 50)
    private String author;

    @NotBlank
    private String isbn;

    @NotNull
    @Positive
    private BigDecimal price;

    @Size(max = 500)
    private String description;

    private String coverImage;

    @NotEmpty
    private Set<Long> categoryIds;
}

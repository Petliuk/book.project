package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotNull
    @Size(min = 1, max = 100)
    private String title;

    @NotNull
    @Size(min = 1, max = 50)
    private String author;

    @NotNull
    private String isbn;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @Size(max = 500)
    private String description;

    @NotNull
    private String coverImage;
}

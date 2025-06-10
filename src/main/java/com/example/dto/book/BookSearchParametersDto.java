package com.example.dto.book;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BookSearchParametersDto {
    private String[] title;
    private String[] author;
}

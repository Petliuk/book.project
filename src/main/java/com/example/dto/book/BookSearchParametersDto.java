package com.example.dto.book;

import lombok.Data;

@Data
public class BookSearchParametersDto {
    private String[] title;
    private String[] author;
}

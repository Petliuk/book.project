package com.example.service;

import com.example.dto.BookDto;
import com.example.dto.BookSearchParametersDto;
import com.example.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {

    List<BookDto> getAll();

    BookDto getBookById(Long id);

    BookDto createBook(CreateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);

    BookDto updateBook(Long id, CreateBookRequestDto bookDto);
}

package com.example.service;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParametersDto;
import com.example.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    Page<BookDto> getAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto createBook(CreateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto params);

    BookDto updateBook(Long id, CreateBookRequestDto bookDto);
}

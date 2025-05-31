package com.example.util.utils;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParametersDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.model.Book;
import com.example.model.Category;
import com.example.util.constants.BookServiceConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.util.Collections;

public final class BookServiceUtils {

    // Prevent instantiation
    private BookServiceUtils() {
    }

    // Create test data
    public static Book createBook() {
        Book book = new Book();
        book.setId(BookServiceConstants.BOOK_ID);
        book.setTitle(BookServiceConstants.BOOK_TITLE);
        book.setAuthor(BookServiceConstants.BOOK_AUTHOR);
        book.setIsbn(BookServiceConstants.BOOK_ISBN);
        book.setPrice(BookServiceConstants.BOOK_PRICE);
        return book;
    }

    public static BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(BookServiceConstants.BOOK_ID);
        bookDto.setTitle(BookServiceConstants.BOOK_TITLE);
        bookDto.setAuthor(BookServiceConstants.BOOK_AUTHOR);
        bookDto.setIsbn(BookServiceConstants.BOOK_ISBN);
        bookDto.setPrice(BookServiceConstants.BOOK_PRICE);
        return bookDto;
    }

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(BookServiceConstants.BOOK_TITLE);
        requestDto.setAuthor(BookServiceConstants.BOOK_AUTHOR);
        requestDto.setIsbn(BookServiceConstants.BOOK_ISBN);
        requestDto.setPrice(BookServiceConstants.BOOK_PRICE);
        requestDto.setCategoryIds(BookServiceConstants.CATEGORY_IDS);
        return requestDto;
    }

    public static BookSearchParametersDto createBookSearchParametersDto() {
        BookSearchParametersDto params = new BookSearchParametersDto();
        params.setTitle(BookServiceConstants.SEARCH_TITLE);
        return params;
    }

    public static Category createCategory() {
        return new Category(BookServiceConstants.CATEGORY_ID);
    }

    public static Page<Book> createBookPage(Book book) {
        return new PageImpl<>(Collections.singletonList(book),
                PageRequest.of(BookServiceConstants.PAGE, BookServiceConstants.SIZE), 1);
    }

    // Create mock specification
    public static Specification<Book> createMockSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}

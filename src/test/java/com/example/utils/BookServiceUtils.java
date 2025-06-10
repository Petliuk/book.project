package com.example.utils;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParametersDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.model.Book;
import com.example.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;

public final class BookServiceUtils {

    public static final Long BOOK_ID = 1L;
    public static final String BOOK_TITLE = "Test Book";
    public static final String BOOK_AUTHOR = "Test Author";
    public static final String BOOK_ISBN = "1234567890";
    public static final BigDecimal BOOK_PRICE = new BigDecimal("29.99");
    public static final Long CATEGORY_ID = 1L;
    public static final Set<Long> CATEGORY_IDS = Set.of(CATEGORY_ID);
    public static final int PAGE = 0;
    public static final int SIZE = 10;
    public static final String EXPECTED_BOOK_COUNT_MESSAGE = "Should return one book";
    public static final String BOOK_TITLE_MESSAGE = "Book title doesn't match";

    private BookServiceUtils() {
    }

    public static Book createBook() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPrice(BOOK_PRICE);
        return book;
    }

    public static BookDto createBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(BOOK_ID);
        bookDto.setTitle(BOOK_TITLE);
        bookDto.setAuthor(BOOK_AUTHOR);
        bookDto.setIsbn(BOOK_ISBN);
        bookDto.setPrice(BOOK_PRICE);
        return bookDto;
    }

    public static CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(BOOK_TITLE);
        requestDto.setAuthor(BOOK_AUTHOR);
        requestDto.setIsbn(BOOK_ISBN);
        requestDto.setPrice(BOOK_PRICE);
        requestDto.setCategoryIds(CATEGORY_IDS);
        return requestDto;
    }

    public static BookSearchParametersDto createBookSearchParametersDto() {
        BookSearchParametersDto params = new BookSearchParametersDto();
        params.setTitle(new String[]{BOOK_TITLE});
        return params;
    }

    public static Category createCategory() {
        return new Category(CATEGORY_ID);
    }

    public static Page<Book> createBookPage(Book book) {
        return new PageImpl<>(Collections.singletonList(book), PageRequest.of(PAGE, SIZE), 1);
    }

    public static Specification<Book> createMockSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }
}

package com.example.utils;

import com.example.model.Book;
import com.example.repository.book.spec.AuthorSpecificationProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;

public final class BookRepositoryUtils {

    public static final int PAGE = 0;
    public static final int SIZE = 10;
    public static final Long BOOK_ID = 1L;
    public static final String TEST_BOOK_TITLE = "Test Book";
    public static final String TEST_BOOK_1_TITLE = "Test Book 1";
    public static final String NEW_BOOK_TITLE = "New Book";
    public static final String TEST_AUTHOR = "Test Author";
    public static final String NEW_AUTHOR = "New Author";
    public static final String NEW_ISBN = "1111111111";
    public static final BigDecimal NEW_BOOK_PRICE = new BigDecimal("19.99");
    public static final Long CATEGORY_ID = 1L;
    public static final String ADD_TEST_BOOKS_SQL = "classpath:database/books/add-test-books.sql";
    public static final String DELETE_BOOK_SQL = "classpath:database/books/delete-book.sql";
    public static final String ADD_TEST_CATEGORIES_SQL = "classpath:database/categories/add-test-categories.sql";
    public static final String REMOVE_TEST_CATEGORIES_SQL = "classpath:database/categories/remove-test-categories.sql";
    public static final String EXPECTED_BOOK_COUNT_MESSAGE = "Expected %d books";
    public static final String BOOK_TITLE_MATCH_MESSAGE = "Book title doesn't match";
    public static final String BOOK_AUTHOR_MATCH_MESSAGE = "Book author doesn't match";
    public static final String BOOK_PRESENT_MESSAGE = "Book should exist";
    public static final String BOOK_NOT_PRESENT_MESSAGE = "Book should be deleted";

    private BookRepositoryUtils() {
    }

    public static Book createNewBook() {
        Book book = new Book();
        book.setTitle(NEW_BOOK_TITLE);
        book.setAuthor(NEW_AUTHOR);
        book.setIsbn(NEW_ISBN);
        book.setPrice(NEW_BOOK_PRICE);
        book.setDeleted(false);
        return book;
    }

    public static Specification<Book> createAuthorSpecification() {
        AuthorSpecificationProvider specProvider = new AuthorSpecificationProvider();
        return specProvider.getSpecification(new String[]{TEST_AUTHOR});
    }

    public static PageRequest createPageRequest() {
        return PageRequest.of(PAGE, SIZE);
    }
}

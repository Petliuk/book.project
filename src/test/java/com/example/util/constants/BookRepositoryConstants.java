package com.example.util.constants;

import java.math.BigDecimal;

public final class BookRepositoryConstants {

    // Prevent instantiation
    private BookRepositoryConstants() {
    }

    // Book data
    public static final Long BOOK_ID = 1L;
    public static final String TEST_BOOK_TITLE = "Test Book";
    public static final String TEST_BOOK_1_TITLE = "Test Book 1";
    public static final String NEW_BOOK_TITLE = "New Book";
    public static final String TEST_AUTHOR = "Test Author";
    public static final String NEW_AUTHOR = "New Author";
    public static final String[] TEST_AUTHOR_ARRAY = new String[]{TEST_AUTHOR};
    public static final String NEW_ISBN = "1111111111";
    public static final BigDecimal NEW_BOOK_PRICE = new BigDecimal("19.99");

    // Category data
    public static final Long CATEGORY_ID = 1L;

    // Pagination parameters
    public static final int PAGE = 0;
    public static final int SIZE = 10;

    // SQL script paths
    public static final String ADD_TEST_BOOKS_SQL = "classpath:database/books/add-test-books.sql";
    public static final String DELETE_BOOK_SQL = "classpath:database/books/delete-book.sql";
    public static final String ADD_TEST_CATEGORIES_SQL = "classpath:database/categories/add-test-categories.sql";
    public static final String REMOVE_TEST_CATEGORIES_SQL = "classpath:database/categories/remove-test-categories.sql";

    // Assertion messages
    public static final String EXPECTED_BOOK_COUNT_MESSAGE = "Expected %d books in the response";
    public static final String BOOK_TITLE_MATCH_MESSAGE = "Book title should match expected";
    public static final String BOOK_AUTHOR_MATCH_MESSAGE = "Book author should match expected";
    public static final String BOOK_PRESENT_MESSAGE = "Book should be present";
    public static final String BOOK_NOT_PRESENT_MESSAGE = "Book should not be present";
}
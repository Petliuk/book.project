package com.example.util.constants;

import java.math.BigDecimal;

public final class CategoryServiceConstants {

    // Prevent instantiation
    private CategoryServiceConstants() {
    }

    // Category data
    public static final Long CATEGORY_ID = 1L;
    public static final String CATEGORY_NAME = "Fiction";

    // Book data
    public static final Long BOOK_ID = 1L;
    public static final String BOOK_TITLE = "Test Book";
    public static final String BOOK_AUTHOR = "Test Author";
    public static final String BOOK_ISBN = "1234567890";
    public static final BigDecimal BOOK_PRICE = new BigDecimal("29.99");

    // Pagination parameters
    public static final int PAGE = 0;
    public static final int SIZE = 10;

    // Assertion messages
    public static final String EXPECTED_COUNT_MESSAGE = "Expected one item in the response";
    public static final String NAME_MATCH_MESSAGE = "Name should match expected";
    public static final String TITLE_MATCH_MESSAGE = "Book title should match expected";
}

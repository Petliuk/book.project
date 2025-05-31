package com.example.util.constants;

import java.math.BigDecimal;
import java.util.Set;

public final class BookServiceConstants {

    // Prevent instantiation
    private BookServiceConstants() {
    }

    // Book data
    public static final Long BOOK_ID = 1L;
    public static final String BOOK_TITLE = "Test Book";
    public static final String[] SEARCH_TITLE = new String[]{BOOK_TITLE};
    public static final String BOOK_AUTHOR = "Test Author";
    public static final String BOOK_ISBN = "1234567890";
    public static final BigDecimal BOOK_PRICE = new BigDecimal("29.99");
    public static final Long CATEGORY_ID = 1L;
    public static final Set<Long> CATEGORY_IDS = Set.of(CATEGORY_ID);

    // Pagination parameters
    public static final int PAGE = 0;
    public static final int SIZE = 10;

    // Assertion messages
    public static final String EXPECTED_BOOK_COUNT_MESSAGE = "Expected one book in the response";
    public static final String BOOK_TITLE_MATCH_MESSAGE = "Book title should match expected";
}

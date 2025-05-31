package com.example.util.constants;

import java.math.BigDecimal;

public class BookControllerConstants {
    private BookControllerConstants() {
    }

    // Book-related constants
    public static final Long BOOK_ID = 1L;
    public static final Long INVALID_BOOK_ID = 999L;
    public static final String BOOK_TITLE = "Test Book";
    public static final String BOOK_AUTHOR = "Test Author";
    public static final String BOOK_ISBN = "1234567890";
    public static final BigDecimal BOOK_PRICE = new BigDecimal("29.99");
    public static final Long CATEGORY_ID = 1L;
    public static final String[] SEARCH_TITLE = new String[]{BOOK_TITLE};

    // Error messages
    public static final String NOT_FOUND_MESSAGE = "Book not found";

    // Pagination parameters
    public static final String PAGE = "0";
    public static final String SIZE = "10";

    // Endpoints
    public static final String BOOKS_ENDPOINT = "/books";
    public static final String BOOKS_SEARCH_ENDPOINT = "/books/search";
    public static final String TITLE_PARAM = "title";

    // Assertion messages
    public static final String RESPONSE_NOT_NULL_MESSAGE = "Response content should not be null";
    public static final String EXPECTED_BOOK_COUNT_MESSAGE = "Expected one book in the response";
    public static final String BOOK_DETAILS_MATCH_MESSAGE = "Book details should match expected";
    public static final String EXCEPTION_TYPE_MESSAGE = "Expected EntityNotFoundException to be thrown";
    public static final String EXCEPTION_MESSAGE_MATCH = "Exception message should match";
    public static final String CREATED_BOOK_NOT_NULL_MESSAGE = "Created book should not be null";
    public static final String CREATED_BOOK_MATCH_MESSAGE = "Created book should match expected";
    public static final String UPDATED_BOOK_NOT_NULL_MESSAGE = "Updated book should not be null";
    public static final String UPDATED_BOOK_MATCH_MESSAGE = "Updated book should match expected";
}

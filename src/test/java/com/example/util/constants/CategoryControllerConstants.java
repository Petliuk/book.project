package com.example.util.constants;

import java.math.BigDecimal;

public final class CategoryControllerConstants {

    // Prevent instantiation
    private CategoryControllerConstants() {
    }

    // IDs
    public static final Long VALID_ID = 1L;
    public static final Long INVALID_ID = -1L;
    public static final Long CATEGORY_ID_2 = 2L;
    public static final Long CATEGORY_ID_3 = 3L;
    public static final Long BOOK_ID_1 = 1L;
    public static final Long BOOK_ID_2 = 3L;

    // Category data
    public static final String CATEGORY_NAME = "Fiction";
    public static final String CATEGORY_DESCRIPTION = "Fiction books";
    public static final String UPDATED_CATEGORY_NAME = "Updated Fiction";
    public static final String UPDATED_CATEGORY_DESCRIPTION = "Updated fiction books";
    public static final String NON_FICTION_NAME = "Non-Fiction";
    public static final String NON_FICTION_DESCRIPTION = "Non-fiction books";
    public static final String SCIENCE_NAME = "Science";
    public static final String SCIENCE_DESCRIPTION = "Science books";

    // Book data
    public static final String BOOK_TITLE_1 = "Test Book 1";
    public static final String BOOK_AUTHOR_1 = "Test Author 1";
    public static final String BOOK_ISBN_1 = "1234567890";
    public static final BigDecimal BOOK_PRICE_1 = BigDecimal.valueOf(29.99);
    public static final String BOOK_DESCRIPTION_1 = "A test book";
    public static final String BOOK_COVER_IMAGE_1 = "cover1.jpg";

    public static final String BOOK_TITLE_2 = "Science Book";
    public static final String BOOK_AUTHOR_2 = "Science Author";
    public static final String BOOK_ISBN_2 = "1122334455";
    public static final BigDecimal BOOK_PRICE_2 = BigDecimal.valueOf(49.99);
    public static final String BOOK_DESCRIPTION_2 = "Science book";
    public static final String BOOK_COVER_IMAGE_2 = "cover3.jpg";

    // Error messages
    public static final String NOT_FOUND_MESSAGE = "Category not found";

    // Pagination parameters
    public static final String PAGE = "0";
    public static final String SIZE = "10";
    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";

    // Endpoints
    public static final String CATEGORIES_ENDPOINT = "/categories";
    public static final String CATEGORY_BY_ID_ENDPOINT = "/categories/{id}";
    public static final String BOOKS_BY_CATEGORY_ENDPOINT = "/categories/{id}/books";

    // Assertion messages
    public static final String RESPONSE_NOT_NULL_MESSAGE = "Response content should not be null";
    public static final String EXPECTED_CATEGORY_COUNT_MESSAGE = "Expected three categories in the response";
    public static final String EXPECTED_BOOK_COUNT_MESSAGE = "Expected two books in the response";
    public static final String CATEGORY_DETAILS_MATCH_MESSAGE = "Category details should match expected";
    public static final String BOOK_DETAILS_MATCH_MESSAGE = "Book details should match expected";
    public static final String EXCEPTION_TYPE_MESSAGE = "Expected EntityNotFoundException to be thrown";
}

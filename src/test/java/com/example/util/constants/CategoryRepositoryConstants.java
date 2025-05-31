package com.example.util.constants;

public final class CategoryRepositoryConstants {

    // Prevent instantiation
    private CategoryRepositoryConstants() {
    }

    // Category data
    public static final Long CATEGORY_ID = 1L;
    public static final String FICTION_NAME = "Fiction";
    public static final String SCIENCE_NAME = "Science";
    public static final String SCIENCE_DESCRIPTION = "Science books";

    // Pagination parameters
    public static final int PAGE = 0;
    public static final int SIZE = 10;

    // SQL script paths
    public static final String ADD_TEST_CATEGORIES_SQL = "classpath:database/categories/add-test-categories.sql";
    public static final String REMOVE_TEST_CATEGORIES_SQL = "classpath:database/categories/remove-test-categories.sql";

    // Assertion messages
    public static final String EXPECTED_CATEGORY_COUNT_MESSAGE = "Expected %d categories in the response";
    public static final String CATEGORY_NAME_MATCH_MESSAGE = "Category name should match expected";
    public static final String CATEGORY_PRESENT_MESSAGE = "Category should be present";
    public static final String CATEGORY_NOT_PRESENT_MESSAGE = "Category should not be present";
}

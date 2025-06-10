package com.example.utils;

import com.example.model.Category;
import org.springframework.data.domain.PageRequest;

public final class CategoryRepositoryUtils {

    public static final int PAGE = 0;
    public static final int SIZE = 10;
    public static final Long CATEGORY_ID = 1L;
    public static final String FICTION_NAME = "Fiction";
    public static final String SCIENCE_NAME = "Science";
    public static final String SCIENCE_DESCRIPTION = "Science books";
    public static final String ADD_TEST_CATEGORIES_SQL = "classpath:database/categories/add-test-categories.sql";
    public static final String REMOVE_TEST_CATEGORIES_SQL = "classpath:database/categories/remove-test-categories.sql";
    public static final String CATEGORY_NAME_MESSAGE = "Category name doesn't match";
    public static final String CATEGORY_EXISTS_MESSAGE = "Category should exist";
    public static final String CATEGORY_NOT_EXISTS_MESSAGE = "Category should be deleted";

    private CategoryRepositoryUtils() {
    }

    public static Category createScienceCategory() {
        Category category = new Category();
        category.setName(SCIENCE_NAME);
        category.setDescription(SCIENCE_DESCRIPTION);
        category.setDeleted(false);
        return category;
    }

    public static PageRequest createPageRequest() {
        return PageRequest.of(PAGE, SIZE);
    }
}

package com.example.util.utils;

import com.example.model.Category;
import com.example.util.constants.CategoryRepositoryConstants;
import org.springframework.data.domain.PageRequest;

public final class CategoryRepositoryUtils {

    // Prevent instantiation
    private CategoryRepositoryUtils() {
    }

    // Create test data
    public static Category createScienceCategory() {
        Category category = new Category();
        category.setName(CategoryRepositoryConstants.SCIENCE_NAME);
        category.setDescription(CategoryRepositoryConstants.SCIENCE_DESCRIPTION);
        category.setDeleted(false);
        return category;
    }

    // Create pagination
    public static PageRequest createPageRequest() {
        return PageRequest.of(CategoryRepositoryConstants.PAGE, CategoryRepositoryConstants.SIZE);
    }
}

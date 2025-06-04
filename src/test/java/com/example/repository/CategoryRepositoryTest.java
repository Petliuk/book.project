package com.example.repository;

import com.example.model.Category;
import com.example.repository.category.CategoryRepository;
import com.example.utils.CategoryRepositoryUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find all categories when categories exist returns page")
    @Sql(scripts = CategoryRepositoryUtils.ADD_TEST_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryRepositoryUtils.REMOVE_TEST_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_CategoriesExist_ReturnsCategoryPage() {
        // Given
        PageRequest pageable = CategoryRepositoryUtils.createPageRequest();

        // When
        Page<Category> result = categoryRepository.findAll(pageable);

        // Then
        assertEquals(3, result.getTotalElements(), "Should return 3 categories");
        assertEquals(CategoryRepositoryUtils.FICTION_NAME, result.getContent().get(0).getName(),
          CategoryRepositoryUtils.CATEGORY_NAME_MESSAGE);
}

    @Test
    @DisplayName("Find category by ID when category exists returns category")
    @Sql(scripts = CategoryRepositoryUtils.ADD_TEST_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryRepositoryUtils.REMOVE_TEST_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_CategoryExists_ReturnsCategory() {
        // Given
        Long categoryId = CategoryRepositoryUtils.CATEGORY_ID;

        // When
        Optional<Category> result = categoryRepository.findById(categoryId);

        // Then
        assertTrue(result.isPresent(), CategoryRepositoryUtils.CATEGORY_EXISTS_MESSAGE);
        assertEquals(CategoryRepositoryUtils.FICTION_NAME, result.get().getName(),
                CategoryRepositoryUtils.CATEGORY_NAME_MESSAGE);
    }

    @Test
    @DisplayName("Save category when valid category returns saved category")
    @Sql(scripts = CategoryRepositoryUtils.REMOVE_TEST_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ValidCategory_ReturnsSavedCategory() {
        // Given
        Category category = CategoryRepositoryUtils.createScienceCategory();

        // When
        Category result = categoryRepository.save(category);

        // Then
        assertEquals(CategoryRepositoryUtils.SCIENCE_NAME, result.getName(),
                CategoryRepositoryUtils.CATEGORY_NAME_MESSAGE);
    }

    @Test
    @DisplayName("Delete category by ID when category exists marks category as deleted")
    @Sql(scripts = CategoryRepositoryUtils.ADD_TEST_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryRepositoryUtils.REMOVE_TEST_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_CategoryExists_MarksCategoryAsDeleted() {
        // Given
        Long categoryId = CategoryRepositoryUtils.CATEGORY_ID;

        // When
        categoryRepository.deleteById(categoryId);

        // Then
        Optional<Category> result = categoryRepository.findById(categoryId);
        assertFalse(result.isPresent(), CategoryRepositoryUtils.CATEGORY_NOT_EXISTS_MESSAGE);
    }
}

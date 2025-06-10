package com.example.utils;

import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;

public final class CategoryControllerUtils {

    public static final String PAGE = "0";
    public static final String SIZE = "10";
    public static final long VALID_ID = 1L;
    public static final long INVALID_ID = -1L;
    public static final int EXPECTED_CATEGORY_LIST_SIZE = 3;
    public static final int EXPECTED_BOOK_LIST_SIZE = 2;
    public static final String FANTASY_CATEGORY_NAME = "Fantasy";
    public static final String FICTION_CATEGORY_NAME = "Fiction";
    public static final String FICTION_CATEGORY_DESCRIPTION = "Fiction books";
    public static final String EPIC_FANTASY_CATEGORY_NAME = "Epic Fantasy";
    public static final String TEST_BOOK_TITLE = "Test Book 1";
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";
    public static final String CATEGORIES_URL = "/categories";
    public static final String CATEGORY_BOOKS_URL = "/categories/{id}/books";
    public static final String CLEAR_BOOKS_SCRIPT = "classpath:database/books/clear-books.sql";
    public static final String ADD_CATEGORY_SCRIPT = "classpath:database/categories/add-category.sql";
    public static final String ADD_TEST_CATEGORIES_SCRIPT = "classpath:database/categories/add-test-categories.sql";

    private CategoryControllerUtils() {
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName("Fantasy")
                .setDescription("Books featuring magical worlds and epic adventures");
    }

    public static CategoryRequestDto createUpdatedCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName("Epic Fantasy")
                .setDescription("High fantasy with grand narratives and mythical creatures");
    }

    public static <T> T parseResponse(MvcResult result, Class<T> clazz, ObjectMapper objectMapper)
            throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }

    public static <T> T parseResponse(MvcResult result, TypeReference<T> typeReference,
                                      ObjectMapper objectMapper) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
    }

    public static List<CategoryResponseDto> parsePagedCategoryResponse(MvcResult result,
                                                                       ObjectMapper objectMapper)
            throws Exception {
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        return objectMapper.readValue(jsonNode.get("content").toString(),
                new TypeReference<>() {
                });
    }
}

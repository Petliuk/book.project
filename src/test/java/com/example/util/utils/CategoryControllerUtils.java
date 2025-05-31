package com.example.util.utils;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.util.constants.CategoryControllerConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MvcResult;
import java.util.List;

public final class CategoryControllerUtils {

    // Prevent instantiation
    private CategoryControllerUtils() {
    }

    // Create test data
    public static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName(CategoryControllerConstants.CATEGORY_NAME)
                .setDescription(CategoryControllerConstants.CATEGORY_DESCRIPTION);
    }

    public static CategoryResponseDto createCategoryResponseDto(Long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName(CategoryControllerConstants.CATEGORY_NAME)
                .setDescription(CategoryControllerConstants.CATEGORY_DESCRIPTION);
    }

    public static CategoryRequestDto createUpdatedCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName(CategoryControllerConstants.UPDATED_CATEGORY_NAME)
                .setDescription(CategoryControllerConstants.UPDATED_CATEGORY_DESCRIPTION);
    }

    public static CategoryResponseDto createUpdatedCategoryResponseDto(Long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName(CategoryControllerConstants.UPDATED_CATEGORY_NAME)
                .setDescription(CategoryControllerConstants.UPDATED_CATEGORY_DESCRIPTION);
    }

    public static CategoryResponseDto createNonFictionCategoryResponseDto(Long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName(CategoryControllerConstants.NON_FICTION_NAME)
                .setDescription(CategoryControllerConstants.NON_FICTION_DESCRIPTION);
    }

    public static CategoryResponseDto createScienceCategoryResponseDto(Long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName(CategoryControllerConstants.SCIENCE_NAME)
                .setDescription(CategoryControllerConstants.SCIENCE_DESCRIPTION);
    }

    public static BookDtoWithoutCategoryIds createBookDto1() {
        return new BookDtoWithoutCategoryIds()
                .setId(CategoryControllerConstants.BOOK_ID_1)
                .setTitle(CategoryControllerConstants.BOOK_TITLE_1)
                .setAuthor(CategoryControllerConstants.BOOK_AUTHOR_1)
                .setIsbn(CategoryControllerConstants.BOOK_ISBN_1)
                .setPrice(CategoryControllerConstants.BOOK_PRICE_1)
                .setDescription(CategoryControllerConstants.BOOK_DESCRIPTION_1)
                .setCoverImage(CategoryControllerConstants.BOOK_COVER_IMAGE_1);
    }

    public static BookDtoWithoutCategoryIds createBookDto2() {
        return new BookDtoWithoutCategoryIds()
                .setId(CategoryControllerConstants.BOOK_ID_2)
                .setTitle(CategoryControllerConstants.BOOK_TITLE_2)
                .setAuthor(CategoryControllerConstants.BOOK_AUTHOR_2)
                .setIsbn(CategoryControllerConstants.BOOK_ISBN_2)
                .setPrice(CategoryControllerConstants.BOOK_PRICE_2)
                .setDescription(CategoryControllerConstants.BOOK_DESCRIPTION_2)
                .setCoverImage(CategoryControllerConstants.BOOK_COVER_IMAGE_2);
    }

    public static Page<CategoryResponseDto> createCategoryPage(List<CategoryResponseDto> categories) {
        return new PageImpl<>(categories, PageRequest.of(
                Integer.parseInt(CategoryControllerConstants.PAGE),
                Integer.parseInt(CategoryControllerConstants.SIZE)), categories.size());
    }

    // JSON deserialization utilities
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
                new TypeReference<List<CategoryResponseDto>>() {});
    }
}

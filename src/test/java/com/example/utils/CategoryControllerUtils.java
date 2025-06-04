package com.example.utils;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MvcResult;
import java.math.BigDecimal;
import java.util.List;

public final class CategoryControllerUtils {

    public static final String PAGE = "0";
    public static final String SIZE = "10";
    public static final long VALID_ID = 1L;
    public static final String CATEGORY_NOT_FOUND_MESSAGE = "Category not found";
    public static final String CATEGORIES_URL = "/categories";
    public static final String CATEGORY_BOOKS_URL = "/categories/{id}/books";

    private CategoryControllerUtils() {
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName("Fantasy")
                .setDescription("Books featuring magical worlds and epic adventures");
    }

    public static CategoryResponseDto createCategoryResponseDto(Long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName("Fantasy")
                .setDescription("Books featuring magical worlds and epic adventures");
    }

    public static CategoryRequestDto createUpdatedCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName("Epic Fantasy")
                .setDescription("High fantasy with grand narratives and mythical creatures");
    }

    public static CategoryResponseDto createUpdatedCategoryResponseDto(Long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName("Epic Fantasy")
                .setDescription("High fantasy with grand narratives and mythical creatures");
    }

    public static CategoryResponseDto createNonFictionCategoryResponseDto(Long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName("Biography")
                .setDescription("True stories of remarkable lives and achievements");
    }

    public static CategoryResponseDto createScienceCategoryResponseDto(Long id) {
        return new CategoryResponseDto()
                .setId(id)
                .setName("Science Fiction")
                .setDescription("Speculative fiction exploring futuristic technologies and space");
    }

    public static BookDtoWithoutCategoryIds createBookDto1() {
        return new BookDtoWithoutCategoryIds()
                .setId(1L)
                .setTitle("The Name of the Wind")
                .setAuthor("Patrick Rothfuss")
                .setIsbn("9780756404079")
                .setPrice(BigDecimal.valueOf(14.99))
                .setDescription("A young man's journey to become a legendary wizard")
                .setCoverImage("name-of-the-wind-cover.jpg");
    }

    public static BookDtoWithoutCategoryIds createBookDto2() {
        return new BookDtoWithoutCategoryIds()
                .setId(3L)
                .setTitle("Dune")
                .setAuthor("Frank Herbert")
                .setIsbn("9780441013593")
                .setPrice(BigDecimal.valueOf(18.99))
                .setDescription("A saga of politics and power on a desert planet")
                .setCoverImage("dune-cover.jpg");
    }

    public static Page<CategoryResponseDto> createCategoryPage(List<CategoryResponseDto> categories) {
        return new PageImpl<>(categories, PageRequest.of(0, 10),
                categories.size());
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

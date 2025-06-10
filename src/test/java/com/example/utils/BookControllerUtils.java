package com.example.utils;

import com.example.dto.book.BookDto;
import com.example.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class BookControllerUtils {

    public static final String SEARCH_URL = "/books/search";
    public static final String BOOKS_URL = "/books";
    public static final long INVALID_ID = 999L;
    public static final long FIRST_BOOK_ID = 1L;
    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "10";
    public static final long CATEGORY_ID = 1L;
    public static final int HTTP_OK_STATUS = 200;
    public static final int EXPECTED_BOOK_LIST_SIZE = 3;
    public static final int EXPECTED_SINGLE_ITEM_SIZE = 1;
    public static final String ADD_TEST_CATEGORIES_SCRIPT = "classpath:database/categories/add-test-categories.sql";
    public static final String REMOVE_TEST_CATEGORIES_SCRIPT = "classpath:database/categories/remove-test-categories.sql";
    public static final String CLEAR_BOOKS_SCRIPT = "classpath:database/books/clear-books.sql";
    public static final String ADD_CATEGORY_SCRIPT = "classpath:database/categories/add-category.sql";
    public static final String ADD_TEST_BOOKS_SCRIPT = "classpath:database/books/add-test-books.sql";

    private BookControllerUtils() {
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("New Book")
                .setAuthor("New Author")
                .setIsbn("9789876543210")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("A new book description")
                .setCoverImage("new_cover.jpg")
                .setCategoryIds(Set.of(CATEGORY_ID));
    }

    public static CreateBookRequestDto createUpdatedBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Test Book 2")
                .setAuthor("Test Author 2")
                .setIsbn("9999999999")
                .setPrice(BigDecimal.valueOf(39.99))
                .setDescription("Another test book")
                .setCategoryIds(Set.of(2L));
    }

    public static <T> T parseResponse(MvcResult result, Class<T> clazz, ObjectMapper objectMapper)
            throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }

    public static <T> T parseResponse(MvcResult result, TypeReference<T> typeReference,
                                      ObjectMapper objectMapper) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
    }

    public static List<BookDto> parsePagedBookResponse(MvcResult result, ObjectMapper objectMapper)
            throws Exception {
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        return objectMapper.readValue(jsonNode.get("content").toString(),
                new TypeReference<>() {
                });
    }
}

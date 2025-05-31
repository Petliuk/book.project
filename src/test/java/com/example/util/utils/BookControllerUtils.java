package com.example.util.utils;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParametersDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.util.constants.BookControllerConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MvcResult;
import java.util.Collections;
import java.util.List;

public class BookControllerUtils {
    // Prevent instantiation
    private BookControllerUtils() {
    }

    // Create test data
    public static BookDto createBookDto() {
        return new BookDto()
                .setId(BookControllerConstants.BOOK_ID)
                .setTitle(BookControllerConstants.BOOK_TITLE)
                .setAuthor(BookControllerConstants.BOOK_AUTHOR)
                .setIsbn(BookControllerConstants.BOOK_ISBN)
                .setPrice(BookControllerConstants.BOOK_PRICE)
                .setCategoryIds(Collections.singleton(BookControllerConstants.CATEGORY_ID));
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle(BookControllerConstants.BOOK_TITLE)
                .setAuthor(BookControllerConstants.BOOK_AUTHOR)
                .setIsbn(BookControllerConstants.BOOK_ISBN)
                .setPrice(BookControllerConstants.BOOK_PRICE)
                .setCategoryIds(Collections.singleton(BookControllerConstants.CATEGORY_ID));
    }

    public static BookSearchParametersDto createBookSearchParametersDto() {
        return new BookSearchParametersDto()
                .setTitle(BookControllerConstants.SEARCH_TITLE);
    }

    public static Page<BookDto> createBookPage(BookDto bookDto) {
        return new PageImpl<>(List.of(bookDto), PageRequest.of(
                Integer.parseInt(BookControllerConstants.PAGE), Integer.parseInt(BookControllerConstants.SIZE)), 1);
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

    public static List<BookDto> parsePagedBookResponse(MvcResult result, ObjectMapper objectMapper)
            throws Exception {
        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
        return objectMapper.readValue(jsonNode.get("content").toString(),
                new TypeReference<List<BookDto>>() {});
    }
}

package com.example.utils;

import com.example.dto.book.BookDto;
import com.example.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MvcResult;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class BookControllerUtils {

    public static final String SEARCH_URL = "/books/search";
    public static final String BOOKS_URL = "/books";
    public static final long INVALID_ID = 999L;
    public static final String BOOK_NOT_FOUND_MESSAGE = "Book not found";

    private BookControllerUtils() {
    }

    public static BookDto createBookDto(Long id) {
        return new BookDto()
                .setId(id)
                .setTitle("The Hobbit")
                .setAuthor("J.R.R. Tolkien")
                .setIsbn("9780547928227")
                .setPrice(BigDecimal.valueOf(12.99))
                .setDescription("A classic fantasy adventure in Middle-earth")
                .setCategoryIds(Set.of(1L));
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("The Hobbit")
                .setAuthor("J.R.R. Tolkien")
                .setIsbn("9780547928227")
                .setPrice(BigDecimal.valueOf(12.99))
                .setDescription("A classic fantasy adventure in Middle-earth")
                .setCategoryIds(Set.of(1L));
    }

    public static BookDto createUpdatedBookDto(Long id) {
        return new BookDto()
                .setId(id)
                .setTitle("The Fellowship of the Ring")
                .setAuthor("J.R.R. Tolkien")
                .setIsbn("9780547928210")
                .setPrice(BigDecimal.valueOf(15.99))
                .setDescription("The first part of The Lord of the Rings trilogy")
                .setCategoryIds(Set.of(1L));
    }

    public static CreateBookRequestDto createUpdatedBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("The Fellowship of the Ring")
                .setAuthor("J.R.R. Tolkien")
                .setIsbn("9780547928210")
                .setPrice(BigDecimal.valueOf(15.99))
                .setDescription("The first part of The Lord of the Rings trilogy")
                .setCategoryIds(Set.of(1L));
    }


    public static Page<BookDto> createBookPage(List<BookDto> books) {
        return new PageImpl<>(books, PageRequest.of(0, 10), books.size());
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

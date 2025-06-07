package com.example.controller;

import com.example.dto.book.BookDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.utils.BookControllerUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @DisplayName("Get all books when books exist")
    @Sql(scripts = BookControllerUtils.ADD_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.REMOVE_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_BooksExist_Success() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get(BookControllerUtils.BOOKS_URL)
                                .param("page", BookControllerUtils.PAGE_NUMBER)
                                .param("size", BookControllerUtils.PAGE_SIZE))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(BookControllerUtils.HTTP_OK_STATUS, result.getResponse().getStatus());

        // Then
        List<BookDto> actual = BookControllerUtils.parsePagedBookResponse(result, objectMapper);
        assertEquals(BookControllerUtils.EXPECTED_BOOK_LIST_SIZE, actual.size());
        assertThat(actual.get(0).getTitle(), equalTo("Test Book 1"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @DisplayName("Get book by valid ID")
    @Sql(scripts = BookControllerUtils.ADD_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.REMOVE_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_ValidId_Success() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get(BookControllerUtils.BOOKS_URL + "/{id}", BookControllerUtils.FIRST_BOOK_ID))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertThat(actual.getTitle(), equalTo("Test Book 1"));
        assertThat(actual.getAuthor(), equalTo("Test Author 1"));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @DisplayName("Get book by invalid ID")
    @Sql(scripts = BookControllerUtils.REMOVE_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.REMOVE_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_InvalidId_Fail() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get(BookControllerUtils.BOOKS_URL + "/{id}", BookControllerUtils.INVALID_ID))
                .andExpect(status().isNotFound())
                .andReturn();

        // Then
        assertTrue(result.getResolvedException() instanceof EntityNotFoundException);
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @DisplayName("Search books with valid parameters")
    @Sql(scripts = BookControllerUtils.ADD_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.REMOVE_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void searchBooks_ValidParameters_Success() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get(BookControllerUtils.SEARCH_URL)
                                .param("title", "Test Book 1"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookDto> actual = BookControllerUtils.parseResponse(
                result, new TypeReference<>() {
                }, objectMapper);
        assertThat(actual, hasSize(BookControllerUtils.EXPECTED_SINGLE_ITEM_SIZE));
        assertThat(actual.get(0).getTitle(), equalTo("Test Book 1"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Create book with valid request")
    @Sql(scripts = BookControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequest_Success() throws Exception {
        // Given
        CreateBookRequestDto request = BookControllerUtils.createBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // When
        MvcResult result = mockMvc.perform(
                        post(BookControllerUtils.BOOKS_URL)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertThat(actual.getTitle(), equalTo("New Book"));
        assertThat(actual.getAuthor(), equalTo("New Author"));
        assertThat(actual.getIsbn(), equalTo("9789876543210"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Update book with valid ID")
    @Sql(scripts = BookControllerUtils.ADD_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.REMOVE_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_ValidId_Success() throws Exception {
        // Given
        CreateBookRequestDto request = BookControllerUtils.createUpdatedBookRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(request);

        // When
        MvcResult result = mockMvc.perform(
                        put(BookControllerUtils.BOOKS_URL + "/{id}", BookControllerUtils.FIRST_BOOK_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertThat(actual.getTitle(), equalTo("Test Book 2"));
        assertThat(actual.getAuthor(), equalTo("Test Author 2"));
        assertThat(actual.getIsbn(), equalTo("9999999999"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Delete book with valid ID")
    @Sql(scripts = BookControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.ADD_TEST_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteBook_ValidId_Success() throws Exception {
        // When
        mockMvc.perform(delete(BookControllerUtils.BOOKS_URL + "/{id}", BookControllerUtils.FIRST_BOOK_ID))
                .andExpect(status().isNoContent());

        // Then
        MvcResult result = mockMvc.perform(get(BookControllerUtils.BOOKS_URL + "/{id}", BookControllerUtils.FIRST_BOOK_ID))
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof EntityNotFoundException);
    }
}

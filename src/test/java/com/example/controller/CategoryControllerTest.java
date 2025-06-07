package com.example.controller;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.exception.EntityNotFoundException;
import com.example.utils.CategoryControllerUtils;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

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
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Create category with valid request")
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequest_Success() throws Exception {
        // Given
        CategoryRequestDto requestDto = CategoryControllerUtils.createCategoryRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                        post(CategoryControllerUtils.CATEGORIES_URL)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CategoryResponseDto actual = CategoryControllerUtils.parseResponse(
                result, CategoryResponseDto.class, objectMapper);
        assertNotNull(actual);
        assertThat(actual.getName(), equalTo(CategoryControllerUtils.FANTASY_CATEGORY_NAME));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @DisplayName("Get all categories with valid size")
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_CategoriesExist_Success() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get(CategoryControllerUtils.CATEGORIES_URL)
                                .param("page", CategoryControllerUtils.PAGE)
                                .param("size", CategoryControllerUtils.SIZE))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<CategoryResponseDto> actual = CategoryControllerUtils.parsePagedCategoryResponse(result, objectMapper);
        assertNotNull(actual);
        assertThat(actual, hasSize(CategoryControllerUtils.EXPECTED_CATEGORY_LIST_SIZE));
        assertThat(actual.get(0).getName(), equalTo(CategoryControllerUtils.FICTION_CATEGORY_NAME));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @DisplayName("Get category by valid ID")
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_ValidId_Success() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get(CategoryControllerUtils.CATEGORIES_URL + "/{id}", CategoryControllerUtils.VALID_ID))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = CategoryControllerUtils.parseResponse(
                result, CategoryResponseDto.class, objectMapper);
        assertNotNull(actual);
        assertThat(actual.getName(), equalTo(CategoryControllerUtils.FICTION_CATEGORY_NAME));
        assertThat(actual.getDescription(), equalTo(CategoryControllerUtils.FICTION_CATEGORY_DESCRIPTION));
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @DisplayName("Get category by invalid ID")
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCategoryById_InvalidId_Fail() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get(CategoryControllerUtils.CATEGORIES_URL + "/{id}", CategoryControllerUtils.INVALID_ID))
                .andExpect(status().isNotFound())
                .andReturn();

        // Then
        assertTrue(result.getResolvedException() instanceof EntityNotFoundException);
        assertEquals(CategoryControllerUtils.CATEGORY_NOT_FOUND_MESSAGE, result.getResolvedException().getMessage());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Update category by valid ID")
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidId_Success() throws Exception {
        // Given
        CategoryRequestDto requestDto = CategoryControllerUtils.createUpdatedCategoryRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                        put(CategoryControllerUtils.CATEGORIES_URL + "/{id}", CategoryControllerUtils.VALID_ID)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = CategoryControllerUtils.parseResponse(
                result, CategoryResponseDto.class, objectMapper);
        assertNotNull(actual);
        assertThat(actual.getName(), equalTo(CategoryControllerUtils.EPIC_FANTASY_CATEGORY_NAME));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    @DisplayName("Delete category by valid ID")
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.ADD_CATEGORY_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategoryById_ValidId_Success() throws Exception {
        // When
        mockMvc.perform(
                delete(CategoryControllerUtils.CATEGORIES_URL + "/{id}", CategoryControllerUtils.VALID_ID))
                .andExpect(status().isNoContent());

        // Then
        MvcResult result = mockMvc.perform(
                        get(CategoryControllerUtils.CATEGORIES_URL + "/{id}", CategoryControllerUtils.VALID_ID))
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof EntityNotFoundException);
        assertEquals(CategoryControllerUtils.CATEGORY_NOT_FOUND_MESSAGE, result.getResolvedException().getMessage());
    }

    @Test
    @WithMockUser(username = "user", authorities = "USER")
    @DisplayName("Get books by category ID with valid ID")
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.ADD_TEST_CATEGORIES_SCRIPT, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = CategoryControllerUtils.CLEAR_BOOKS_SCRIPT, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_ValidId_Success() throws Exception {
        // When
        MvcResult result = mockMvc.perform(
                        get(CategoryControllerUtils.CATEGORY_BOOKS_URL, CategoryControllerUtils.VALID_ID)
                                .param("page", CategoryControllerUtils.PAGE)
                                .param("size", CategoryControllerUtils.SIZE)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookDtoWithoutCategoryIds> actual = CategoryControllerUtils.parseResponse(
                result, new TypeReference<>() {
                }, objectMapper);
        assertNotNull(actual);
        assertThat(actual, hasSize(CategoryControllerUtils.EXPECTED_BOOK_LIST_SIZE));
        assertThat(actual.get(0).getTitle(), equalTo(CategoryControllerUtils.TEST_BOOK_TITLE));
    }
}

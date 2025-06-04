package com.example.controller;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.exception.EntityNotFoundException;
import com.example.service.CategoryService;
import com.example.utils.CategoryControllerUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setFallbackPageable(PageRequest.of(0, 10));
        mockMvc = MockMvcBuilders
                .standaloneSetup(categoryController)
                .setCustomArgumentResolvers(pageableResolver)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Create category with valid request")
    void createCategory_ValidRequest_Success() throws Exception {
        // Given
        CategoryRequestDto requestDto = CategoryControllerUtils.createCategoryRequestDto();
        CategoryResponseDto expected = CategoryControllerUtils.createCategoryResponseDto(CategoryControllerUtils.VALID_ID);
        when(categoryService.save(any(CategoryRequestDto.class))).thenReturn(expected);
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
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get all categories with valid size")
    void getAll_CategoriesExist_Success() throws Exception {
        // Given
        CategoryResponseDto category1 = CategoryControllerUtils.createCategoryResponseDto(1L);
        CategoryResponseDto category2 = CategoryControllerUtils.createNonFictionCategoryResponseDto(2L);
        CategoryResponseDto category3 = CategoryControllerUtils.createScienceCategoryResponseDto(3L);
        List<CategoryResponseDto> categories = List.of(category1, category2, category3);
        Page<CategoryResponseDto> page = CategoryControllerUtils.createCategoryPage(categories);
        when(categoryService.findAll(any(Pageable.class))).thenReturn(page);

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
        assertEquals(3, actual.size());
        assertEquals(categories, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get category by valid ID")
    void getCategoryById_ValidId_Success() throws Exception {
        // Given
        CategoryResponseDto expected = CategoryControllerUtils.createCategoryResponseDto(CategoryControllerUtils.VALID_ID);
        when(categoryService.getById(CategoryControllerUtils.VALID_ID)).thenReturn(expected);

        // When
        MvcResult result = mockMvc.perform(
                        get(CategoryControllerUtils.CATEGORIES_URL + "/{id}", CategoryControllerUtils.VALID_ID))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = CategoryControllerUtils.parseResponse(
                result, CategoryResponseDto.class, objectMapper);
        assertNotNull(actual);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get category by invalid ID")
    void getCategoryById_InvalidId_Fail() throws Exception {
        // Given
        when(categoryService.getById(-1L))
                .thenThrow(new EntityNotFoundException(CategoryControllerUtils.CATEGORY_NOT_FOUND_MESSAGE));

        // When
        MvcResult result = mockMvc.perform(
                        get(CategoryControllerUtils.CATEGORIES_URL + "/{id}", -1L))
                .andExpect(status().isNotFound())
                .andReturn();

        // Then
        assertTrue(result.getResolvedException() instanceof EntityNotFoundException);
        assertEquals(CategoryControllerUtils.CATEGORY_NOT_FOUND_MESSAGE, result.getResolvedException().getMessage());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Update category by valid ID")
    void updateCategory_ValidId_Success() throws Exception {
        // Given
        CategoryRequestDto requestDto = CategoryControllerUtils.createUpdatedCategoryRequestDto();
        CategoryResponseDto expected = CategoryControllerUtils.createUpdatedCategoryResponseDto(CategoryControllerUtils.VALID_ID);
        when(categoryService.update(eq(CategoryControllerUtils.VALID_ID), any(CategoryRequestDto.class))).thenReturn(expected);
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
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Delete category by valid ID")
    void deleteCategoryById_ValidId_Success() throws Exception {
        // Given
        when(categoryService.getById(CategoryControllerUtils.VALID_ID))
                .thenThrow(new EntityNotFoundException(CategoryControllerUtils.CATEGORY_NOT_FOUND_MESSAGE));

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
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get books by category ID with valid ID")
    void getBooksByCategoryId_ValidId_Success() throws Exception {
        // Given
        List<BookDtoWithoutCategoryIds> expected = List.of(
                CategoryControllerUtils.createBookDto1(),
                CategoryControllerUtils.createBookDto2());
        when(categoryService.getBooksByCategoryId(eq(CategoryControllerUtils.VALID_ID), any(Pageable.class)))
                .thenReturn(expected);

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
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }
}

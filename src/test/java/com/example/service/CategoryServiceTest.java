package com.example.service;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.mapper.BookMapper;
import com.example.mapper.CategoryMapper;
import com.example.model.Book;
import com.example.model.Category;
import com.example.repository.book.BookRepository;
import com.example.repository.category.CategoryRepository;
import com.example.service.impl.CategoryServiceImpl;
import com.example.util.constants.CategoryServiceConstants;
import com.example.util.utils.CategoryServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryResponseDto responseDto;
    private CategoryRequestDto requestDto;
    private Book book;
    private BookDtoWithoutCategoryIds bookDto;

    @BeforeEach
    void setUp() {
        category = CategoryServiceUtils.createCategory();
        responseDto = CategoryServiceUtils.createCategoryResponseDto();
        requestDto = CategoryServiceUtils.createCategoryRequestDto();
        book = CategoryServiceUtils.createBook();
        bookDto = CategoryServiceUtils.createBookDtoWithoutCategoryIds();
    }

    @Test
    @DisplayName("Save category when valid request returns category DTO")
    void save_ValidRequest_ReturnsCategoryDto() {
        // Given:
        when(categoryMapper.toEntity(any(CategoryRequestDto.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponseDto(any(Category.class))).thenReturn(responseDto);

        // When:
        CategoryResponseDto result = categoryService.save(requestDto);

        // Then:
        assertEquals(CategoryServiceConstants.CATEGORY_NAME, result.getName(),
                CategoryServiceConstants.NAME_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Get category by ID when category exists returns category DTO")
    void getById_CategoryExists_ReturnsCategoryDto() {
        // Given:
        when(categoryRepository.findById(CategoryServiceConstants.CATEGORY_ID))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(any(Category.class))).thenReturn(responseDto);

        // When:
        CategoryResponseDto result = categoryService.getById(CategoryServiceConstants.CATEGORY_ID);

        // Then:
        assertEquals(CategoryServiceConstants.CATEGORY_NAME, result.getName(),
                CategoryServiceConstants.NAME_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Find all categories when categories exist returns category page")
    void findAll_CategoriesExist_ReturnsCategoryPage() {
        // Given:
        PageRequest pageable = PageRequest.of(CategoryServiceConstants.PAGE,
                CategoryServiceConstants.SIZE);
        Page<Category> page = CategoryServiceUtils.createCategoryPage(category);
        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toResponseDto(any(Category.class))).thenReturn(responseDto);

        // When:
        Page<CategoryResponseDto> result = categoryService.findAll(pageable);

        // Then:
        assertEquals(1, result.getTotalElements(), CategoryServiceConstants.EXPECTED_COUNT_MESSAGE);
        assertEquals(CategoryServiceConstants.CATEGORY_NAME, result.getContent().get(0).getName(),
                CategoryServiceConstants.NAME_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Update category when category exists returns updated category DTO")
    void update_CategoryExists_ReturnsUpdatedCategoryDto() {
        // Given:
        when(categoryRepository.findById(CategoryServiceConstants.CATEGORY_ID))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponseDto(any(Category.class))).thenReturn(responseDto);
        doNothing().when(categoryMapper).updateCategoryFromDto(any(CategoryRequestDto.class),
                any(Category.class));

        // When:
        CategoryResponseDto result = categoryService.update(CategoryServiceConstants.CATEGORY_ID,
                requestDto);

        // Then:
        assertEquals(CategoryServiceConstants.CATEGORY_NAME, result.getName(),
                CategoryServiceConstants.NAME_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Delete category by ID when category exists deletes category")
    void deleteById_CategoryExists_DeletesCategory() {
        // Given:
        doNothing().when(categoryRepository).deleteById(CategoryServiceConstants.CATEGORY_ID);

        // When:
        categoryService.deleteById(CategoryServiceConstants.CATEGORY_ID);

        // Then:
        verify(categoryRepository).deleteById(CategoryServiceConstants.CATEGORY_ID);
    }

    @Test
    @DisplayName("Get books by category ID when books exist returns book DTO list")
    void getBooksByCategoryId_BooksExist_ReturnsBookDtoList() {
        // Given:
        PageRequest pageable = PageRequest.of(CategoryServiceConstants.PAGE,
                CategoryServiceConstants.SIZE);
        when(bookRepository.findAllByCategoriesId(CategoryServiceConstants.CATEGORY_ID, pageable))
                .thenReturn(Collections.singletonList(book));
        when(bookMapper.toDtoWithoutCategories(any(Book.class))).thenReturn(bookDto);

        // When:
        List<BookDtoWithoutCategoryIds> result = categoryService.getBooksByCategoryId(
                CategoryServiceConstants.CATEGORY_ID, pageable);

        // Then:
        assertEquals(1, result.size(), CategoryServiceConstants.EXPECTED_COUNT_MESSAGE);
        assertEquals(CategoryServiceConstants.BOOK_TITLE, result.get(0).getTitle(),
                CategoryServiceConstants.TITLE_MATCH_MESSAGE);
    }
}
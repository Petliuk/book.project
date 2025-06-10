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
import com.example.utils.CategoryServiceUtils;
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
        // Given
        when(categoryMapper.toEntity(any(CategoryRequestDto.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponseDto(any(Category.class))).thenReturn(responseDto);

        // When
        CategoryResponseDto result = categoryService.save(requestDto);

        // Then
        assertEquals(CategoryServiceUtils.CATEGORY_NAME, result.getName(),
                CategoryServiceUtils.NAME_MESSAGE);
    }

    @Test
    @DisplayName("Get category by ID when category exists returns category DTO")
    void getById_CategoryExists_ReturnsCategoryDto() {
        // Given
        when(categoryRepository.findById(CategoryServiceUtils.CATEGORY_ID))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(any(Category.class))).thenReturn(responseDto);

        // When
        CategoryResponseDto result = categoryService.getById(CategoryServiceUtils.CATEGORY_ID);

        // Then
        assertEquals(CategoryServiceUtils.CATEGORY_NAME, result.getName(),
                CategoryServiceUtils.NAME_MESSAGE);
    }

    @Test
    @DisplayName("Find all categories when categories exist returns category page")
    void findAll_CategoriesExist_ReturnsCategoryPage() {
        // Given
        PageRequest pageable = PageRequest.of(CategoryServiceUtils.PAGE, CategoryServiceUtils.SIZE);
        Page<Category> page = CategoryServiceUtils.createCategoryPage(category);
        when(categoryRepository.findAll(pageable)).thenReturn(page);
        when(categoryMapper.toResponseDto(any(Category.class))).thenReturn(responseDto);

        // When
        Page<CategoryResponseDto> result = categoryService.findAll(pageable);

        // Then
        assertEquals(CategoryServiceUtils.CATEGORY_NAME, result.getContent().get(0).getName(),
                CategoryServiceUtils.NAME_MESSAGE);
    }

    @Test
    @DisplayName("Update category when category exists returns updated category DTO")
    void update_CategoryExists_ReturnsUpdatedCategoryDto() {
        // Given
        when(categoryRepository.findById(CategoryServiceUtils.CATEGORY_ID))
                .thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponseDto(any(Category.class))).thenReturn(responseDto);

        // When
        CategoryResponseDto result = categoryService.update(CategoryServiceUtils.CATEGORY_ID,
                requestDto);

        // Then
        assertEquals(CategoryServiceUtils.CATEGORY_NAME, result.getName(),
                CategoryServiceUtils.NAME_MESSAGE);
    }

    @Test
    @DisplayName("Delete category by ID when category exists deletes category")
    void deleteById_CategoryExists_DeletesCategory() {

        // When
        categoryService.deleteById(CategoryServiceUtils.CATEGORY_ID);

        // Then
        verify(categoryRepository).deleteById(CategoryServiceUtils.CATEGORY_ID);
    }

    @Test
    @DisplayName("Get books by category ID when books exist returns book DTO list")
    void getBooksByCategoryId_BooksExist_ReturnsBookDtoList() {
        // Given
        PageRequest pageable = PageRequest.of(CategoryServiceUtils.PAGE, CategoryServiceUtils.SIZE);
        when(bookRepository.findAllByCategoriesId(CategoryServiceUtils.CATEGORY_ID, pageable))
                .thenReturn(Collections.singletonList(book));
        when(bookMapper.toDtoWithoutCategories(any(Book.class))).thenReturn(bookDto);

        // When
        List<BookDtoWithoutCategoryIds> result = categoryService.getBooksByCategoryId(
                CategoryServiceUtils.CATEGORY_ID, pageable);

        // Then
        assertEquals(CategoryServiceUtils.BOOK_TITLE, result.get(0).getTitle(),
                CategoryServiceUtils.TITLE_MESSAGE);
    }
}

package com.example.util.utils;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.model.Book;
import com.example.model.Category;
import com.example.util.constants.CategoryServiceConstants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Collections;

public final class CategoryServiceUtils {

    // Prevent instantiation
    private CategoryServiceUtils() {
    }

    // Create test data
    public static Category createCategory() {
        Category category = new Category();
        category.setId(CategoryServiceConstants.CATEGORY_ID);
        category.setName(CategoryServiceConstants.CATEGORY_NAME);
        return category;
    }

    public static CategoryResponseDto createCategoryResponseDto() {
        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(CategoryServiceConstants.CATEGORY_ID);
        responseDto.setName(CategoryServiceConstants.CATEGORY_NAME);
        return responseDto;
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName(CategoryServiceConstants.CATEGORY_NAME);
        return requestDto;
    }

    public static Book createBook() {
        Book book = new Book();
        book.setId(CategoryServiceConstants.BOOK_ID);
        book.setTitle(CategoryServiceConstants.BOOK_TITLE);
        book.setAuthor(CategoryServiceConstants.BOOK_AUTHOR);
        book.setIsbn(CategoryServiceConstants.BOOK_ISBN);
        book.setPrice(CategoryServiceConstants.BOOK_PRICE);
        return book;
    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(CategoryServiceConstants.BOOK_ID);
        bookDto.setTitle(CategoryServiceConstants.BOOK_TITLE);
        bookDto.setAuthor(CategoryServiceConstants.BOOK_AUTHOR);
        bookDto.setIsbn(CategoryServiceConstants.BOOK_ISBN);
        bookDto.setPrice(CategoryServiceConstants.BOOK_PRICE);
        return bookDto;
    }

    public static Page<Category> createCategoryPage(Category category) {
        return new PageImpl<>(Collections.singletonList(category),
                PageRequest.of(CategoryServiceConstants.PAGE, CategoryServiceConstants.SIZE), 1);
    }
}

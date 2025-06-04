package com.example.utils;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.model.Book;
import com.example.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Collections;

public final class CategoryServiceUtils {

    public static final Long CATEGORY_ID = 1L;
    public static final String CATEGORY_NAME = "Fiction";
    public static final String CATEGORY_DESCRIPTION = "Fiction books";
    public static final Long BOOK_ID = 1L;
    public static final String BOOK_TITLE = "Test Book";
    public static final int PAGE = 0;
    public static final int SIZE = 10;
    public static final String NAME_MESSAGE = "Category name doesn't match";
    public static final String TITLE_MESSAGE = "Book title doesn't match";

    private CategoryServiceUtils() {
    }

    public static Category createCategory() {
        Category category = new Category();
        category.setId(CATEGORY_ID);
        category.setName(CATEGORY_NAME);
        category.setDescription(CATEGORY_DESCRIPTION);
        return category;
    }

    public static CategoryResponseDto createCategoryResponseDto() {
        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(CATEGORY_ID);
        responseDto.setName(CATEGORY_NAME);
        responseDto.setDescription(CATEGORY_DESCRIPTION);
        return responseDto;
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName(CATEGORY_NAME);
        requestDto.setDescription(CATEGORY_DESCRIPTION);
        return requestDto;
    }

    public static Book createBook() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        return book;
    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(BOOK_ID);
        bookDto.setTitle(BOOK_TITLE);
        return bookDto;
    }

    public static Page<Category> createCategoryPage(Category category) {
        return new PageImpl<>(Collections.singletonList(category),
                PageRequest.of(PAGE, SIZE), 1);
    }
}

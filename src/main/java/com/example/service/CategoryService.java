package com.example.service;

import com.example.dto.BookDtoWithoutCategoryIds;
import com.example.dto.CategoryDto;
import java.util.List;

public interface CategoryService {

    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id);
}

package com.example.service;

import com.example.dto.BookDtoWithoutCategoryIds;
import com.example.dto.CategoryRequestDto;
import com.example.dto.CategoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id, Pageable pageable);
}

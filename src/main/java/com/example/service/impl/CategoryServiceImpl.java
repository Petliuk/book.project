package com.example.service.impl;

import com.example.dto.BookDtoWithoutCategoryIds;
import com.example.dto.CategoryDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.BookMapper;
import com.example.mapper.CategoryMapper;
import com.example.model.Category;
import com.example.repository.book.BookRepository;
import com.example.repository.category.CategoryRepository;
import com.example.service.CategoryService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found")));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find category by id " + id)
        );
        return categoryMapper.toDto(categoryRepository.save(category));

    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategoriesId(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}

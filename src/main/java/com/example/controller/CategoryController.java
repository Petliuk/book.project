package com.example.controller;

import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.category.CategoryRequestDto;
import com.example.dto.category.CategoryResponseDto;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Category Controller",
        description = "Manage category with CRUD operations and search functionality")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get all categories",
            description = "Retrieve a list of all categories")
    @GetMapping
    public Page<CategoryResponseDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "Get category by ID",
            description = "Retrieve details of a category by its ID")
    @GetMapping("/{id}")
    public CategoryResponseDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Create a new category",
            description = "Add a new category to the system")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@RequestBody @Valid CategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Operation(summary = "Update an existing category",
            description = "Update the details of a category by ID")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CategoryResponseDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid CategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete a category",
            description = "Remove a category from the system by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get books by category ID",
            description = "Retrieve books by a specific category")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id,
                                                                Pageable pageable) {
        return categoryService.getBooksByCategoryId(id, pageable);
    }
}

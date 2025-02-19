package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.CategoryRequestDto;
import com.example.dto.CategoryResponseDto;
import com.example.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toEntity(CategoryRequestDto categoryDto);

    CategoryResponseDto toResponseDto(Category category);

    void updateCategoryFromDto(CategoryRequestDto categoryDto, @MappingTarget Category category);
}

package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.CategoryDto;
import com.example.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category category);
}

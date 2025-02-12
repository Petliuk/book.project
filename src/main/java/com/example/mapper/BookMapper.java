package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.BookDto;
import com.example.dto.BookDtoWithoutCategoryIds;
import com.example.dto.CreateBookRequestDto;
import com.example.model.Book;
import com.example.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toEntity(CreateBookRequestDto requestDto);

    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    default Set<Category> map(List<String> categoryNames) {
        if (categoryNames == null) {
            return null;
        }
        return categoryNames.stream()
                .map(Category::new)
                .collect(Collectors.toSet());
    }

    default List<String> map(Set<Category> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }

}

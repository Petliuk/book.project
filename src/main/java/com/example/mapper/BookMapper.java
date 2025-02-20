package com.example.mapper;

import com.example.config.MapperConfig;
import com.example.dto.book.BookDto;
import com.example.dto.book.BookDtoWithoutCategoryIds;
import com.example.dto.book.CreateBookRequestDto;
import com.example.model.Book;
import com.example.model.Category;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toEntity(CreateBookRequestDto requestDto);

    @Mapping(source = "categories", target = "categoryIds", qualifiedByName = "setCategoryIds")
    BookDto toDto(Book book);

    @Mapping(target = "categories", ignore = true)
    void updateBookFromDto(CreateBookRequestDto bookDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    default Set<Category> categoryById(Set<Long> categoryIds) {
        if (categoryIds != null) {
            return categoryIds.stream()
                    .map(Category::new)
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }

    @Named("setCategoryIds")
    default Set<Long> setCategoryIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
    }
}

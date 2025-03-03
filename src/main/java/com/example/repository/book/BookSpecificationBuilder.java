package com.example.repository.book;

import com.example.dto.book.BookSearchParametersDto;
import com.example.model.Book;
import com.example.repository.SpecificationBuilder;
import com.example.repository.SpecificationProviderManager;
import com.example.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> spec = Specification.where(null);
        if (bookSearchParametersDto.getAuthor() != null
                && bookSearchParametersDto.getAuthor().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(Constants.AUTHOR)
                    .getSpecification(bookSearchParametersDto.getAuthor()));
        }
        if (bookSearchParametersDto.getTitle() != null
                && bookSearchParametersDto.getTitle().length > 0) {
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider(Constants.TITLE)
                    .getSpecification(bookSearchParametersDto.getTitle()));
        }
        return spec;
    }
}

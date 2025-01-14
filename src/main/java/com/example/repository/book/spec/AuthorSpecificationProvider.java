package com.example.repository.book.spec;

import com.example.model.Book;
import com.example.repository.SpecificationProvider;
import com.example.util.Constants;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public String getKey() {
        return Constants.AUTHOR;
    }

    public Specification<Book> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder)
                -> root.get(Constants.AUTHOR).in(Arrays.stream(params).toArray()));
    }
}

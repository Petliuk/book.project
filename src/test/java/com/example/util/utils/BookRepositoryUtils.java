package com.example.util.utils;

import com.example.model.Book;
import com.example.repository.book.spec.AuthorSpecificationProvider;
import com.example.util.constants.BookRepositoryConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public final class BookRepositoryUtils {

    // Prevent instantiation
    private BookRepositoryUtils() {
    }

    // Create test data
    public static Book createNewBook() {
        Book book = new Book();
        book.setTitle(BookRepositoryConstants.NEW_BOOK_TITLE);
        book.setAuthor(BookRepositoryConstants.NEW_AUTHOR);
        book.setIsbn(BookRepositoryConstants.NEW_ISBN);
        book.setPrice(BookRepositoryConstants.NEW_BOOK_PRICE);
        book.setDeleted(false);
        return book;
    }

    // Create pagination
    public static PageRequest createPageRequest() {
        return PageRequest.of(BookRepositoryConstants.PAGE, BookRepositoryConstants.SIZE);
    }

    // Create specification
    public static Specification<Book> createAuthorSpecification() {
        AuthorSpecificationProvider specProvider = new AuthorSpecificationProvider();
        return specProvider.getSpecification(BookRepositoryConstants.TEST_AUTHOR_ARRAY);
    }
}

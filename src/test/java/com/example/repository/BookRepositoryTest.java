package com.example.repository;

import com.example.model.Book;
import com.example.repository.book.BookRepository;
import com.example.utils.BookRepositoryUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category ID when category exists returns book list")
    @Sql(scripts = {BookRepositoryUtils.ADD_TEST_BOOKS_SQL, BookRepositoryUtils.ADD_TEST_CATEGORIES_SQL},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {BookRepositoryUtils.DELETE_BOOK_SQL, BookRepositoryUtils.REMOVE_TEST_CATEGORIES_SQL},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoriesId_CategoryExists_ReturnsBookList() {
        // Given
        Long categoryId = BookRepositoryUtils.CATEGORY_ID;
        PageRequest pageable = BookRepositoryUtils.createPageRequest();

        // When:
        List<Book> books = bookRepository.findAllByCategoriesId(categoryId, pageable);

        // Then
        assertEquals(2, books.size(), String.format(BookRepositoryUtils.EXPECTED_BOOK_COUNT_MESSAGE, 2));
        assertEquals(BookRepositoryUtils.TEST_BOOK_1_TITLE, books.get(0).getTitle(),
                BookRepositoryUtils.BOOK_TITLE_MATCH_MESSAGE);
    }


    @Test
    @DisplayName("Find all books when books exist returns book page")
    @Sql(scripts = BookRepositoryUtils.ADD_TEST_BOOKS_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookRepositoryUtils.DELETE_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_BooksExist_ReturnsBookPage() {
        // Given
        PageRequest pageable = BookRepositoryUtils.createPageRequest();

        // When
        Page<Book> books = bookRepository.findAll(pageable);

        // Then
        assertEquals(2, books.getTotalElements(), String.format(BookRepositoryUtils.EXPECTED_BOOK_COUNT_MESSAGE, 2));
        assertEquals(BookRepositoryUtils.TEST_BOOK_TITLE, books.getContent().get(0).getTitle(),
                BookRepositoryUtils.BOOK_TITLE_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Find book by ID when book exists returns book")
    @Sql(scripts = BookRepositoryUtils.ADD_TEST_BOOKS_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookRepositoryUtils.DELETE_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_BookExists_ReturnsBook() {
        // Given
        Long bookId = BookRepositoryUtils.BOOK_ID;

        // When
        Optional<Book> book = bookRepository.findById(bookId);

        // Then
        assertTrue(book.isPresent(), BookRepositoryUtils.BOOK_PRESENT_MESSAGE);
        assertEquals(BookRepositoryUtils.TEST_BOOK_TITLE, book.get().getTitle(),
                BookRepositoryUtils.BOOK_TITLE_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Save book when valid book returns saved book")
    @Sql(scripts = BookRepositoryUtils.DELETE_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ValidBook_ReturnsSavedBook() {
        // Given
        Book book = BookRepositoryUtils.createNewBook();

        // When
        Book savedBook = bookRepository.save(book);

        // Then
        assertEquals(BookRepositoryUtils.NEW_BOOK_TITLE, savedBook.getTitle(),
                BookRepositoryUtils.BOOK_TITLE_MATCH_MESSAGE);
        assertEquals(BookRepositoryUtils.NEW_AUTHOR, savedBook.getAuthor(),
                BookRepositoryUtils.BOOK_AUTHOR_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Delete book by ID when book exists marks book as deleted")
    @Sql(scripts = BookRepositoryUtils.ADD_TEST_BOOKS_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookRepositoryUtils.DELETE_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteById_BookExists_MarksBookAsDeleted() {
        // Given
        Long bookId = BookRepositoryUtils.BOOK_ID;

        // When
        bookRepository.deleteById(bookId);

        // Then
        Optional<Book> book = bookRepository.findById(bookId);
        assertFalse(book.isPresent(), BookRepositoryUtils.BOOK_NOT_PRESENT_MESSAGE);
    }

    @Test
    @DisplayName("Find all books by specification when author matches returns book list")
    @Sql(scripts = BookRepositoryUtils.ADD_TEST_BOOKS_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = BookRepositoryUtils.DELETE_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllBySpecification_AuthorMatches_ReturnsBookList() {
        // Given
        Specification<Book> spec = BookRepositoryUtils.createAuthorSpecification();

        // When
        List<Book> books = bookRepository.findAll(spec);

        // Then
        assertEquals(1, books.size(), "Should return 1 book");
        assertEquals(BookRepositoryUtils.TEST_BOOK_TITLE, books.get(0).getTitle(),
                BookRepositoryUtils.BOOK_TITLE_MATCH_MESSAGE);
    }
}

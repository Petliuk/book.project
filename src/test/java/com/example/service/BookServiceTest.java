package com.example.service;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParametersDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.mapper.BookMapper;
import com.example.model.Book;
import com.example.repository.book.BookRepository;
import com.example.repository.book.BookSpecificationBuilder;
import com.example.repository.category.CategoryRepository;
import com.example.service.impl.BookServiceImpl;
import com.example.utils.BookServiceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto requestDto;

    @BeforeEach
    void setUp() {
        book = BookServiceUtils.createBook();
        bookDto = BookServiceUtils.createBookDto();
        requestDto = BookServiceUtils.createBookRequestDto();
    }

    @Test
    @DisplayName("Create book when valid request returns book DTO")
    void createBook_ValidRequest_ReturnsBookDto() {
        // Given
        when(bookMapper.toEntity(any(CreateBookRequestDto.class))).thenReturn(book);
        when(categoryRepository.findAllById(any())).thenReturn(Collections.singletonList(BookServiceUtils.createCategory()));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        // When
        BookDto result = bookService.createBook(requestDto);

        // Then
        assertEquals(BookServiceUtils.BOOK_TITLE, result.getTitle(), BookServiceUtils.BOOK_TITLE_MESSAGE);
    }

    @Test
    @DisplayName("Get book by ID when book exists returns book DTO")
    void getBookById_BookExists_ReturnsBookDto() {
        // Given
        when(bookRepository.findById(BookServiceUtils.BOOK_ID)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        // When
        BookDto result = bookService.getBookById(BookServiceUtils.BOOK_ID);

        // Then
        assertEquals(BookServiceUtils.BOOK_TITLE, result.getTitle(), BookServiceUtils.BOOK_TITLE_MESSAGE);
    }

    @Test
    @DisplayName("Get all books when books exist returns book page")
    void getAll_BooksExist_ReturnsBookPage() {
        // Given
        PageRequest pageable = PageRequest.of(BookServiceUtils.PAGE, BookServiceUtils.SIZE);
        Page<Book> page = BookServiceUtils.createBookPage(book);
        when(bookRepository.findAll(pageable)).thenReturn(page);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        // When
        Page<BookDto> result = bookService.getAll(pageable);

        // Then
        assertEquals(1, result.getTotalElements(), BookServiceUtils.EXPECTED_BOOK_COUNT_MESSAGE);
        assertEquals(BookServiceUtils.BOOK_TITLE, result.getContent().get(0).getTitle(),
                BookServiceUtils.BOOK_TITLE_MESSAGE);
    }

    @Test
    @DisplayName("Update book when book exists returns updated book DTO")
    void updateBook_BookExists_ReturnsUpdatedBookDto() {
        // Given
        when(bookRepository.findById(BookServiceUtils.BOOK_ID)).thenReturn(Optional.of(book));
        when(categoryRepository.findAllById(any())).thenReturn(Collections.singletonList(BookServiceUtils.createCategory()));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        // When
        BookDto result = bookService.updateBook(BookServiceUtils.BOOK_ID, requestDto);

        // Then
        assertEquals(BookServiceUtils.BOOK_TITLE, result.getTitle(), BookServiceUtils.BOOK_TITLE_MESSAGE);
    }

    @Test
    @DisplayName("Search books when parameters match returns book list")
    void search_ParametersMatch_ReturnsBookList() {
        // Given
        BookSearchParametersDto params = BookServiceUtils.createBookSearchParametersDto();
        Specification<Book> spec = BookServiceUtils.createMockSpecification();
        when(bookSpecificationBuilder.build(any(BookSearchParametersDto.class))).thenReturn(spec);
        when(bookRepository.findAll(spec)).thenReturn(Collections.singletonList(book));
        when(bookMapper.toDto(any(Book.class))).thenReturn(bookDto);

        // When
        List<BookDto> result = bookService.search(params);

        // Then
        assertEquals(1, result.size(), BookServiceUtils.EXPECTED_BOOK_COUNT_MESSAGE);
        assertEquals(BookServiceUtils.BOOK_TITLE, result.get(0).getTitle(), BookServiceUtils.BOOK_TITLE_MESSAGE);
    }

    @Test
    @DisplayName("Delete book by ID when book does not exist does nothing")
    void deleteById_BookDoesNotExist_DoesNothing() {
        // When
        bookService.deleteById(BookServiceUtils.BOOK_ID);

        // Then
        verify(bookRepository).deleteById(BookServiceUtils.BOOK_ID);
    }
}

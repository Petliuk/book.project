package com.example.controller;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParametersDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.service.BookService;
import com.example.utils.BookControllerUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @ControllerAdvice
    static class TestExceptionHandler {
        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setFallbackPageable(PageRequest.of(0, 10));
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .setCustomArgumentResolvers(pageableResolver)
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get all books when books exist")
    void getAll_BooksExist_Success() throws Exception {
        // Given
        BookDto book1 = BookControllerUtils.createBookDto(1L);
        BookDto book2 = BookControllerUtils.createBookDto(2L);
        List<BookDto> books = List.of(book1, book2);
        Page<BookDto> page = BookControllerUtils.createBookPage(books);
        when(bookService.getAll(any(Pageable.class))).thenReturn(page);

        // When
        MvcResult result = mockMvc.perform(
                        get(BookControllerUtils.BOOKS_URL)
                                .param("page", "0")
                                .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        // Then
        List<BookDto> actual = BookControllerUtils.parsePagedBookResponse(result, objectMapper);
        assertEquals(2, actual.size());
        assertThat(actual.get(0).getTitle(), equalTo("The Hobbit"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get book by valid ID")
    void getBookById_ValidId_Success() throws Exception {
        // Given
        BookDto expected = BookControllerUtils.createBookDto(1L);
        when(bookService.getBookById(1L)).thenReturn(expected);

        // When
        MvcResult result = mockMvc.perform(
                        get(BookControllerUtils.BOOKS_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertThat(actual.getTitle(), equalTo("The Hobbit"));
        assertThat(actual.getAuthor(), equalTo("J.R.R. Tolkien"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Get book by invalid ID")
    void getBookById_InvalidId_Fail() throws Exception {
        // Given
        when(bookService.getBookById(BookControllerUtils.INVALID_ID))
                .thenThrow(new EntityNotFoundException(BookControllerUtils.BOOK_NOT_FOUND_MESSAGE));

        // When
        MvcResult result = mockMvc.perform(
                        get(BookControllerUtils.BOOKS_URL + "/{id}", BookControllerUtils.INVALID_ID))
                .andExpect(status().isNotFound())
                .andReturn();

        // Then
        assertTrue(result.getResolvedException() instanceof EntityNotFoundException);
        assertThat(result.getResolvedException().getMessage(), equalTo(BookControllerUtils.BOOK_NOT_FOUND_MESSAGE));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Search books with valid parameters")
    void searchBooks_ValidParameters_Success() throws Exception {
        // Given
        BookDto expected = BookControllerUtils.createBookDto(1L);
        when(bookService.search(any(BookSearchParametersDto.class))).thenReturn(List.of(expected));

        // When
        MvcResult result = mockMvc.perform(
                        get(BookControllerUtils.SEARCH_URL)
                                .param("title", "The Hobbit"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        List<BookDto> actual = BookControllerUtils.parseResponse(
                result, new TypeReference<>() {
                }, objectMapper);
        assertThat(actual, hasSize(1));
        assertThat(actual.get(0).getTitle(), equalTo("The Hobbit"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Create book with valid request")
    void createBook_ValidRequest_Success() throws Exception {
        // Given
        CreateBookRequestDto request = BookControllerUtils.createBookRequestDto();
        BookDto expected = BookControllerUtils.createBookDto(1L);
        when(bookService.createBook(any(CreateBookRequestDto.class))).thenReturn(expected);

        // When
        MvcResult result = mockMvc.perform(
                        post(BookControllerUtils.BOOKS_URL)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertThat(actual.getTitle(), equalTo("The Hobbit"));
        assertThat(actual.getAuthor(), equalTo("J.R.R. Tolkien"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Update book with valid ID")
    void updateBook_ValidId_Success() throws Exception {
        // Given
        CreateBookRequestDto request = BookControllerUtils.createUpdatedBookRequestDto();
        BookDto expected = BookControllerUtils.createUpdatedBookDto(1L);
        when(bookService.updateBook(eq(1L), any(CreateBookRequestDto.class))).thenReturn(expected);

        // When
        MvcResult result = mockMvc.perform(
                        put(BookControllerUtils.BOOKS_URL + "/{id}", 1L)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertThat(actual.getTitle(), equalTo("The Fellowship of the Ring"));
        assertThat(actual.getAuthor(), equalTo("J.R.R. Tolkien"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Delete book with valid ID")
    void deleteBook_ValidId_Success() throws Exception {
        // Given
        when(bookService.getBookById(1L))
                .thenThrow(new EntityNotFoundException(BookControllerUtils.BOOK_NOT_FOUND_MESSAGE));

        // When
        mockMvc.perform(delete(BookControllerUtils.BOOKS_URL + "/{id}", 1L))
                .andExpect(status().isNoContent());

        // Then
        MvcResult result = mockMvc.perform(get(BookControllerUtils.BOOKS_URL + "/{id}", 1L))
                .andExpect(status().isNotFound())
                .andReturn();
        assertTrue(result.getResolvedException() instanceof EntityNotFoundException);
    }
}

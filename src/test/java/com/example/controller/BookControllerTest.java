package com.example.controller;

import com.example.dto.book.BookDto;
import com.example.dto.book.BookSearchParametersDto;
import com.example.dto.book.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.service.BookService;
import com.example.util.constants.BookControllerConstants;
import com.example.util.utils.BookControllerUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDto bookDto;
    private CreateBookRequestDto requestDto;
    private BookSearchParametersDto searchParams;

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
        pageableResolver.setFallbackPageable(PageRequest.of(
                Integer.parseInt(BookControllerConstants.PAGE), Integer.parseInt(BookControllerConstants.SIZE)));
        mockMvc = MockMvcBuilders
                .standaloneSetup(bookController)
                .setCustomArgumentResolvers(pageableResolver)
                .setControllerAdvice(new TestExceptionHandler())
                .build();

        bookDto = BookControllerUtils.createBookDto();
        requestDto = BookControllerUtils.createBookRequestDto();
        searchParams = BookControllerUtils.createBookSearchParametersDto();
    }

    @Test
    @DisplayName("Get all books when books exist returns book page")
    @WithMockUser(username = "user", roles = "USER")
    void getAll_BooksExist_ReturnsBookPage() throws Exception {
        // Given:
        Page<BookDto> expectedPage = BookControllerUtils.createBookPage(bookDto);
        when(bookService.getAll(any(Pageable.class))).thenReturn(expectedPage);

        // When:
        MvcResult result = mockMvc.perform(
                get(BookControllerConstants.BOOKS_ENDPOINT)
                        .param(BookControllerConstants.TITLE_PARAM, BookControllerConstants.PAGE)
                        .param(BookControllerConstants.TITLE_PARAM, BookControllerConstants.SIZE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then:
        List<BookDto> actualBooks = BookControllerUtils.parsePagedBookResponse(result, objectMapper);
        assertNotNull(actualBooks, BookControllerConstants.RESPONSE_NOT_NULL_MESSAGE);
        assertEquals(1, actualBooks.size(), BookControllerConstants.EXPECTED_BOOK_COUNT_MESSAGE);
        assertTrue(EqualsBuilder.reflectionEquals(bookDto, actualBooks.get(0), "id"),
                BookControllerConstants.BOOK_DETAILS_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Get book by ID when book exists returns book DTO")
    @WithMockUser(username = "user", roles = "USER")
    void getBookById_BookExists_ReturnsBookDto() throws Exception {
        // Given:
        when(bookService.getBookById(BookControllerConstants.BOOK_ID)).thenReturn(bookDto);

        // When:
        MvcResult result = mockMvc.perform(
                get(BookControllerConstants.BOOKS_ENDPOINT + "/" + BookControllerConstants.BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then:
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertNotNull(actual, BookControllerConstants.RESPONSE_NOT_NULL_MESSAGE);
        assertTrue(EqualsBuilder.reflectionEquals(bookDto, actual),
                BookControllerConstants.BOOK_DETAILS_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Get book by invalid ID returns not found")
    @WithMockUser(username = "user", roles = "USER")
    void getBookById_InvalidId_ReturnsNotFound() throws Exception {
        // Given:
        when(bookService.getBookById(BookControllerConstants.INVALID_BOOK_ID))
                .thenThrow(new EntityNotFoundException(BookControllerConstants.NOT_FOUND_MESSAGE));

        // When:
        MvcResult result = mockMvc.perform(
                get(BookControllerConstants.BOOKS_ENDPOINT + "/" + BookControllerConstants.INVALID_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound()).andReturn();

        // Then:
        assertTrue(result.getResolvedException() instanceof EntityNotFoundException,
                BookControllerConstants.EXCEPTION_TYPE_MESSAGE);
        assertEquals(BookControllerConstants.NOT_FOUND_MESSAGE, result.getResolvedException().getMessage(),
                BookControllerConstants.EXCEPTION_MESSAGE_MATCH);
    }

    @Test
    @DisplayName("Search books when parameters match returns book list")
    @WithMockUser(username = "user", roles = "USER")
    void searchBooks_ParametersMatch_ReturnsBookList() throws Exception {
        // Given:
        when(bookService.search(any(BookSearchParametersDto.class))).thenReturn(List.of(bookDto));

        // When:
        MvcResult result = mockMvc.perform(
                get(BookControllerConstants.BOOKS_SEARCH_ENDPOINT)
                        .param(BookControllerConstants.TITLE_PARAM, BookControllerConstants.BOOK_TITLE)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();

        // Then:
        List<BookDto> actualBooks = BookControllerUtils.parseResponse(result,
                new TypeReference<List<BookDto>>() {}, objectMapper);
        assertNotNull(actualBooks, BookControllerConstants.RESPONSE_NOT_NULL_MESSAGE);
        assertEquals(1, actualBooks.size(), BookControllerConstants.EXPECTED_BOOK_COUNT_MESSAGE);
        assertTrue(EqualsBuilder.reflectionEquals(bookDto, actualBooks.get(0)),
                BookControllerConstants.BOOK_DETAILS_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Create book when valid request returns created book DTO")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void createBook_ValidRequest_ReturnsCreatedBookDto() throws Exception {
        // Given:
        when(bookService.createBook(any(CreateBookRequestDto.class))).thenReturn(bookDto);

        // When:
        MvcResult result = mockMvc.perform(
                post(BookControllerConstants.BOOKS_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(status().isCreated()).andReturn();

        // Then:
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertNotNull(actual, BookControllerConstants.CREATED_BOOK_NOT_NULL_MESSAGE);
        assertTrue(EqualsBuilder.reflectionEquals(bookDto, actual),
                BookControllerConstants.CREATED_BOOK_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Update book when book exists returns updated book DTO")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void updateBook_BookExists_ReturnsUpdatedBookDto() throws Exception {
        // Given:
        when(bookService.updateBook(eq(BookControllerConstants.BOOK_ID),
                any(CreateBookRequestDto.class))).thenReturn(bookDto);

        // When:
        MvcResult result = mockMvc.perform(
                put(BookControllerConstants.BOOKS_ENDPOINT + "/" + BookControllerConstants.BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(status().isOk()).andReturn();

        // Then:
        BookDto actual = BookControllerUtils.parseResponse(result, BookDto.class, objectMapper);
        assertNotNull(actual, BookControllerConstants.UPDATED_BOOK_NOT_NULL_MESSAGE);
        assertTrue(EqualsBuilder.reflectionEquals(bookDto, actual),
                BookControllerConstants.UPDATED_BOOK_MATCH_MESSAGE);
    }

    @Test
    @DisplayName("Delete book when book exists returns no content")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void delete_BookExists_ReturnsNoContent() throws Exception {
        // Given:
        doNothing().when(bookService).deleteById(BookControllerConstants.BOOK_ID);

        // When:
        mockMvc.perform(
                delete(BookControllerConstants.BOOKS_ENDPOINT + "/" + BookControllerConstants.BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent());

        // Then:
        verify(bookService).deleteById(BookControllerConstants.BOOK_ID);
    }
}

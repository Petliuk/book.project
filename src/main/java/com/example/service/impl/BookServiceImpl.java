package com.example.service.impl;

import com.example.dto.BookDto;
import com.example.dto.BookSearchParametersDto;
import com.example.dto.CreateBookRequestDto;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.BookMapper;
import com.example.model.Book;
import com.example.model.Category;
import com.example.repository.book.BookRepository;
import com.example.repository.book.BookSpecificationBuilder;
import com.example.repository.category.CategoryRepository;
import com.example.service.BookService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find book by id " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        Set<Category> categories = new HashSet<>(categoryRepository
                .findAllById(requestDto.getCategoryIds()));
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Categories not found");
        }
        Book book = bookMapper.toEntity(requestDto);
        book.setCategories(categories);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cant find book by id " + id));
        bookMapper.updateBookFromDto(bookDto, book);
        updateBookCategories(book, bookDto.getCategoryIds());
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> search(@RequestParam BookSearchParametersDto params) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private void updateBookCategories(Book book, Set<Long> categoryIds) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
            if (categories.isEmpty()) {
                throw new EntityNotFoundException("Categories not found");
            }
            book.setCategories(categories);
        }
    }
}

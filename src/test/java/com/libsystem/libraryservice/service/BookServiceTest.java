package com.libsystem.libraryservice.service;

import com.libsystem.libraryservice.entity.Book;
import com.libsystem.libraryservice.exception.NotFoundException;
import com.libsystem.libraryservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book(null, "Clean Code", "Robert C. Martin", "978-0132350884", true);
    }

    @Test
    void addBook_shouldThrowException_whenBookIdIsNotNull() {
        book.setBookId(1000001L);
        assertThatThrownBy(() -> bookService.addBook(book))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("null");
    }

    @Test
    void addBook_shouldSaveAndReturnBook_whenBookIdIsNull() {
        book.setBookId(null);
        when(bookRepository.save(book)).thenReturn(book);
        Book result = bookService.addBook(book);
        assertThat(result).isNotNull();
        assertThat(result.getBookName()).isEqualTo("Clean Code");
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void findBookById_shouldReturnBook_whenBookExists() {
        when(bookRepository.findById(1000001L)).thenReturn(Optional.of(book));
        Book result = bookService.findBookById(1000001L);
        assertThat(result).isNotNull();
        assertThat(result.getBookName()).isEqualTo("Clean Code");
    }

    @Test
    void findBookById_shouldThrowNotFoundException_whenBookDoesNotExist() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.findBookById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(book)));
        Page<Book> result = bookService.getAllBooks(pageable);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getBookName()).isEqualTo("Clean Code");
    }

    @Test
    void editBook_shouldReturnUpdatedBook_whenBookExists() {
        book.setBookId(1000001L);
        when(bookRepository.findById(1000001L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        Book result = bookService.editBook(book);
        assertThat(result).isNotNull();
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void editBook_shouldThrowNotFoundException_whenBookDoesNotExist() {
        book.setBookId(99L);
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.editBook(book))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deleteBookById_shouldDeleteBook_whenBookExists() {
        when(bookRepository.findById(1000001L)).thenReturn(Optional.of(book));
        bookService.deleteBookById(1000001L);
        verify(bookRepository, times(1)).deleteById(1000001L);
    }

    @Test
    void deleteBookById_shouldThrowNotFoundException_whenBookDoesNotExist() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.deleteBookById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAllAvailableBooksByIsbn_shouldReturnIds_whenIsbnExists() {
        when(bookRepository.existsByBookISBN("978-0132350884")).thenReturn(true);
        when(bookRepository.findAllSimilarISBNIsAvailableTrue("978-0132350884"))
                .thenReturn(Arrays.asList(1000001L, 1000002L));
        List<Long> result = bookService.findAllAvailableBooksByIsbn("978-0132350884");
        assertThat(result).hasSize(2);
    }

    @Test
    void findAllAvailableBooksByIsbn_shouldThrowNotFoundException_whenIsbnDoesNotExist() {
        when(bookRepository.existsByBookISBN("000-0000000000")).thenReturn(false);
        assertThatThrownBy(() -> bookService.findAllAvailableBooksByIsbn("000-0000000000"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("000-0000000000");
    }
}
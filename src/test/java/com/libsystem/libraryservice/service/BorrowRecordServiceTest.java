package com.libsystem.libraryservice.service;

import com.libsystem.libraryservice.entity.Book;
import com.libsystem.libraryservice.entity.BorrowRecord;
import com.libsystem.libraryservice.entity.User;
import com.libsystem.libraryservice.exception.BorrowRecordException;
import com.libsystem.libraryservice.repository.BorrowRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowRecordServiceTest {

    @Mock
    private BookService bookService;
    @Mock
    private UserService userService;
    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @InjectMocks
    private BorrowRecordService borrowRecordService;

    private Book availableBook;
    private Book unavailableBook;
    private User user;

    @BeforeEach
    void setUp() {
        availableBook = new Book("Unit Test", "Moein", "12345678",true);
        availableBook.setBookId(1000001L);

        unavailableBook = new Book("Unit Test", "Moein", "12345678",false);
        unavailableBook.setBookId(1000002L);

        user = new User("John", "Doe", "JohnDoe");
    }

    @Test
    void addBorrowRecord_shouldThrowException_whenBookIdIsNull() {
        assertThatThrownBy(() -> borrowRecordService.addBorrowRecord(1L, null))
                .isInstanceOf(BorrowRecordException.class)
                .hasMessageContaining("null");
    }

    @Test
    void addBorrowRecord_shouldThrowException_whenUserIdIsNull() {
        assertThatThrownBy(() -> borrowRecordService.addBorrowRecord(null, 1000001L))
                .isInstanceOf(BorrowRecordException.class)
                .hasMessageContaining("null");
    }

    @Test
    void addBorrowRecord_shouldThrowException_whenBookIsNotAvailable() {
        when(bookService.findBookById(1000002L)).thenReturn(unavailableBook);
        when(userService.findUserById(1L)).thenReturn(user);
        when(bookService.findAllAvailableBooksByIsbn("12345678"))
                .thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> borrowRecordService.addBorrowRecord(1L, 1000002L))
                .isInstanceOf(BorrowRecordException.class)
                .hasMessageContaining("already borrowed");
    }

    @Test
    void addBorrowRecord_shouldReturnBorrowRecord_whenBookIsAvailable() {
        when(bookService.findBookById(1000001L)).thenReturn(availableBook);
        when(userService.findUserById(1L)).thenReturn(user);
        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BorrowRecord result = borrowRecordService.addBorrowRecord(1L, 1000001L);

        assertThat(result).isNotNull();
        assertThat(result.getBookId()).isEqualTo(1000001L);
        assertThat(result.getUserId()).isEqualTo(user.getId());
        verify(bookService, times(1)).editBook(availableBook);
    }

    @Test
    void deleteBorrowRecord_shouldThrowException_whenBookNotBorrowed() {
        when(bookService.findBookById(1000001L)).thenReturn(availableBook);
        when(borrowRecordRepository.findBorrowRecordByBookId(1000001L)).thenReturn(null);

        assertThatThrownBy(() -> borrowRecordService.deleteBorrowRecordByBookId(1000001L))
                .isInstanceOf(BorrowRecordException.class)
                .hasMessageContaining("hasn't been borrowed");
    }

    @Test
    void deleteBorrowRecord_shouldReturnConfirmationMessage_whenRecordExists() {
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBookId(1000001L);
        borrowRecord.setUserId(1L);

        when(bookService.findBookById(1000001L)).thenReturn(availableBook);
        when(borrowRecordRepository.findBorrowRecordByBookId(1000001L)).thenReturn(borrowRecord);

        String result = borrowRecordService.deleteBorrowRecordByBookId(1000001L);

        assertThat(result).contains("returned");
        assertThat(result).contains("1000001");
        verify(borrowRecordRepository, times(1)).delete(borrowRecord);
    }

    @Test
    void findAllBorrowRecordByUserId_shouldReturnRecords() {
        BorrowRecord record = new BorrowRecord();
        record.setUserId(1L);
        when(borrowRecordRepository.findAllByUserId(1L)).thenReturn(Arrays.asList(record));

        List<BorrowRecord> result = borrowRecordService.findAllBorrowRecordByUserId(1L);

        assertThat(result).hasSize(1);;
        assertThat(result.get(0).getUserId()).isEqualTo(1L);
    }

    @Test
    void findAllBorrowedBookIds_shouldReturnAllIds() {
        when(borrowRecordRepository.getAllBookIds()).thenReturn(Arrays.asList(1000001L, 1000002L));

        List<Long> result = borrowRecordService.findAllBorrowedBookIds();

        assertThat(result).hasSize(2);
        assertThat(result).contains(1000001L, 1000002L);
    }

    @Test
    void getAll_shouldReturnAllBorrowRecords() {
        when(borrowRecordRepository.findAll()).thenReturn(Arrays.asList(new BorrowRecord()));

        List<BorrowRecord> result = borrowRecordService.getAll();

        assertThat(result).hasSize(1);
    }
}
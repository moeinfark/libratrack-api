package com.libsystem.libraryservice.mapper;

import com.libsystem.libraryservice.dto.request.*;
import com.libsystem.libraryservice.dto.response.BookResponse;
import com.libsystem.libraryservice.dto.response.BorrowRecordResponse;
import com.libsystem.libraryservice.dto.response.UserResponse;
import com.libsystem.libraryservice.entity.Book;
import com.libsystem.libraryservice.entity.BorrowRecord;
import com.libsystem.libraryservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LibraryMapper {

    public Book toBook(BookCreateRequest request) {
        Book book = new Book();
        book.setBookName(request.getBookName());
        book.setBookAuthor(request.getBookAuthor());
        book.setBookISBN(request.getBookISBN());
        book.setIsAvailable(true);
        return book;
    }

    public Book updateBook(BookUpdateRequest request, Long bookId) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookName(request.getBookName());
        book.setBookAuthor(request.getBookAuthor());
        book.setBookISBN(request.getBookISBN());
        book.setIsAvailable(request.getIsAvailable());
        return book;
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .bookId(book.getBookId())
                .bookName(book.getBookName())
                .bookAuthor(book.getBookAuthor())
                .bookISBN(book.getBookISBN())
                .isAvailable(book.getIsAvailable())
                .build();
    }

    public User toUser(UserCreateRequest request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        return user;
    }

    public User updateUser(UserUpdateRequest request, Long userId) {
        User user = new User();
        user.setId(userId);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        return user;
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUsername())
                .build();
    }

    public BorrowRecordResponse toBorrowRecordResponse(BorrowRecord record) {
        return BorrowRecordResponse.builder()
                .id(record.getId())
                .userId(record.getUserId())
                .bookId(record.getBookId())
                .borrowDate(record.getBorrowDate())
                .returnDate(record.getReturnDate())
                .build();
    }

    
}
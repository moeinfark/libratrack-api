package com.libsystem.libraryservice.service;

import com.libsystem.libraryservice.entity.Book;
import com.libsystem.libraryservice.entity.BorrowRecord;
import com.libsystem.libraryservice.entity.User;
import com.libsystem.libraryservice.exception.BorrowRecordException;
import com.libsystem.libraryservice.repository.BorrowRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowRecordService {

    BookService bookService;
    UserService userService;
    BorrowRecordRepository borrowRecordRepository;

    @Autowired
    public BorrowRecordService(BookService bookService,
                               UserService userService,
                               BorrowRecordRepository borrowRecordRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    @Transactional
    public BorrowRecord addBorrowRecord( Long userId,Long bookId) {
        try {
            if (bookId == null || userId == null) {
                throw new BorrowRecordException("User ID and Book ID cannot be null");
            }
            BorrowRecord borrowRecord = new BorrowRecord();

            Book book = bookService.findBookById(bookId);
            User user = userService.findUserById(userId);


            if (book.getIsAvailable() == false) {
                String message = "book with id " + bookId + " is already borrowed! ";

                List<Long> availableAlternatives = bookService.findAllAvailableBooksByIsbn(book.getBookISBN());
                if (!availableAlternatives.isEmpty()) {
                    message += "You can find an available copy of the book under following book-ids: " +
                            availableAlternatives;
                }
                throw new BorrowRecordException(message);
            }

            borrowRecord.setBookId(bookId);
            borrowRecord.setUserId(userId);
            borrowRecord.setBorrowDate(LocalDate.now());

            borrowRecordRepository.save(borrowRecord);

            book.setIsAvailable(false);
            bookService.editBook(book);

            return borrowRecord;

        } catch (BorrowRecordException e) {
            throw e;
        }catch (Exception e) {
            throw new BorrowRecordException("Something unexpected happened. please try again later!");
        }
    }


    @Transactional
    public String deleteBorrowRecordByBookId(Long bookId) {
        if (bookId == null) {
            throw new BorrowRecordException("Please enter valid Book ID!");
        }

        BorrowRecord borrowRecord = borrowRecordRepository.findBorrowRecordByBookId(bookId);
        if (borrowRecord == null) {
            throw new BorrowRecordException("this book hasn't been borrowed!");
        }

        Book book =bookService.findBookById(bookId);
        book.setIsAvailable(true);
        bookService.editBook(book);

        borrowRecordRepository.delete(borrowRecord);
        return "User with user-id: "+ borrowRecord.getUserId()+ " has returned book with id " + bookId ;
    }

    public List<BorrowRecord> findAllBorrowRecordByUserId(Long userId) {
        return borrowRecordRepository.findAllByUserId(userId);
    }

    public List<Long> findAllBorrowedBookIds(){
        return borrowRecordRepository.getAllBookIds();
    }

    public List<BorrowRecord> getAll() {
        return borrowRecordRepository.findAll();
    }

}

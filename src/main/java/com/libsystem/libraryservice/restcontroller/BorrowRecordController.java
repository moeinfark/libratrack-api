package com.libsystem.libraryservice.restcontroller;

import com.libsystem.libraryservice.entity.BorrowRecord;
import com.libsystem.libraryservice.service.BookService;
import com.libsystem.libraryservice.service.BorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/borrow-records")
public class BorrowRecordController {

    private final BookService bookService;
    BorrowRecordService borrowRecordService;

    @Autowired
    public BorrowRecordController(BorrowRecordService borrowRecordService, BookService bookService) {
        this.borrowRecordService = borrowRecordService;
        this.bookService = bookService;
    }

    @PostMapping
    public BorrowRecord addBorrowRecord(@RequestBody BorrowRecord borrowRecord) {
        return borrowRecordService.addBorrowRecord(borrowRecord.getUserId(), borrowRecord.getBookId());

    }

    @DeleteMapping("/{bookId}")
    public String removeBorrowRecordByBookId(@PathVariable Long bookId) {
        return borrowRecordService.deleteBorrowRecordByBookId(bookId);
    }

    @GetMapping
    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordService.getAll();
    }

    @GetMapping("/borrowed/book-ids")
    public List<Long> getAllBorrowedBookIds() {
        return borrowRecordService.findAllBorrowedBookIds();
    }

    @GetMapping("/available/book-ids")
    public List<Long> getAllAvailableBookIds() {
        return bookService.findAllAvailableBookIds();
    }

}

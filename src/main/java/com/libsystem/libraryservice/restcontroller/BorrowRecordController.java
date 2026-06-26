package com.libsystem.libraryservice.restcontroller;

import com.libsystem.libraryservice.dto.request.BorrowRequest;
import com.libsystem.libraryservice.dto.response.BorrowRecordResponse;
import com.libsystem.libraryservice.mapper.LibraryMapper;
import com.libsystem.libraryservice.service.BookService;
import com.libsystem.libraryservice.service.BorrowRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;
    private final BookService bookService;
    private final LibraryMapper mapper;

    @Autowired
    public BorrowRecordController(BorrowRecordService borrowRecordService,
                                  BookService bookService,
                                  LibraryMapper mapper) {
        this.borrowRecordService = borrowRecordService;
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<Page<BorrowRecordResponse>> getAllBorrowRecords(@PageableDefault(size = 10, sort = "borrowDate") Pageable pageable) {
        Page<BorrowRecordResponse> records = borrowRecordService.getAll(pageable).map(mapper::toBorrowRecordResponse);
        return ResponseEntity.ok(records);
    }

    @PostMapping
    public ResponseEntity<BorrowRecordResponse> addBorrowRecord(@Valid @RequestBody BorrowRequest request) {
        BorrowRecordResponse response = mapper.toBorrowRecordResponse(
                borrowRecordService.addBorrowRecord(request.getUserId(), request.getBookId())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(borrowRecordService.deleteBorrowRecordByBookId(bookId));
    }

    @GetMapping("/borrowed/book-ids")
    public ResponseEntity<List<Long>> getAllBorrowedBookIds() {
        return ResponseEntity.ok(borrowRecordService.findAllBorrowedBookIds());
    }

    @GetMapping("/available/book-ids")
    public ResponseEntity<List<Long>> getAllAvailableBookIds() {
        return ResponseEntity.ok(bookService.findAllAvailableBookIds());
    }
}
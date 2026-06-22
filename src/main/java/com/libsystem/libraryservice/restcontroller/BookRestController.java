package com.libsystem.libraryservice.restcontroller;

import com.libsystem.libraryservice.dto.request.BookCreateRequest;
import com.libsystem.libraryservice.dto.request.BookUpdateRequest;
import com.libsystem.libraryservice.dto.response.BookResponse;
import com.libsystem.libraryservice.mapper.LibraryMapper;
import com.libsystem.libraryservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;
    private final LibraryMapper mapper;

    @Autowired
    public BookRestController(BookService bookService, LibraryMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> books = bookService.getAllBooks()
                .stream()
                .map(mapper::toBookResponse)
                .toList();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(mapper.toBookResponse(bookService.findBookById(bookId)));
    }

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookCreateRequest request) {
        BookResponse response = mapper.toBookResponse(bookService.addBook(mapper.toBook(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Long bookId,
                                                   @Valid @RequestBody BookUpdateRequest request) {
        BookResponse response = mapper.toBookResponse(bookService.editBook(mapper.updateBook(request, bookId)));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/advance-search")
    public ResponseEntity<List<Long>> advancedSearch(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String author,
                                                     @RequestParam(required = false) String isbn) {
        return ResponseEntity.ok(bookService.searchByDifferentCriterias(name, author, isbn));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Long>> search(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(bookService.searchAllFields(keyword));
    }
}
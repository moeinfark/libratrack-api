package com.libsystem.libraryservice.restcontroller;

import com.libsystem.libraryservice.entity.Book;
import com.libsystem.libraryservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private final BookService bookService;

    @Autowired
    public BookRestController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{bookId}")
    public Book getBookById(@PathVariable Long bookId) {
        return bookService.findBookById(bookId);
    }

    @PostMapping()
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/{bookId}")
    public Book updateBook(@PathVariable Long bookId, @RequestBody Book book) {
        book.setBookId(bookId);
        return bookService.editBook(book);
    }

    @DeleteMapping("/{bookId}")
    public String deleteBook(@PathVariable Long bookId) {
        bookService.deleteBookById(bookId);
        return "Book with ID - " + bookId + " Deleted Successfully";
    }

    @GetMapping("/advance-search")
    public List<Long> advancedSearch(@RequestParam(required = false) String name,
                                     @RequestParam(required = false) String author,
                                     @RequestParam(required = false) String isbn) {
        return bookService.searchByDifferentCriterias(name, author, isbn);
    }

    @GetMapping("/search")
    public List<Long> searchResult(@RequestParam(required = false) String keyword){
        return bookService.searchAllFields(keyword);
    }


}

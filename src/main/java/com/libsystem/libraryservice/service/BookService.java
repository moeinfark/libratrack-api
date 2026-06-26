package com.libsystem.libraryservice.service;

import com.libsystem.libraryservice.entity.Book;
import com.libsystem.libraryservice.exception.NotFoundException;
import com.libsystem.libraryservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {
        if (book.getBookId() != null) {
            throw new RuntimeException("to add new user, Book-ID must be set to null!");
        }
        return bookRepository.save(book);
    }

    public Book findBookById(Long id) {

        Optional<Book> book = bookRepository.findById(id);

        if(book.isEmpty()) {
            throw new NotFoundException("Book by id - " + id + " not found");
        }
        return book.get();
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book editBook(Book book) {
        if (bookRepository.findById(book.getBookId()).isPresent()) {
            return bookRepository.save(book);
        }
        throw new NotFoundException("Book by id - " + book.getBookId() + " not found");
    }

    public void deleteBookById(Long id) {
        if (bookRepository.findById(id).isPresent()) {
            bookRepository.deleteById(id);
        }
        else {
            throw new NotFoundException("Book by id - " + id + " not found");
        }

    }

    public List<Long> findAllAvailableBooksByIsbn(String bookISBN) {
        if (bookRepository.existsByBookISBN(bookISBN)) {
            return bookRepository.findAllSimilarISBNIsAvailableTrue(bookISBN);
        }
        else {
            throw new NotFoundException("Book by ISBN - " + bookISBN + " not found");
        }
    }

    public List<Long> findAllAvailableBookIds(){
        return bookRepository.findAllIsAvailableTrueBookIds();
    }

    public List<Long> searchByDifferentCriterias(String name, String author, String isbn) {
        return bookRepository.searchByCriteria(name, author, isbn);
    }

    public List<Long> searchAllFields(String entry){
        return bookRepository.normalSearch(entry);
    }
}

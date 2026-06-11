package com.libsystem.libraryservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name="book_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
    @SequenceGenerator(
            name="book_generator",
            sequenceName = "book_sequence",
            initialValue = 1000001,
            allocationSize = 1
    )
    private Long bookId;

    @Column(name="book_name")
    private String bookName;
    @Column(name="book_author")
    private String bookAuthor;
    @Column(name="book_isbn")
    private String bookISBN;
    @Column(name="is_available")
    private Boolean isAvailable;


    public Book(String bookName, String bookAuthor, String bookISBN, Boolean isAvailable) {
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookISBN = bookISBN;
        this.isAvailable = isAvailable;
    }

    public Book() {}

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + bookName + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookISBN='" + bookISBN + '\'' +
                ", bookId=" + bookId +
                '}';
    }
}

package com.libsystem.libraryservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_generator")
    @SequenceGenerator(
            name = "book_generator",
            sequenceName = "book_sequence",
            initialValue = 1000001,
            allocationSize = 1
    )
    private Long bookId;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "book_author")
    private String bookAuthor;

    @Column(name = "book_isbn")
    private String bookISBN;

    @Column(name = "is_available")
    private Boolean isAvailable;
}
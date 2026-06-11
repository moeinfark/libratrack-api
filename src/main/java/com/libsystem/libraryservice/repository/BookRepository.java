package com.libsystem.libraryservice.repository;

import com.libsystem.libraryservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByBookISBNAndIsAvailableTrue(String isbn);

    @Query("SELECT bk.bookId from Book bk where bk.bookISBN=:isbn and bk.isAvailable=true")
    List<Long> findAllSimilarISBNIsAvailableTrue(@Param("isbn") String isbn);
    Boolean existsByBookISBN(String isbn);

    @Query("SELECT bk.bookId from Book bk where bk.isAvailable=true")
    List<Long> findAllIsAvailableTrueBookIds();

    @Query("SELECT b.bookId FROM Book b WHERE " +
            "(:name IS null OR LOWER(b.bookName) LIKE LOWER(concat('%', :name, '%'))) AND " +
            "(:author is null or LOWER(b.bookAuthor) LIKE LOWER(concat('%', :author, '%'))) AND " +
            "(:isbn is null or LOWER(b.bookISBN) LIKE LOWER(concat('%', :isbn, '%'))) " )
    List<Long> searchByCriteria(@Param("name") String name,
                                @Param("author") String author,
                                @Param("isbn") String isbn);

    @Query("SELECT b.bookId from Book b WHERE " +
            "(lower(b.bookName) like lower(concat('%',:entry,'%') ) ) OR " +
            "(lower(b.bookAuthor) like lower(concat('%',:entry,'%') ) ) OR " +
            "(lower(b.bookISBN) like lower(concat('%',:entry,'%') ) )")
    List<Long> normalSearch(@Param("entry") String entry);
}


package com.libsystem.libraryservice.repository;

import com.libsystem.libraryservice.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    public BorrowRecord deleteBorrowRecordsByBookId(Long bookId);

    public BorrowRecord findBorrowRecordByBookId(Long bookId);

    public List<BorrowRecord> findAllByUserId(Long userId);

    @Query("SELECT distinct br.bookId FROM BorrowRecord br")
    public List<Long> getAllBookIds();


}

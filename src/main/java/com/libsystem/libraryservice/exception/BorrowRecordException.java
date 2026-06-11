package com.libsystem.libraryservice.exception;

public class BorrowRecordException extends RuntimeException {
    public BorrowRecordException(String message) {
        super(message);
    }

    public BorrowRecordException(String message, Throwable cause) {
        super(message, cause);
    }

    public BorrowRecordException(Throwable cause) {
        super(cause);
    }
}

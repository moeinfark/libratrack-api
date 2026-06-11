package com.libsystem.libraryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    // add exception handing code
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(NotFoundException ex){

        // create a UserErrorResponse

        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setTimestamp(System.currentTimeMillis());

        // return Response
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handlerException(BorrowRecordException ex){

        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // add second ExceptionHandler for all the exception kinds
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception ex){

        ErrorResponse error = new ErrorResponse();
        error.setMessage(ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}

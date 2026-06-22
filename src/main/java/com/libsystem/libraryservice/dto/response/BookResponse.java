package com.libsystem.libraryservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookResponse {

    private Long bookId;
    private String bookName;
    private String bookAuthor;
    private String bookISBN;
    private Boolean isAvailable;
}
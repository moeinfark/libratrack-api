package com.libsystem.libraryservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookUpdateRequest {

    @NotBlank(message = "Book name is required")
    private String bookName;

    @NotBlank(message = "Author is required")
    private String bookAuthor;

    @NotBlank(message = "ISBN is required")
    private String bookISBN;

    private Boolean isAvailable;
}
package com.libsystem.libraryservice.security.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String userName;
    private String role;
    private Long id;
}

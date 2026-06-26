package com.libsystem.libraryservice.security;

import com.libsystem.libraryservice.entity.Role;
import com.libsystem.libraryservice.entity.User;
import com.libsystem.libraryservice.exception.NotFoundException;
import com.libsystem.libraryservice.repository.UserRepository;
import com.libsystem.libraryservice.security.auth.AuthResponse;
import com.libsystem.libraryservice.security.auth.LoginRequest;
import com.libsystem.libraryservice.security.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .userName(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new NotFoundException("User not found"));

        String token = jwtService.generateToken(user.getUsername());

        return AuthResponse.builder()
                .id(user.getId())
                .token(token)
                .userName(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
package com.libsystem.libraryservice.restcontroller;

import com.libsystem.libraryservice.dto.request.UserCreateRequest;
import com.libsystem.libraryservice.dto.request.UserUpdateRequest;
import com.libsystem.libraryservice.dto.response.BorrowRecordResponse;
import com.libsystem.libraryservice.dto.response.UserResponse;
import com.libsystem.libraryservice.mapper.LibraryMapper;
import com.libsystem.libraryservice.service.BorrowRecordService;
import com.libsystem.libraryservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final BorrowRecordService borrowRecordService;
    private final LibraryMapper mapper;

    @Autowired
    public UserRestController(UserService userService,
                              BorrowRecordService borrowRecordService,
                              LibraryMapper mapper) {
        this.userService = userService;
        this.borrowRecordService = borrowRecordService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(mapper::toUserResponse)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(mapper.toUserResponse(userService.findUserById(userId)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = mapper.toUserResponse(userService.addUser(mapper.toUser(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long userId,
                                                   @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = mapper.toUserResponse(userService.editUser(mapper.updateUser(request, userId)));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/borrow-records")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<BorrowRecordResponse>> getBorrowRecordsByUserId(@PathVariable Long userId) {
        List<BorrowRecordResponse> records = borrowRecordService.findAllBorrowRecordByUserId(userId)
                .stream()
                .map(mapper::toBorrowRecordResponse)
                .toList();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/borrow-records")
    public ResponseEntity<List<Long>> getAllBorrowedBookIds() {
        return ResponseEntity.ok(borrowRecordService.findAllBorrowedBookIds());
    }
}
package com.libsystem.libraryservice.service;

import com.libsystem.libraryservice.entity.User;
import com.libsystem.libraryservice.exception.NotFoundException;
import com.libsystem.libraryservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .userName("JohnDoe")
                .build();
    }

    @Test
    void findUserById_shouldReturnUser_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User result = userService.findUserById(1L);
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    @Test
    void findUserById_shouldThrowNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.findUserById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getAllUsers_shouldReturnAllUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(user)));
        Page<User> result = userService.getAllUsers(pageable);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void editUser_shouldReturnUpdatedUser_whenUserExists() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        User updatedUser = User.builder()
                .firstName("Jonny")
                .lastName("Dolan")
                .userName("JonnyDolan")
                .build();
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User result = userService.editUser(updatedUser);
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jonny");
    }

    @Test
    void editUser_shouldThrowException_whenUserDoesNotExist() {
        User updatedUser = User.builder()
                .firstName("Jonny")
                .lastName("Doe")
                .userName("JohnDoe")
                .build();
        when(userRepository.existsById(updatedUser.getId())).thenReturn(false);
        assertThatThrownBy(() -> userService.editUser(updatedUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id");
    }

    @Test
    void deleteUserById_shouldDeleteUser_whenUserExists() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        userService.deleteUserById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void deleteUserById_shouldThrowException_whenUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> userService.deleteUserById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");
    }
}
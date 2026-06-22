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
        user = new User(null, "John", "Doe", "JohnDoe");
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
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        List<User> result = userService.getAllUsers();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void editUser_shouldReturnUpdatedUser_whenUserExists() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        User updatedUser = new User(null, "Jonny", "Dolan", "JonnyDolan");
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User result = userService.editUser(updatedUser);
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Jonny");
    }

    @Test
    void editUser_shouldThrowException_whenUserDoesNotExist() {
        User updatedUser = new User(null, "Jonny", "Doe", "JohnDoe");
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
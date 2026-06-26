package com.libsystem.libraryservice.service;

import com.libsystem.libraryservice.entity.User;
import com.libsystem.libraryservice.repository.UserRepository;
import com.libsystem.libraryservice.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    };

    public User addUser(User theUser) {
        return userRepository.save(theUser);
    }

    public User findUserById(Long id) {

        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        }
        else  {
            throw new NotFoundException("User with id - " + id + " not found");
        }
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    public User editUser(User theUser) {
        if (!userRepository.existsById(theUser.getId())) {
            throw new NotFoundException("User with id - " + theUser.getId() + " not found");
        }
        return userRepository.save(theUser);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id - " + id + " not found");
        }
        userRepository.deleteById(id);
    }
}

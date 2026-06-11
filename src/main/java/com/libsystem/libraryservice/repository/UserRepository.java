package com.libsystem.libraryservice.repository;

import com.libsystem.libraryservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

package com.lms.core.service;

import com.lms.core.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    User updateUser(UUID id, User user);
    void deleteUser(UUID id);
    boolean existsByEmail(String email);
}
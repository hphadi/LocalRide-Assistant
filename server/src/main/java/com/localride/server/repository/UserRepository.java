package com.localride.server.repository;

import com.localride.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * UserRepository interface for managing User entities.
 * It extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface UserRepository extends JpaRepository <User, Long> {
    // Method to find a user by username
   Optional<User> findByUsername(String username);
}

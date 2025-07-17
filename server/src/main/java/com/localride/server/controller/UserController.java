package com.localride.server.controller;

import com.localride.server.dto.UserRegistrationRequest;
import com.localride.server.dto.UserResponseDTO;
import com.localride.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        logger.info("Received request to get all users");
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRegistrationRequest request) {
        logger.info("Received request to update user with id: {}", id);
        return userService.updateUser(id, request)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("Received request to delete user with id: {}", id);
        if (userService.deleteUser(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.warn("User not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
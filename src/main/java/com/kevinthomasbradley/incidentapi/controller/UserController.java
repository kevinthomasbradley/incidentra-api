package com.kevinthomasbradley.incidentapi.controller;

import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * REST controller for managing users.
 * Provides endpoints for creating users, retrieving all users, and fetching a user by ID.
 * Delegates business logic to the {@link UserService}.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user.
     *
     * @param request the request body containing user details (username, password, email, role)
     * @return the created User object with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(
            request.getUsername(),
            request.getPassword(),
            request.getEmail(),
            request.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a list of all User objects with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user to retrieve
     * @return the User object with the specified ID and HTTP 200 status
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Data Transfer Object (DTO) for user creation requests.
     * Encapsulates the fields required to create a new user.
     */
    @Data
    static class CreateUserRequest {
        /**
         * The username for the new user.
         */
        private String username;

        /**
         * The password for the new user (should be hashed in production).
         */
        private String password;

        /**
         * The email address of the new user.
         */
        private String email;

        /**
         * The role of the new user (must match a value in User.Role enum).
         */
        private String role;
    }
}
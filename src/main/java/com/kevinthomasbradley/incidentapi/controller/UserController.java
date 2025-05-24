package com.kevinthomasbradley.incidentapi.controller;

import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // DTO for user creation
    @Data
    static class CreateUserRequest {
        private String username;
        private String password;
        private String email;
        private String role;
    }
}
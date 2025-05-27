package com.kevinthomasbradley.incidentapi.service;

import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Service class for managing users.
 * Handles business logic related to creating, retrieving, and managing user accounts.
 * Uses UserRepository for data access.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Creates a new user with the provided details.
     *
     * @param username the username for the new user
     * @param password the password for the new user (should be hashed in production)
     * @param email    the email address of the new user
     * @param role     the role of the user (must match a value in User.Role enum)
     * @return the created User object
     * @throws IllegalArgumentException if the role is invalid
     */
    public User createUser(String username, String password, String email, String role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // In production, hash the password!
        user.setEmail(email);
        user.setRole(User.Role.valueOf(role));
        return userRepository.save(user);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all User objects
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user to retrieve
     * @return the User object with the specified ID
     * @throws RuntimeException if the user is not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
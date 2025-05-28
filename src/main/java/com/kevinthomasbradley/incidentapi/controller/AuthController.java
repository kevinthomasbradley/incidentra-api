package com.kevinthomasbradley.incidentapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevinthomasbradley.incidentapi.JwtUtil;
import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.repository.UserRepository;
import com.kevinthomasbradley.incidentapi.service.CustomUserDetailsService;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * REST controller for authentication and registration endpoints.
 * <p>
 * Provides endpoints for user registration and login, issuing JWT tokens upon successful authentication.
 * </p>
 * <ul>
 *   <li>POST /api/auth/register: Registers a new user with the provided credentials and role.</li>
 *   <li>POST /api/auth/login: Authenticates a user and returns a JWT token if credentials are valid.</li>
 * </ul>
 * <p>
 * Delegates user management to {@link UserRepository}, password encoding to {@link PasswordEncoder},
 * authentication to {@link AuthenticationManager}, and JWT operations to {@link JwtUtil}.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    /**
     * Spring Security authentication manager for authenticating login requests.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Repository for accessing and persisting user data.
     */
    private final UserRepository userRepository;

    /**
     * Utility for generating and validating JWT tokens.
     */
    private final JwtUtil jwtUtil;

    /**
     * Password encoder for securely hashing user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Service for loading user details during authentication.
     */
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Registers a new user with the provided details.
     *
     * @param request the registration request containing username, password, email, and role
     * @return a ResponseEntity with a success or error message
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash the password
        user.setEmail(request.getEmail());
        user.setRole(User.Role.valueOf(request.getRole())); // Validate role

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * Authenticates a user and returns a JWT token if credentials are valid.
     *
     * @param request the authentication request containing username and password
     * @return a ResponseEntity containing the JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    /**
     * Data Transfer Object (DTO) for user registration requests.
     * Encapsulates the fields required to register a new user.
     */
    @Data
    static class RegisterRequest {
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

    /**
     * Data Transfer Object (DTO) for authentication (login) requests.
     * Encapsulates the fields required to authenticate a user.
     */
    @Data
    static class AuthRequest {
        /**
         * The username of the user attempting to log in.
         */
        private String username;

        /**
         * The password of the user attempting to log in.
         */
        private String password;
    }

    /**
     * Data Transfer Object (DTO) for authentication responses.
     * Encapsulates the JWT token returned upon successful authentication.
     */
    @Data
    @AllArgsConstructor
    static class AuthResponse {
        /**
         * The JWT token issued to the authenticated user.
         */
        private String token;
    }
}

package com.kevinthomasbradley.incidentapi.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Custom implementation of {@link UserDetailsService} for Spring Security.
 * <p>
 * This service is responsible for loading user-specific data during authentication.
 * It retrieves user information from the database using {@link UserRepository}
 * and adapts it to Spring Security's {@link UserDetails} interface.
 * </p>
 * <ul>
 *   <li>Used by Spring Security to authenticate and authorize users based on their credentials and roles.</li>
 *   <li>Maps application-specific {@link User} roles to Spring Security authorities (e.g., "ROLE_CITIZEN").</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository for accessing user data from the database.
     */
    private final UserRepository userRepository;

    /**
     * Loads the user by username for authentication.
     *
     * @param username the username identifying the user whose data is required
     * @return a fully populated {@link UserDetails} object (username, password, authorities)
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        // Map the user's role to a Spring Security authority (e.g., "ROLE_CITIZEN")
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}

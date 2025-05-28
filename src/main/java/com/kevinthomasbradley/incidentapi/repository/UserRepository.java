package com.kevinthomasbradley.incidentapi.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kevinthomasbradley.incidentapi.model.User;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Repository interface for User entities.
 * Extends JpaRepository to provide standard CRUD operations and custom query methods for User data.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds all users with the specified role.
     *
     * @param role the role of the users to retrieve (e.g., CITIZEN, DISPATCHER, RESPONDER, ADMIN)
     * @return a list of User objects with the given role
     */
    List<User> findByRole(User.Role role);

    /**
     * Checks if a user with the specified username exists.
     *
     * @param username the username to check for existence
     * @return true if a user with the given username exists, false otherwise
     */ 
    boolean existsByUsername(String username);

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the User if found, or empty if not found
     */
    java.util.Optional<User> findByUsername(String username);
    
    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to find
     * @return an Optional containing the User if found, or empty if not found
     */
    java.util.Optional<User> findByEmail(String email);
}
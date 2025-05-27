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
}
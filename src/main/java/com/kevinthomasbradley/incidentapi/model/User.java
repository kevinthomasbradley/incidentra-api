package com.kevinthomasbradley.incidentapi.model;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Entity representing a user in the system.
 * Users can have different roles such as CITIZEN, DISPATCHER, or RESPONDER.
 * This entity is mapped to the "users" table in the database.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Enumeration of possible user roles.
     * CITIZEN: Regular user who can report incidents.
     * DISPATCHER: User who can assign incidents to responders.
     * RESPONDER: User who can resolve incidents.
     */
    public enum Role { CITIZEN, DISPATCHER, RESPONDER }

    /**
     * Unique identifier for the user.
     * Auto-generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username for authentication and identification.
     */
    private String username;

    /**
     * Password for authentication (should be hashed in production).
     */
    private String password;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * Role of the user in the system.
     * Stored as a string in the database.
     */
    @Enumerated(EnumType.STRING)
    private Role role;
}
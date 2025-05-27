package com.kevinthomasbradley.incidentapi.service;

import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the UserService class.
 * Uses Mockito to mock the UserRepository dependency.
 */
class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Mocked repository for user data

    @InjectMocks
    private UserService userService; // Service under test

    /**
     * Initializes Mockito mocks before each test.
     */
    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test creating a user and verifying the returned user object.
     */
    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("pass");
        user.setEmail("test@example.com");
        user.setRole(User.Role.valueOf("CITIZEN"));

        when(userRepository.save(any(User.class))).thenReturn(user);

        User created = userService.createUser("test", "pass", "test@example.com", "CITIZEN");
        assertEquals("test", created.getUsername());
        assertEquals(User.Role.CITIZEN, created.getRole());
    }

    /**
     * Test retrieving all users.
     */
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(new User()));
        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
    }

    /**
     * Test retrieving a user by ID.
     */
    @Test
    void testGetUserById() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User found = userService.getUserById(1L);
        assertEquals(1L, found.getId());
    }
}
package com.kevinthomasbradley.incidentapi.controller;

import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Unit tests for the UserController class.
 * Uses MockMvc to simulate HTTP requests and Mockito to mock the UserService dependency.
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc for performing HTTP requests in tests

    @MockitoBean
    private UserService userService; // Mocked UserService injected into the controller

    /**
     * Test the user creation endpoint.
     * Mocks the service layer and verifies that a user can be created via POST /api/users.
     */
    @Test
    void testCreateUser() throws Exception {
        User user = new User();
        user.setUsername("john");
        user.setPassword("pass123");
        user.setEmail("john@example.com");
        user.setRole(User.Role.CITIZEN);

        Mockito.when(userService.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(user);

        String json = """
        {
          "username": "john",
          "password": "pass123",
          "email": "john@example.com",
          "role": "CITIZEN"
        }
        """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john"));
    }

    /**
     * Test the endpoint for retrieving all users.
     * Mocks the service layer and verifies that a GET request to /api/users returns a 200 OK status.
     */
    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.singletonList(new User()));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }
}
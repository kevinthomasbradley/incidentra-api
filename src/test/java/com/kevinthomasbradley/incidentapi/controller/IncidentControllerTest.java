package com.kevinthomasbradley.incidentapi.controller;

import com.kevinthomasbradley.incidentapi.model.Incident;
import com.kevinthomasbradley.incidentapi.model.Incident.IncidentType;
import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.service.IncidentService;
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
 * Unit tests for the IncidentController class.
 * Uses MockMvc to simulate HTTP requests and Mockito to mock the IncidentService dependency.
 */
@WebMvcTest(IncidentController.class)
class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IncidentService incidentService;

    /**
     * Test the incident creation endpoint.
     * Mocks the service layer and verifies that an incident can be created via POST /api/incidents.
     */
    @Test
    void testCreateIncident() throws Exception {

        User user = new User();
        user.setUsername("john");
        user.setPassword("pass123");
        user.setEmail("john@example.com");
        user.setRole(User.Role.CITIZEN);

        Incident incident = new Incident();
        incident.setDescription("Test incident");
        incident.setStatus(Incident.Status.REPORTED);
        incident.setIncidentType(IncidentType.OTHER);
        incident.setCreatedBy(user);

        Mockito.when(incidentService.createIncident(Mockito.anyString(), Mockito.anyLong()))
                .thenReturn(incident);

        String json = """
        {
          "description": "Test incident",
          "status": "REPORTED",
          "incidentType": "OTHER",
          "citizenId": 1
        }
        """;

        mockMvc.perform(post("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test incident"));
    }

    /**
     * Test the endpoint for retrieving all incidents.
     * Mocks the service layer and verifies that a GET request to /api/incidents returns a 200 OK status.
     */
    @Test
    void testGetAllIncidents() throws Exception {
        Mockito.when(incidentService.getAllIncidents()).thenReturn(Collections.singletonList(new Incident()));
        mockMvc.perform(get("/api/incidents"))
                .andExpect(status().isOk());
    }
}
package com.kevinthomasbradley.incidentapi.service;

import com.kevinthomasbradley.incidentapi.model.Incident;
import com.kevinthomasbradley.incidentapi.model.Incident.IncidentType;
import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.repository.IncidentRepository;
import com.kevinthomasbradley.incidentapi.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Unit tests for the IncidentService class.
 * Uses Mockito to mock the IncidentRepository dependency.
 */
class IncidentServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private IncidentRepository incidentRepository; // Mocked repository for incident data

    @InjectMocks
    private IncidentService incidentService; // Service under test

    /**
     * Initializes Mockito mocks before each test.
     */
    public IncidentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test creating an incident and verifying the returned incident object.
     */
    @Test
    void testCreateIncident() {

        User user = new User();
        user.setId(1L); // Match the ID used in createIncident
        user.setEmail("john@example.com");
        user.setRole(User.Role.CITIZEN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user)); // Correct mock

        Incident incident = new Incident();
        incident.setDescription("Test incident");
        incident.setStatus(Incident.Status.REPORTED);
        incident.setIncidentType(IncidentType.OTHER);
        incident.setCreatedBy(user);

        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);

        Incident created = incidentService.createIncident("Test incident", 1L);
        assertEquals("Test incident", created.getDescription());
        assertEquals(Incident.Status.REPORTED, created.getStatus());
        assertEquals(Incident.IncidentType.OTHER, created.getIncidentType());
    }

    /**
     * Test retrieving all incidents.
     */
    @Test
    void testGetAllIncidents() {
        when(incidentRepository.findAll()).thenReturn(Collections.singletonList(new Incident()));
        List<Incident> incidents = incidentService.getAllIncidents();
        assertEquals(1, incidents.size());
    }

    /**
     * Test retrieving an incident by ID.
     */
    @Test
    void testGetIncidentById() {
        Incident incident = new Incident();
        incident.setId(1L);
        when(incidentRepository.findById(1L)).thenReturn(Optional.of(incident));
        Incident found = incidentService.getIncidentById(1L);
        assertEquals(1L, found.getId());
    }
}
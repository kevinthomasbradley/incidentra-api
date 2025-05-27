package com.kevinthomasbradley.incidentapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kevinthomasbradley.incidentapi.model.Incident;
import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.repository.IncidentRepository;
import com.kevinthomasbradley.incidentapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Service class for managing incidents.
 * Handles business logic related to creating, assigning, resolving, and retrieving incidents.
 * Uses IncidentRepository and UserRepository for data access.
 */
@Service
@RequiredArgsConstructor
public class IncidentService {
    
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new incident reported by a citizen.
     *
     * @param description Description of the incident.
     * @param citizenId   ID of the reporting citizen.
     * @return The created Incident object.
     * @throws RuntimeException if the citizen is not found or does not have the CITIZEN role.
     */
    public Incident createIncident(String description, Long citizenId) {
        User citizen = userRepository.findById(citizenId)
            .filter(u -> u.getRole() == User.Role.CITIZEN)
            .orElseThrow(() -> new RuntimeException("Citizen not found"));
        
        Incident incident = new Incident();
        incident.setDescription(description);
        incident.setCreatedBy(citizen);
        return incidentRepository.save(incident);
    }

    /**
     * Assigns an incident to a dispatcher and a responder.
     *
     * @param incidentId   ID of the incident to assign.
     * @param dispatcherId ID of the dispatcher assigning the incident.
     * @param responderId  ID of the responder to whom the incident is assigned.
     * @return The updated Incident object.
     * @throws RuntimeException if the dispatcher, responder, or incident is not found,
     *                          or if the users do not have the correct roles.
     */
    public Incident assignIncident(Long incidentId, Long dispatcherId, Long responderId) {
        User dispatcher = userRepository.findById(dispatcherId)
            .filter(u -> u.getRole() == User.Role.DISPATCHER)
            .orElseThrow(() -> new RuntimeException("Dispatcher not found"));
        
        User responder = userRepository.findById(responderId)
            .filter(u -> u.getRole() == User.Role.RESPONDER)
            .orElseThrow(() -> new RuntimeException("Responder not found"));
        
        Incident incident = incidentRepository.findById(incidentId)
            .orElseThrow(() -> new RuntimeException("Incident not found"));
        
        incident.setAssignedBy(dispatcher);
        incident.setAssignedTo(responder);
        incident.setStatus(Incident.Status.ASSIGNED);
        return incidentRepository.save(incident);
    }

    /**
     * Marks an incident as resolved.
     *
     * @param incidentId ID of the incident to resolve.
     * @return The updated Incident object.
     * @throws RuntimeException if the incident is not found.
     */
    public Incident resolveIncident(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
            .orElseThrow(() -> new RuntimeException("Incident not found"));
        incident.setStatus(Incident.Status.RESOLVED);
        return incidentRepository.save(incident);
    }

    /**
     * Retrieves all incidents.
     *
     * @return List of all Incident objects.
     */
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    /**
     * Retrieves an incident by its ID.
     *
     * @param incidentId ID of the incident to retrieve.
     * @return The Incident object.
     * @throws RuntimeException if the incident is not found.
     */
    public Incident getIncidentById(Long incidentId) {
        return incidentRepository.findById(incidentId)
            .orElseThrow(() -> new RuntimeException("Incident not found"));
    }
    
}
package com.kevinthomasbradley.incidentapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kevinthomasbradley.incidentapi.model.Incident;
import com.kevinthomasbradley.incidentapi.service.IncidentService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * REST controller for managing incidents.
 * Provides endpoints for creating incidents, assigning incidents to dispatchers and responders,
 * resolving incidents, and retrieving all incidents.
 * Delegates business logic to the {@link IncidentService}.
 */
@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {
    
    private final IncidentService incidentService;

    /**
     * Creates a new incident reported by a citizen.
     *
     * @param request the request body containing incident details (description, citizenId)
     * @return the created Incident object with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Incident> createIncident(
            @RequestBody CreateIncidentRequest request) {
        Incident incident = incidentService.createIncident(
            request.getDescription(), 
            request.getCitizenId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(incident);
    }

    /**
     * Assigns an incident to a dispatcher and a responder.
     *
     * @param id      the ID of the incident to assign
     * @param request the request body containing dispatcherId and responderId
     * @return the updated Incident object with HTTP 200 status
     */
    @PutMapping("/{id}/assign")
    public ResponseEntity<Incident> assignIncident(
            @PathVariable Long id,
            @RequestBody AssignRequest request) {
        Incident incident = incidentService.assignIncident(
            id,
            request.getDispatcherId(),
            request.getResponderId()
        );
        return ResponseEntity.ok(incident);
    }

    /**
     * Retrieves all incidents in the system.
     * <p>
     * Note: This endpoint should be restricted to admin users in production.
     * </p>
     *
     * @return a list of all Incident objects with HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    /**
     * Marks an incident as resolved.
     *
     * @param id the ID of the incident to resolve
     * @return the updated Incident object with HTTP 200 status
     */
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Incident> resolveIncident(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.resolveIncident(id));
    }

    /**
     * Data Transfer Object (DTO) for incident creation requests.
     * Encapsulates the fields required to create a new incident.
     */
    @Data
    static class CreateIncidentRequest {
        /**
         * Description of the incident.
         */
        private String description;

        /**
         * ID of the citizen reporting the incident.
         */
        private Long citizenId;
    }

    /**
     * Data Transfer Object (DTO) for incident assignment requests.
     * Encapsulates the fields required to assign an incident.
     */
    @Data
    static class AssignRequest {
        /**
         * ID of the dispatcher assigning the incident.
         */
        private Long dispatcherId;

        /**
         * ID of the responder to whom the incident is assigned.
         */
        private Long responderId;
    }
}
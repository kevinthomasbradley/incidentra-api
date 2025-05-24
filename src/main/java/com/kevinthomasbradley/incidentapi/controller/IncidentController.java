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
import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.service.IncidentService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {
    
    private final IncidentService incidentService;
    
    @PostMapping
    public ResponseEntity<Incident> createIncident(
            @RequestBody CreateIncidentRequest request) {
        Incident incident = incidentService.createIncident(
            request.getDescription(), 
            request.getCitizenId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(incident);
    }
    
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

    // TODO: should be locked down to admin
    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }
    
    @PutMapping("/{id}/resolve")
    public ResponseEntity<Incident> resolveIncident(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.resolveIncident(id));
    }
    
    // DTO classes
    @Data
    static class CreateIncidentRequest {
        private String description;
        private Long citizenId;
    }
    
    @Data
    static class AssignRequest {
        private Long dispatcherId;
        private Long responderId;
    }
}

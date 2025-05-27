package com.kevinthomasbradley.incidentapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kevinthomasbradley.incidentapi.model.Incident;
import com.kevinthomasbradley.incidentapi.model.User;
import com.kevinthomasbradley.incidentapi.repository.IncidentRepository;
import com.kevinthomasbradley.incidentapi.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncidentService {
    
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    
    public Incident createIncident(String description, Long citizenId) {
        User citizen = userRepository.findById(citizenId)
            .filter(u -> u.getRole() == User.Role.CITIZEN)
            .orElseThrow(() -> new RuntimeException("Citizen not found"));
        
        Incident incident = new Incident();
        incident.setDescription(description);
        incident.setCreatedBy(citizen);
        return incidentRepository.save(incident);
    }
    
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
    
    public Incident resolveIncident(Long incidentId) {
        Incident incident = incidentRepository.findById(incidentId)
            .orElseThrow(() -> new RuntimeException("Incident not found"));
        incident.setStatus(Incident.Status.RESOLVED);
        return incidentRepository.save(incident);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident getIncidentById(Long incidentId) {
        return incidentRepository.findById(incidentId)
            .orElseThrow(() -> new RuntimeException("Incident not found"));
    }
    
}
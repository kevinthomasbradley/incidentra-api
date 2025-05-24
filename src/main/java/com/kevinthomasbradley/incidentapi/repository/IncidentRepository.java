package com.kevinthomasbradley.incidentapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevinthomasbradley.incidentapi.model.Incident;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatus(Incident.Status status);
}

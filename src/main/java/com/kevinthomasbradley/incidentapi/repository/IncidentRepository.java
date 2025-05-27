package com.kevinthomasbradley.incidentapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kevinthomasbradley.incidentapi.model.Incident;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Repository interface for Incident entities.
 * Extends JpaRepository to provide CRUD operations and custom query methods for Incident data.
 */
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    /**
     * Finds all incidents with the specified status.
     *
     * @param status the status of the incidents to retrieve (e.g., REPORTED, ASSIGNED, RESOLVED)
     * @return a list of Incident objects with the given status
     */
    List<Incident> findByStatus(Incident.Status status);
}
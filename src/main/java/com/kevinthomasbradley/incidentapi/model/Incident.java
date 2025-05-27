package com.kevinthomasbradley.incidentapi.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kevin T. Bradley | kevin.thomas.bradley@gmail.com
 * Entity representing an incident reported in the system.
 * Incidents can be of various types (FIRE, MEDICAL, POLICE, OTHER) and have different statuses
 * (REPORTED, ASSIGNED, RESOLVED). Each incident is associated with users who report, assign,
 * or respond to the incident.
 * This entity is mapped to the "incidents" table in the database.
 */
@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incident {

    /**
     * Enumeration of possible incident types.
     * FIRE: Fire-related incident.
     * MEDICAL: Medical emergency.
     * POLICE: Police-related incident.
     * OTHER: Any other type of incident.
     */
    public enum IncidentType { FIRE, MEDICAL, POLICE, OTHER }

    /**
     * Enumeration of possible incident statuses.
     * REPORTED: Incident has been reported but not yet assigned.
     * ASSIGNED: Incident has been assigned to a responder.
     * RESOLVED: Incident has been resolved.
     */
    public enum Status { REPORTED, ASSIGNED, RESOLVED }

    /**
     * Unique identifier for the incident.
     * Auto-generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Description of the incident.
     */
    private String description;

    /**
     * Current status of the incident.
     * Defaults to REPORTED.
     */
    @Enumerated(EnumType.STRING)
    private Status status = Status.REPORTED;

    /**
     * Type of the incident.
     * Defaults to OTHER.
     */
    @Enumerated(EnumType.STRING)
    private IncidentType incidentType = IncidentType.OTHER;

    /**
     * The user who reported (created) the incident.
     */
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    /**
     * The dispatcher who assigned the incident.
     */
    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    /**
     * The responder assigned to handle the incident.
     */
    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    /**
     * Timestamp when the incident was created.
     * Automatically set when the incident is persisted.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Timestamp when the incident was last updated.
     * Automatically updated when the incident is modified.
     */
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
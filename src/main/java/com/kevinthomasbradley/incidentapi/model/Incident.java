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

@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incident {
    
    public enum IncidentType { FIRE, MEDICAL, POLICE, OTHER }
    
    public enum Status { REPORTED, ASSIGNED, RESOLVED }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    private Status status = Status.REPORTED;

    @Enumerated(EnumType.STRING)
    private IncidentType incidentType = IncidentType.OTHER;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;
    
    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
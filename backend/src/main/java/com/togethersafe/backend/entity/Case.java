package com.togethersafe.backend.entity;

import com.togethersafe.backend.entity.enums.CasePriority;
import com.togethersafe.backend.entity.enums.CaseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_id", nullable = false)
    private Long orgId;

    @Column(name = "case_reference", nullable = false)
    private String caseReference;

    @Column(name = "tracking_reference")
    private String trackingReference;

    @Column(name = "reporter_id")
    private Long reporterId;

    @Column(name = "assigned_caseworker_id")
    private Long assignedCaseworkerId;

    @Column(name = "case_type", nullable = false)
    private String caseType;

    @Column(nullable = false)
    private String description;

    @Column(name = "incident_date")
    private LocalDate incidentDate;

    @Column(name = "location_summary")
    private String locationSummary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CaseStatus status = CaseStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CasePriority priority = CasePriority.MEDIUM;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

package com.togethersafe.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CaseResponse {
    private Long id;
    private String caseReference;
    private String caseType;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime createdAt;
}
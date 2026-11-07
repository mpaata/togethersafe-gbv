package com.togethersafe.backend.dto;

import lombok.Data;

@Data
public class CreateCaseRequest {
    private String caseType;
    private String description;
    private String locationSummary;
}
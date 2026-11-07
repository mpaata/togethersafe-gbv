package com.togethersafe.desktop.model;

public class CaseItem {
    private Long id;
    private String caseReference;
    private String caseType;
    private String description;
    private String status;
    private String priority;
    private String createdAt;

    public CaseItem(Long id, String caseReference, String caseType,
                    String description, String status, String priority, String createdAt) {
        this.id = id;
        this.caseReference = caseReference;
        this.caseType = caseType;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getCaseReference() { return caseReference; }
    public String getCaseType() { return caseType; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public String getCreatedAt() { return createdAt; }
}
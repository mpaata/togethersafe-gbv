package com.togethersafe.backend.controller;

import com.togethersafe.backend.dto.ApiResponse;
import com.togethersafe.backend.dto.CaseResponse;
import com.togethersafe.backend.dto.CreateCaseRequest;
import com.togethersafe.backend.entity.Case;
import com.togethersafe.backend.entity.enums.CaseStatus;
import com.togethersafe.backend.entity.enums.CasePriority;
import com.togethersafe.backend.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cases")
public class CaseController {

    @Autowired
    private CaseRepository caseRepository;

    @GetMapping
    public ApiResponse<List<CaseResponse>> getAllCases() {
        List<CaseResponse> cases = caseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ApiResponse.success(cases);
    }

    @PostMapping
    public ApiResponse<CaseResponse> createCase(@RequestBody CreateCaseRequest request) {
        Case newCase = new Case();
        newCase.setOrgId(1L);
        newCase.setCaseReference("CASE-" + UUID.randomUUID().toString().substring(0, 8));
        newCase.setCaseType(request.getCaseType());
        newCase.setDescription(request.getDescription());
        newCase.setLocationSummary(request.getLocationSummary());
        newCase.setStatus(CaseStatus.OPEN);
        newCase.setPriority(CasePriority.MEDIUM);
        newCase.setIsActive(true);
        newCase.setCreatedAt(LocalDateTime.now());
        
        Case saved = caseRepository.save(newCase);
        return ApiResponse.success(mapToResponse(saved));
    }

    private CaseResponse mapToResponse(Case c) {
        CaseResponse response = new CaseResponse();
        response.setId(c.getId());
        response.setCaseReference(c.getCaseReference());
        response.setCaseType(c.getCaseType());
        response.setDescription(c.getDescription());
        response.setStatus(c.getStatus().name());
        response.setPriority(c.getPriority() != null ? c.getPriority().name() : "MEDIUM");
        response.setCreatedAt(c.getCreatedAt());
        return response;
    }
}
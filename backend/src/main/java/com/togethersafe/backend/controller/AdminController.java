package com.togethersafe.backend.controller;

import com.togethersafe.backend.dto.ApiResponse;
import com.togethersafe.backend.entity.Case;
import com.togethersafe.backend.entity.User;
import com.togethersafe.backend.repository.CaseRepository;
import com.togethersafe.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard-stats")
    public ApiResponse<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        long totalCases = caseRepository.count();
        long totalUsers = userRepository.count();

        stats.put("activeCases", totalCases);
        stats.put("pendingReview", 12);
        stats.put("referrals", 18);
        stats.put("alerts", 6);
        stats.put("totalUsers", totalUsers);

        return ApiResponse.success(stats);
    }

    @GetMapping("/recent-cases")
    public ApiResponse<List<Case>> getRecentCases() {
        List<Case> cases = caseRepository.findAll();
        // Return the 5 most recent cases
        List<Case> recent = cases.stream()
                .sorted(Comparator.comparing(Case::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(5)
                .collect(Collectors.toList());
        return ApiResponse.success(recent);
    }

    @GetMapping("/cases")
    public ApiResponse<List<Case>> getAllCases() {
        return ApiResponse.success(caseRepository.findAll());
    }

    @GetMapping("/victims")
    public ApiResponse<List<User>> getVictims() {
        List<User> survivors = userRepository.findAll().stream()
                .filter(u -> u.getRole() != null && u.getRole().name().equals("SURVIVOR"))
                .collect(Collectors.toList());
        return ApiResponse.success(survivors);
    }

    @GetMapping("/analytics")
    public ApiResponse<Map<String, Object>> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalCases", caseRepository.count());
        analytics.put("survivorsSupported", 2856);
        analytics.put("resolutionRate", 82);
        analytics.put("caseTypes", Map.of(
                "Physical Assault", 42,
                "Sexual Assault", 28,
                "Emotional Abuse", 18,
                "Neglect", 12
        ));
        analytics.put("topLocations", Map.of(
                "Kampala", 247,
                "Wakiso", 89,
                "Mukono", 56,
                "Entebbe", 34
        ));
        return ApiResponse.success(analytics);
    }

    @GetMapping("/referrals")
    public ApiResponse<List<Map<String, Object>>> getReferrals() {
        // Placeholder: return empty list for now (no Referral entity yet)
        return ApiResponse.success(new ArrayList<>());
    }

    @GetMapping("/evidence")
    public ApiResponse<List<Map<String, Object>>> getEvidence() {
        return ApiResponse.success(new ArrayList<>());
    }

    @GetMapping("/services")
    public ApiResponse<List<Map<String, Object>>> getServices() {
        return ApiResponse.success(new ArrayList<>());
    }

    @GetMapping("/reports")
    public ApiResponse<List<Map<String, Object>>> getReports() {
        return ApiResponse.success(new ArrayList<>());
    }

    @GetMapping("/settings")
    public ApiResponse<Map<String, Object>> getSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.put("organizationName", "Safe Haven NGO");
        settings.put("contactEmail", "contact@safehaven.org");
        settings.put("contactPhone", "+256 700 123 456");
        return ApiResponse.success(settings);
    }
}
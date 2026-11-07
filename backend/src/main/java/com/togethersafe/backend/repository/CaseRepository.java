package com.togethersafe.backend.repository;

import com.togethersafe.backend.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CaseRepository extends JpaRepository<Case, Long> {
    List<Case> findByReporterId(Long reporterId);
    List<Case> findByAssignedCaseworkerId(Long caseworkerId);
}
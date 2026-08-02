package com.learnease.backend.repository;

import com.learnease.backend.entity.SummaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummaryHistoryRepository
        extends JpaRepository<SummaryHistory, Long> {
}
package com.learnease.backend.repository;

import com.learnease.backend.entity.QuizHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizHistoryRepository
        extends JpaRepository<QuizHistory, Long> {
}
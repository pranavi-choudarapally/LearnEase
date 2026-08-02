package com.learnease.backend.service;

import com.learnease.backend.entity.QuizHistory;
import com.learnease.backend.entity.SummaryHistory;
import com.learnease.backend.repository.QuizHistoryRepository;
import com.learnease.backend.repository.SummaryHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoryService {

    private final SummaryHistoryRepository summaryRepository;
    private final QuizHistoryRepository quizRepository;

    public HistoryService(
            SummaryHistoryRepository summaryRepository,
            QuizHistoryRepository quizRepository) {

        this.summaryRepository = summaryRepository;
        this.quizRepository = quizRepository;
    }

    public void saveSummary(Long pdfId,
                            String userEmail,
                            String summary) {

        SummaryHistory history = new SummaryHistory();

        history.setPdfId(pdfId);
        history.setUserEmail(userEmail);
        history.setSummary(summary);
        history.setGeneratedAt(LocalDateTime.now());

        summaryRepository.save(history);
    }

    public void saveQuiz(Long pdfId,
                         String userEmail,
                         int score,
                         int totalQuestions) {

        QuizHistory history = new QuizHistory();

        history.setPdfId(pdfId);
        history.setUserEmail(userEmail);
        history.setScore(score);
        history.setTotalQuestions(totalQuestions);

        history.setPercentage(
                (score * 100.0) / totalQuestions
        );

        history.setAttemptedAt(LocalDateTime.now());

        quizRepository.save(history);
    }

}
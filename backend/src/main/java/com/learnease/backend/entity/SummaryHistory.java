package com.learnease.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "summary_history")
public class SummaryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;

    private Long pdfId;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String summary;

    private LocalDateTime generatedAt;

    public SummaryHistory() {
    }

    public SummaryHistory(String userEmail,
                          Long pdfId,
                          String summary,
                          LocalDateTime generatedAt) {

        this.userEmail = userEmail;
        this.pdfId = pdfId;
        this.summary = summary;
        this.generatedAt = generatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getPdfId() {
        return pdfId;
    }

    public void setPdfId(Long pdfId) {
        this.pdfId = pdfId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
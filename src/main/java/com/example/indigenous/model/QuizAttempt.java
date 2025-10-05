package com.example.indigenous.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "quiz_attempts",
        indexes = {
                @Index(name = "idx_attempt_user_time", columnList = "user_email,taken_at")
        }
)
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // We store the authenticated user's email for simplicity (no FK needed)
    @Column(name = "user_email", nullable = false, length = 190)
    private String userEmail;

    @Column(nullable = false, length = 40)
    private String language;          // e.g., "OSHIWAMBO", "HERERO"

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions;

    @Column(nullable = false)
    private int correct;

    @Column(name = "elapsed_seconds", nullable = false)
    private int elapsedSeconds;

    @Column(name = "taken_at", nullable = false, updatable = false)
    private Instant takenAt;

    // --- Lifecycle hooks ---
    @PrePersist
    protected void onCreate() {
        if (takenAt == null) {
            takenAt = Instant.now();
        }
    }

    // --- Convenience computed value (not stored in DB) ---
    @Transient
    public double getPercent() {
        return totalQuestions == 0 ? 0.0 : (correct * 100.0) / totalQuestions;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public Instant getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Instant takenAt) {
        this.takenAt = takenAt;
    }

    // (Optional) toString for logging
    @Override
    public String toString() {
        return "QuizAttempt{" +
                "id=" + id +
                ", userEmail='" + userEmail + '\'' +
                ", language='" + language + '\'' +
                ", totalQuestions=" + totalQuestions +
                ", correct=" + correct +
                ", elapsedSeconds=" + elapsedSeconds +
                ", takenAt=" + takenAt +
                '}';
    }
}

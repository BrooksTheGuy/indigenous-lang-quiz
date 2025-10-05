package com.example.indigenous.dto;

/**
 * Payload sent from the quiz page when a user finishes.
 * Matches controller calls: getLanguage(), getTotalQuestions(), getCorrect(), getElapsedSeconds()
 */
public class QuizSubmissionRequest {

    private String language;        // e.g. "OSHIWAMBO"
    private int totalQuestions;     // total questions served
    private int correct;            // correct answers
    private int elapsedSeconds;     // time taken

    public QuizSubmissionRequest() {}

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
}

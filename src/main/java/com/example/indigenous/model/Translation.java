package com.example.indigenous.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"lang", "source_text"})
)
public class Translation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String lang;

    @Column(name = "source_text", nullable = false, length = 512)
    private String sourceText;

    @Column(name = "translated_text", nullable = false, length = 512)
    private String translatedText;

    // --- NEW: timestamps ---
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    // -----------------------

    protected Translation() {}

    public Translation(String lang, String sourceText, String translatedText) {
        this.lang = lang;
        this.sourceText = sourceText;
        this.translatedText = translatedText;
    }

    public Long getId() { return id; }

    public String getLang() { return lang; }
    public void setLang(String lang) { this.lang = lang; }

    public String getSourceText() { return sourceText; }
    public void setSourceText(String sourceText) { this.sourceText = sourceText; }

    public String getTranslatedText() { return translatedText; }
    public void setTranslatedText(String translatedText) { this.translatedText = translatedText; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

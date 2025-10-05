package com.example.indigenous.repository;

import com.example.indigenous.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TranslationRepository extends JpaRepository<Translation, Long> {

    Optional<Translation> findByLangAndSourceText(String lang, String sourceText);

    List<Translation> findTop100ByOrderByIdDesc();

    // Newest row by timestamp, if your entity has updatedAt/createdAt
    Optional<Translation> findTopByOrderByUpdatedAtDesc();
    Optional<Translation> findTopByOrderByCreatedAtDesc();

    // Safe fallback if you don’t have timestamps yet
    Optional<Translation> findTopByOrderByIdDesc();

    // (Optional) counts per language for a mini breakdown chip/widget
    @Query("select t.lang, count(t) from Translation t group by t.lang")
    List<Object[]> countByLang();
}

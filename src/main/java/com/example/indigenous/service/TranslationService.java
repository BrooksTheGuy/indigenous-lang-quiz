package com.example.indigenous.service;

import com.example.indigenous.repository.TranslationRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TranslationService {
    private final TranslationRepository repo;
    private final Map<String, Map<String, String>> fallback = new HashMap<>();

    public TranslationService(TranslationRepository repo) {
        this.repo = repo;
        seedFallback();
    }

    private void seedFallback() {
        put("OSHIWAMBO", "Hello", "Wa uhala po");
        put("OSHIWAMBO", "Thank you", "Tangi unene");
        // TODO add the rest of your pairs here
    }

    private void put(String lang, String en, String tr) {
        fallback.computeIfAbsent(lang.toUpperCase(), k -> new HashMap<>()).put(en, tr);
    }

    /** DB first; fallback; else empty. */
    public Optional<String> translate(String lang, String english) {
        if (lang == null || english == null) return Optional.empty();
        String L = lang.trim().toUpperCase();
        String src = english.trim();

        var db = repo.findByLangAndSourceText(L, src);
        if (db.isPresent()) return Optional.of(db.get().getTranslatedText());

        var m = fallback.get(L);
        if (m != null && m.containsKey(src)) return Optional.of(m.get(src));

        return Optional.empty();
    }
}

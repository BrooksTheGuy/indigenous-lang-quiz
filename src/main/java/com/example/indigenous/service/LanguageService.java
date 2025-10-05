package com.example.indigenous.service;

import com.example.indigenous.i18n.Language;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    public List<Language> all() {
        return List.of(Language.values());
    }

    public Optional<Language> findByCode(String code) {
        if (code == null) return Optional.empty();
        return Arrays.stream(Language.values())
                .filter(l -> l.name().equalsIgnoreCase(code))
                .findFirst();
    }


    public Language defaultLang() {
        return Language.OSHIWAMBO; // pick whichever you prefer
    }
}

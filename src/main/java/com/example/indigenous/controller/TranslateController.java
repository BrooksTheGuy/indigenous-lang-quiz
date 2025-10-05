package com.example.indigenous.controller;

import com.example.indigenous.i18n.Language;
import com.example.indigenous.service.LanguageService;
import com.example.indigenous.service.TranslationService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class TranslateController {

    private final TranslationService translationService;
    private final LanguageService languageService;

    public TranslateController(TranslationService translationService,
                               LanguageService languageService) {
        this.translationService = translationService;
        this.languageService = languageService;
    }

    // Page
    @GetMapping("/translate")
    public String translate(Model model) {
        model.addAttribute("languages", languageService.all());
        model.addAttribute("target", languageService.defaultLang());
        return "translate";
    }

    @GetMapping(value = "/api/translate", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> apiTranslate(@RequestParam String text,
                                            @RequestParam String target) {

        Language lang = languageService.findByCode(target)
                .orElse(languageService.defaultLang());

        // your TranslationService expects a String, so pass the enum name (or code if you added one)
        String translation = translationService.translate(lang.name(), text).orElse("");

        // If you DON'T have a note(...) method, just return an empty note
        String note = ""; // or: translationService.note(lang.name(), text).orElse("");

        return Map.of("translation", translation, "note", note);
    }

}

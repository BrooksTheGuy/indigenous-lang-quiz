package com.example.indigenous.controller;

import com.example.indigenous.repository.QuizAttemptRepository;
import com.example.indigenous.repository.TranslationRepository;
import com.example.indigenous.repository.UserRepository;
import com.example.indigenous.service.LanguageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final TranslationRepository translationRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final LanguageService languageService;   // <-- inject this

    public AdminController(UserRepository userRepository,
                           TranslationRepository translationRepository,
                           QuizAttemptRepository quizAttemptRepository,
                           LanguageService languageService) {
        this.userRepository = userRepository;
        this.translationRepository = translationRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.languageService = languageService;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("userCount",         userRepository.count());
        model.addAttribute("translationCount",  translationRepository.count());
        model.addAttribute("attemptCount",      quizAttemptRepository.count());
        model.addAttribute("languageCount",     languageService.all().size()); // uses your service

        // "Last upload" — prefer updatedAt, else createdAt, else fallback by id
        translationRepository.findTopByOrderByUpdatedAtDesc()
                .or(() -> translationRepository.findTopByOrderByCreatedAtDesc())
                .or(() -> translationRepository.findTopByOrderByIdDesc())
                .ifPresent(t -> model.addAttribute("lastUpload",
                        t.getUpdatedAt() != null ? t.getUpdatedAt() : t.getCreatedAt()));

        model.addAttribute("recentUploads", translationRepository.findTop100ByOrderByIdDesc());
        return "admin/dashboard";
    }
}

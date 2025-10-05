package com.example.indigenous.controller;

import com.example.indigenous.model.QuizAttempt;
import com.example.indigenous.repository.QuizAttemptRepository;
import com.example.indigenous.dto.QuizSubmissionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuizController {
    private final QuizAttemptRepository attempts;
    public QuizController(QuizAttemptRepository attempts){ this.attempts = attempts; }

    @PostMapping(path="/quiz/submit", consumes="application/json", produces="application/json")
    @ResponseBody
    public ResponseEntity<?> submit(@RequestBody QuizSubmissionRequest req,
                                    @AuthenticationPrincipal UserDetails principal){
        String email = principal.getUsername(); // adjust if your principal stores email differently
        QuizAttempt a = new QuizAttempt();
        a.setUserEmail(email);
        a.setLanguage(req.getLanguage());
        a.setTotalQuestions(req.getTotalQuestions());
        a.setCorrect(req.getCorrect());
        a.setElapsedSeconds(req.getElapsedSeconds());
        attempts.save(a);
        return ResponseEntity.ok().body("{\"status\":\"ok\",\"id\":"+a.getId()+"}");
    }

    @GetMapping("/quiz/history")
    public String history(@AuthenticationPrincipal UserDetails principal, Model model){
        model.addAttribute("attempts", attempts.findTop20ByUserEmailOrderByTakenAtDesc(principal.getUsername()));
        return "quiz-history";
    }
}

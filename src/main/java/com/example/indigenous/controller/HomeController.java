// e.g. src/main/java/com/example/indigenous/controller/HomeController.java
package com.example.indigenous.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("/quiz")
    public String quizPage(
            @RequestParam(value = "lang", required = false, defaultValue = "OSHIWAMBO") String lang,
            Model model) {
        model.addAttribute("language", lang); // used by quiz.html if present
        return "quiz"; // resolves templates/quiz.html
    }
}

package com.example.indigenous.controller;

import com.example.indigenous.dto.RegisterRequest;
import com.example.indigenous.model.Role;
import com.example.indigenous.model.User;
import com.example.indigenous.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.EnumSet;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new RegisterRequest());
        }
        model.addAttribute("mode", "login");
        return "auth";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new RegisterRequest());
        }
        model.addAttribute("mode", "signup");
        return "auth";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("form") RegisterRequest form,
                           BindingResult binding,
                           RedirectAttributes ra) {

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            binding.rejectValue("confirmPassword", "mismatch", "Passwords do not match");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            binding.rejectValue("email", "exists", "An account with this email already exists");
        }
        if (binding.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", binding);
            ra.addFlashAttribute("form", form);
            return "redirect:/register";
        }

        User u = new User();
        u.setEmail(form.getEmail().toLowerCase().trim());
        u.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        u.setRoles(EnumSet.of(Role.USER));
        userRepository.save(u);

        ra.addFlashAttribute("registered", true);
        return "redirect:/login?registered";
    }
}

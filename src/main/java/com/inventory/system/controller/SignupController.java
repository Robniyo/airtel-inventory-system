package com.inventory.system.controller;

import com.inventory.system.entity.User;
import com.inventory.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user) {
        try {
            // Safety: Check if username already exists to avoid the "already inserted" SQL error
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return "redirect:/signup?error=exists";
            }

            userRepository.save(user);
            return "redirect:/login?success";
        } catch (Exception e) {
            // Catches any database "Index" or "Constraint" errors
            return "redirect:/signup?error=conflict";
        }
    }
}

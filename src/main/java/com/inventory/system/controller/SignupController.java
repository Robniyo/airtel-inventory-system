package com.inventory.system.controller;

import com.inventory.system.entity.User;
import com.inventory.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignupController {

    @Autowired(required = false)
    private UserRepository userRepository;

    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user) {
        try {
            if (userRepository != null) {
                userRepository.save(user);
                return "redirect:/login?success";
            }
        } catch (Exception e) {
            // Redirects to conflict if user already exists
            return "redirect:/signup?error=conflict";
        }
        return "redirect:/signup?error";
    }
}

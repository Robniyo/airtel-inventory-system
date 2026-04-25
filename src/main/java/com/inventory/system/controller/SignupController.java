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

    // 1. Display the Signup Form
    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        // Ensure the 'user' object is never null when the page loads
        model.addAttribute("user", new User());
        return "signup";
    }

    // 2. Process the Registration
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user) {
        try {
            // Check if userRepository is available to prevent null pointer crashes
            if (userRepository == null) {
                return "redirect:/signup?error=server_issue";
            }

            // Simple validation to ensure fields aren't empty
            if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
                return "redirect:/signup?error=invalid_data";
            }

            // Save the user (Defaulting to STAFF role implicitly via logic)
            userRepository.save(user);
            
            // Redirect to login with a success parameter
            return "redirect:/login?success";
            
        } catch (Exception e) {
            // Print the error to Render logs so you can see it
            e.printStackTrace();
            // Redirect back to signup with an error message instead of crashing (500)
            return "redirect:/signup?error";
        }
    }
}

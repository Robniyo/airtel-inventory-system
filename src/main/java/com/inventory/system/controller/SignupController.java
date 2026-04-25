package com.inventory.system.controller;

import com.inventory.system.entity.User;
import com.inventory.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Displays the signup form.
     * Injects a new User object into the model for form binding.
     */
    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    /**
     * Processes the registration of a new Staff User.
     * Checks if the Registration Number (Username) is already in use.
     */
    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        
        // 1. Validation: Ensure the Registration Number (Username) is unique
        // We use findByUsername which we previously defined in the UserRepository
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            // Redirects back to signup with an error parameter if username exists
            return "redirect:/signup?error=exists";
        }
        
        // 2. Logic: Save the new Staff Member to the 'users' table
        // Note: This user will have the 'STAFF' role logic applied during login
        userRepository.save(user);
        
        // 3. Success: Redirect to the login page with a success notification
        return "redirect:/login?signupSuccess=true";
    }
}

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
            // If you get a 'cannot find symbol setRole' error, 
            // it means your User entity is missing the role field.
            // user.setRole("STAFF"); 
            userRepository.save(user);
            return "redirect:/login?success";
        } catch (Exception e) {
            return "redirect:/signup?error";
        }
    }
}

package com.inventory.system.controller;

import com.inventory.system.entity.User;
import com.inventory.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute User user, Model model) {
        try {
            // Set default role for new signups
            user.setRole("STAFF"); 
            userRepository.save(user);
            return "redirect:/login?success";
        } catch (Exception e) {
            // If database fails, this prevents the 500 Whitelabel error
            return "redirect:/signup?error";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // Static Admin Check for your specific ID
        if ("24RP05300".equals(username) && "admin123".equals(password)) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setUsername("24RP05300");
            session.setAttribute("user", admin);
            session.setAttribute("role", "ADMIN");
            return "redirect:/dashboard";
        }

        // Database check for regular users
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("user", user.get());
            session.setAttribute("role", "STAFF");
            return "redirect:/dashboard";
        }

        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

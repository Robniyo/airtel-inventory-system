package com.inventory.system.controller;

import com.inventory.system.entity.User;
import com.inventory.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired(required = false)
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // 1. HARDCODED ADMIN (No Database required)
        // Use your Reg Number for both
        if ("24RP05300".equals(username) && "24RP05300".equals(password)) {
            User admin = new User();
            admin.setName("Airtel Admin");
            admin.setUsername("24RP05300");
            session.setAttribute("user", admin);
            session.setAttribute("role", "ADMIN");
            return "redirect:/dashboard";
        }

        // 2. STAFF CHECK (With Error Protection)
        try {
            if (userRepository != null) {
                Optional<User> userOpt = userRepository.findByUsername(username);
                if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                    session.setAttribute("user", userOpt.get());
                    session.setAttribute("role", "STAFF");
                    return "redirect:/dashboard";
                }
            }
        } catch (Exception e) {
            // Log the error but don't show Whitelabel 500
            System.out.println("Database Login Error: " + e.getMessage());
        }

        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) session.invalidate();
        return "redirect:/login";
    }
}

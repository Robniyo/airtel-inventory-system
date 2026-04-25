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

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        try {
            // 1. HARDCODED ADMIN CHECK (Always works even without DB)
            if ("24RP05300".equals(username) && "admin123".equals(password)) {
                User admin = new User();
                admin.setName("System Admin");
                admin.setUsername("24RP05300");
                session.setAttribute("user", admin);
                session.setAttribute("role", "ADMIN");
                return "redirect:/dashboard";
            }

            // 2. DATABASE STAFF CHECK
            if (userRepository == null) return "redirect:/login?error";
            
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.getPassword() != null && user.getPassword().equals(password)) {
                    session.setAttribute("user", user);
                    session.setAttribute("role", "STAFF");
                    return "redirect:/dashboard";
                }
            }
        } catch (Exception e) {
            // This catches the 500 error and just sends you back to login safely
            return "redirect:/login?error=db_fail";
        }

        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}

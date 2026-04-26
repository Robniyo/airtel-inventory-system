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
        // 1. ABSOLUTE ADMIN CHECK
        if ("24RP05300".equals(username) && "24RP05300".equals(password)) {
            User admin = new User();
            admin.setUsername("24RP05300");
            admin.setName("System Administrator");
            session.setAttribute("user", admin);
            session.setAttribute("role", "ADMIN");
            return "redirect:/dashboard";
        }

        // 2. STAFF CHECK
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            session.setAttribute("user", userOpt.get());
            session.setAttribute("role", "STAFF");
            return "redirect:/dashboard";
        }

        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) session.invalidate();
        return "redirect:/login";
    }
}

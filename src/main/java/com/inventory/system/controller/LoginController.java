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
    public String loginPage(HttpSession session) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        // CLEAN THE SESSION FIRST
        session.invalidate();
        HttpSession newSession = null;

        // 1. CHECK ADMIN
        if ("24RP05300".equals(username) && "24RP05300".equals(password)) {
            newSession = createNewSession(session);
            User admin = new User();
            admin.setUsername("24RP05300");
            admin.setName("System Administrator");
            newSession.setAttribute("user", admin);
            newSession.setAttribute("role", "ADMIN");
            return "redirect:/dashboard";
        }

        // 2. CHECK STAFF
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            newSession = createNewSession(session);
            newSession.setAttribute("user", userOpt.get());
            newSession.setAttribute("role", "STAFF");
            return "redirect:/dashboard";
        }

        return "redirect:/login?error";
    }

    // Helper to ensure fresh session creation
    private HttpSession createNewSession(HttpSession oldSession) {
        return (oldSession != null) ? oldSession : null; 
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) session.invalidate();
        return "redirect:/login";
    }
}

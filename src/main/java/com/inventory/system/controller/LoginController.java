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
        // 1. PRIORITY: Hardcoded Admin (Bypasses Database)
        // This will work even if Render shows a "db_fail"
        if ("24RP05300".equals(username) && "24RP05300".equals(password)) {
            User admin = new User();
            admin.setName("Airtel Admin");
            admin.setUsername("24RP05300");
            session.setAttribute("user", admin);
            session.setAttribute("role", "ADMIN");
            return "redirect:/dashboard";
        }

        // 2. Staff Database Login
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
            return "redirect:/login?error=db_fail";
        }

        return "redirect:/login?error=invalid";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

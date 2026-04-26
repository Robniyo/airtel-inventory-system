package com.inventory.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // This replaces the IndexController. It sends everyone to /login
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        HttpSession session) {
        
        // Credentials: 24RP05300 / 24RP12881
        if ("24RP05300".equals(username) && "24RP12881".equals(password)) {
            session.setAttribute("role", "ADMIN");
            session.setAttribute("userName", "System Admin");
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

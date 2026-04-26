package com.inventory.system.controller;

import com.inventory.system.repository.AssetRepository;
import com.inventory.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private AssetRepository assetRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password, 
                        HttpSession session) {
        
        // STATIC CREDENTIALS CHECK
        if ("24RP05300".equals(username) && "24RP12881".equals(password)) {
            session.setAttribute("role", "ADMIN");
            session.setAttribute("username", "Administrator");
            System.out.println(">>> LOGIN SUCCESS: Admin Authenticated");
            return "redirect:/dashboard";
        }

        System.out.println(">>> LOGIN FAILED: Invalid credentials");
        return "redirect:/login?error=true";
    }

    // EMERGENCY RESET: Call this URL to empty the database table
    @GetMapping("/reset-db")
    @ResponseBody
    public String resetDatabase() {
        assetRepository.deleteAll();
        return "Database Cleared! All assets have been removed.";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

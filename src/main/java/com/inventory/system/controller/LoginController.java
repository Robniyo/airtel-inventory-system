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
        // Step 1: Force a clean session state
        session.invalidate(); 
        
        // Step 2: MASTER ADMIN OVERRIDE (Hardcoded for your specific ID)
        if ("24RP05300".equals(username) && "24RP05300".equals(password)) {
            User admin = new User();
            admin.setUsername("24RP05300");
            admin.setName("System Administrator");
            
            // Create fresh session
            session.setAttribute("user", admin);
            session.setAttribute("role", "ADMIN"); 
            System.out.println(">>> LOGIN SUCCESS: Admin role assigned to " + username);
            return "redirect:/dashboard";
        }

        // Step 3: DATABASE STAFF CHECK
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User dbUser = userOpt.get();
            if (dbUser.getPassword().equals(password)) {
                session.setAttribute("user", dbUser);
                session.setAttribute("role", "STAFF");
                System.out.println(">>> LOGIN SUCCESS: Staff role assigned to " + username);
                return "redirect:/dashboard";
            }
        }

        System.out.println(">>> LOGIN FAILED for user: " + username);
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}

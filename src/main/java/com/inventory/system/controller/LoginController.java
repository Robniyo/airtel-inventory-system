package com.inventory.system.controller;

import com.inventory.system.entity.User;
import com.inventory.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    // Hardcoded Master Admin Credentials
    private static final String ADMIN_EMAIL = "adminAirtel@gmail.com";
    private static final String ADMIN_PASS = "AirtelPass";

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, 
                               @RequestParam String password, 
                               HttpSession session,
                               Model model) {
        
        // 1. Check for Master Admin Access first
        if (ADMIN_EMAIL.equalsIgnoreCase(email) && ADMIN_PASS.equals(password)) {
            User admin = new User();
            admin.setName("System Administrator");
            admin.setEmail(ADMIN_EMAIL);
            // If your User entity has a department field, set it here
            session.setAttribute("user", admin); 
            return "redirect:/dashboard";
        }

        // 2. Check Database for Staff Users using Optional
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Check if password matches
            if (user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                return "redirect:/dashboard";
            }
        }

        // 3. If everything fails, return to login with error
        model.addAttribute("error", "Invalid Airtel credentials. Please try again.");
        return "login";
    }

    /**
     * Standard Logout:
     * Clears the session and returns to the login portal.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate(); 
        }
        return "redirect:/login";
    }
}
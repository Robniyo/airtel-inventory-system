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

    // Static Admin Credentials as requested
    private static final String ADMIN_USER = "24RP05300";
    private static final String ADMIN_PASS = "24RP12881";

    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        // If already logged in, skip login page
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        return "login"; 
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, 
                               @RequestParam String password, 
                               @RequestParam String role, 
                               HttpSession session,
                               Model model) {
        
        // ADMIN PATH
        if ("admin".equalsIgnoreCase(role)) {
            if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
                User admin = new User();
                admin.setName("Airtel Administrator");
                admin.setUsername(ADMIN_USER);
                admin.setEmail("admin@airtel.com"); // Placeholder
                
                session.setAttribute("user", admin);
                session.setAttribute("role", "ADMIN");
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Invalid Admin Credentials");
                return "login";
            }
        }

        // USER (STAFF) PATH
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                session.setAttribute("user", user);
                session.setAttribute("role", "STAFF");
                return "redirect:/dashboard";
            }
        }

        model.addAttribute("error", "Invalid Staff Credentials or Account not found");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}

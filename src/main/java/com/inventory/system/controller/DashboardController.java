package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.entity.User;
import com.inventory.system.repository.AssetRepository;
import com.inventory.system.repository.AssetRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class DashboardController {

    @Autowired
    private AssetRepository assetRepository;
    
    @Autowired
    private AssetRequestRepository requestRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        if (user == null || role == null) return "redirect:/login";

        model.addAttribute("user", user);
        
        // Use try-catch to prevent 500 errors if DB is slow
        try {
            model.addAttribute("assets", assetRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("assets", new ArrayList<>());
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            model.addAttribute("asset", new Asset());
            try {
                model.addAttribute("pendingRequests", requestRepository.findAll());
            } catch (Exception e) {
                model.addAttribute("pendingRequests", new ArrayList<>());
            }
            return "admin_dashboard";
        } 
        return "staff_dashboard";
    }
}

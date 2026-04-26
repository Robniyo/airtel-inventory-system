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
import java.util.List;

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

        // 1. Session check
        if (user == null || role == null) {
            System.out.println(">>> DASHBOARD REJECTED: No session found.");
            return "redirect:/login";
        }

        // 2. Load Shared Data
        List<Asset> allAssets = assetRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("assets", allAssets != null ? allAssets : new ArrayList<>());
        model.addAttribute("totalAssets", allAssets != null ? allAssets.size() : 0);

        // 3. LOGGING FOR DEBUGGING (Check your Render logs for this!)
        System.out.println(">>> ACCESSING DASHBOARD: User=" + user.getUsername() + " | Role=" + role);

        // 4. Forced Redirection based on Role
        if ("ADMIN".equalsIgnoreCase(role)) {
            model.addAttribute("asset", new Asset()); // Blank asset for the 'Add' form
            model.addAttribute("pendingRequests", requestRepository.findAll());
            System.out.println(">>> LOADING: admin_dashboard.html");
            return "admin_dashboard"; 
        } else {
            System.out.println(">>> LOADING: staff_dashboard.html");
            return "staff_dashboard";
        }
    }
}

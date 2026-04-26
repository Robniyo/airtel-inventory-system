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

        // Kick out if not logged in
        if (user == null || role == null) return "redirect:/login";

        // Data for both pages
        model.addAttribute("user", user);
        model.addAttribute("assets", assetRepository.findAll());
        model.addAttribute("totalAssets", assetRepository.count());

        // THE TRAFFIC COP LOGIC
        if ("ADMIN".equals(role)) {
            model.addAttribute("asset", new Asset()); // Form object for Admin
            model.addAttribute("pendingRequests", requestRepository.findAll());
            return "admin_dashboard"; // This looks for admin_dashboard.html
        } else {
            return "staff_dashboard"; // This looks for staff_dashboard.html
        }
    }
}

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

        // Shared Data
        model.addAttribute("user", user);
        model.addAttribute("assets", assetRepository.findAll());
        model.addAttribute("totalAssets", assetRepository.count());
        model.addAttribute("assignedCount", assetRepository.findAll().stream()
                .filter(a -> "ASSIGNED".equalsIgnoreCase(String.valueOf(a.getStatus()))).count());

        // THE ABSOLUTE SPLIT
        if ("ADMIN".equals(role)) {
            model.addAttribute("asset", new Asset()); // For the add form
            model.addAttribute("pendingRequests", requestRepository.findAll());
            return "admin_dashboard"; // LOOKS FOR admin_dashboard.html
        } else {
            return "staff_dashboard"; // LOOKS FOR staff_dashboard.html
        }
    }
}

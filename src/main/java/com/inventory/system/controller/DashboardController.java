package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.entity.AssetRequest;
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
        // Retrieve session data
        User currentUser = (User) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        // SECURITY GUARD: If no user or role, kick back to login
        if (currentUser == null || role == null) {
            return "redirect:/login";
        }

        // 1. DATA FOR EVERYONE
        List<Asset> allAssets = assetRepository.findAll();
        model.addAttribute("assets", allAssets != null ? allAssets : new ArrayList<>());
        model.addAttribute("user", currentUser); // Used for ${user.name}
        model.addAttribute("role", role);        // Used for ${role == 'ADMIN'}
        model.addAttribute("totalAssets", allAssets != null ? allAssets.size() : 0);
        model.addAttribute("asset", new Asset()); // Blank asset for the "Add" form

        long assignedCount = 0;
        if (allAssets != null) {
            assignedCount = allAssets.stream()
                .filter(a -> a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                .count();
        }
        model.addAttribute("assignedCount", assignedCount);

        // 2. DATA ONLY FOR ADMIN
        if ("ADMIN".equals(role)) {
            List<AssetRequest> requests = requestRepository.findAll();
            model.addAttribute("pendingRequests", requests != null ? requests : new ArrayList<>());
        } else {
            // Staff sees an empty list so Thymeleaf doesn't crash
            model.addAttribute("pendingRequests", new ArrayList<>());
        }

        return "dashboard";
    }
}

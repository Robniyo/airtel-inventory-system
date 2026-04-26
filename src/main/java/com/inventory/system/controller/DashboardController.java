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
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private AssetRepository assetRepository;
    
    @Autowired
    private AssetRequestRepository requestRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Security Check: Is anyone logged in?
        User currentUser = (User) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        if (currentUser == null || role == null) {
            return "redirect:/login";
        }

        // 1. Load Common Data
        List<Asset> allAssets = assetRepository.findAll();
        model.addAttribute("assets", allAssets);
        model.addAttribute("user", currentUser);
        model.addAttribute("role", role); // Send the role string to HTML ('ADMIN' or 'STAFF')
        model.addAttribute("totalAssets", allAssets.size());
        model.addAttribute("asset", new Asset()); 

        long assigned = allAssets.stream()
                .filter(a -> a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                .count();
        model.addAttribute("assignedCount", assigned);

        // 2. Role-Based Logic
        if ("ADMIN".equals(role)) {
            // Admin sees everything + approval hub
            List<AssetRequest> pending = requestRepository.findAll().stream()
                .filter(r -> r.getStatus() != null && "PENDING".equalsIgnoreCase(r.getStatus().toString()))
                .collect(Collectors.toList());
            model.addAttribute("pendingRequests", pending);
        } else {
            // Staff sees NO pending requests (Security)
            model.addAttribute("pendingRequests", new ArrayList<AssetRequest>());
        }

        return "dashboard";
    }
}

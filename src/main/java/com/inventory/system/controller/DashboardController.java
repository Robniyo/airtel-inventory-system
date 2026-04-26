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
        User currentUser = (User) session.getAttribute("user");
        String sessionRole = (String) session.getAttribute("role");

        if (currentUser == null) return "redirect:/login";

        // Pass data to UI
        List<Asset> allAssets = assetRepository.findAll();
        model.addAttribute("assets", allAssets);
        model.addAttribute("user", currentUser);
        model.addAttribute("role", sessionRole); // Sending role directly to HTML
        model.addAttribute("totalAssets", allAssets.size());
        model.addAttribute("asset", new Asset()); // For the 'Add Asset' form

        long assigned = allAssets.stream()
                .filter(a -> a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                .count();
        model.addAttribute("assignedCount", assigned);

        // If Admin, also send pending requests
        if ("ADMIN".equals(sessionRole)) {
            List<AssetRequest> pending = requestRepository.findAll().stream()
                .filter(r -> r.getStatus() != null && "PENDING".equalsIgnoreCase(r.getStatus().toString()))
                .collect(Collectors.toList());
            model.addAttribute("pendingRequests", pending);
        }

        return "dashboard";
    }
}

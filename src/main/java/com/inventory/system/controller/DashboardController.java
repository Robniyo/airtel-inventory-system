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
        String role = (String) session.getAttribute("role");

        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Asset> allAssets = assetRepository.findAll();
        model.addAttribute("assets", allAssets);
        model.addAttribute("totalAssets", allAssets.size());
        model.addAttribute("user", currentUser);
        model.addAttribute("role", role);

        // Logic for ADMIN (24RP05300)
        if ("ADMIN".equals(role)) {
            List<AssetRequest> allRequests = requestRepository.findAll();
            model.addAttribute("pendingRequests", allRequests.stream()
                .filter(r -> "PENDING".equals(r.getStatus().toString()))
                .collect(Collectors.toList()));
            model.addAttribute("isAdmin", true);
            return "dashboard"; // Loads the admin version of dashboard
        } 
        
        // Logic for STAFF
        model.addAttribute("isAdmin", false);
        return "dashboard"; 
    }
}

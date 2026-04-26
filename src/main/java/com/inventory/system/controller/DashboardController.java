package com.inventory.system.controller;

import com.inventory.system.entity.*;
import com.inventory.system.repository.*;
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
        // 1. Session Safety Check
        Object userObj = session.getAttribute("user");
        Object roleObj = session.getAttribute("role");

        if (userObj == null || roleObj == null) {
            return "redirect:/login";
        }

        try {
            // 2. Load Assets
            List<Asset> allAssets = assetRepository.findAll();
            model.addAttribute("assets", allAssets != null ? allAssets : new ArrayList<>());
            model.addAttribute("user", userObj);
            model.addAttribute("role", roleObj.toString());
            model.addAttribute("totalAssets", allAssets != null ? allAssets.size() : 0);
            model.addAttribute("asset", new Asset());

            // 3. Count Deployed
            long assigned = 0;
            if (allAssets != null) {
                assigned = allAssets.stream()
                    .filter(a -> a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                    .count();
            }
            model.addAttribute("assignedCount", assigned);

            // 4. Admin Only Data
            if ("ADMIN".equals(roleObj.toString())) {
                List<AssetRequest> allReqs = requestRepository.findAll();
                List<AssetRequest> pending = new ArrayList<>();
                if (allReqs != null) {
                    for (AssetRequest r : allReqs) {
                        if (r.getStatus() != null && "PENDING".equalsIgnoreCase(r.getStatus().toString())) {
                            pending.add(r);
                        }
                    }
                }
                model.addAttribute("pendingRequests", pending);
            } else {
                model.addAttribute("pendingRequests", new ArrayList<>());
            }

        } catch (Exception e) {
            // If database fails, still show the page with empty lists instead of a 500 error
            model.addAttribute("assets", new ArrayList<>());
            model.addAttribute("pendingRequests", new ArrayList<>());
            model.addAttribute("role", roleObj.toString());
            System.out.println("Dashboard Error: " + e.getMessage());
        }

        return "dashboard";
    }
}

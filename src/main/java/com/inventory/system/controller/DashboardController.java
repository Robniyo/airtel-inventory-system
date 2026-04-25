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
import org.springframework.web.bind.annotation.PathVariable;
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
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        return prepareDashboardView(model, new Asset(), false, session);
    }

    @GetMapping("/dashboard/edit/{id}")
    public String editAsset(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("user") == null || !"ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/dashboard";
        }
        Asset assetToEdit = assetRepository.findById(id).orElse(new Asset());
        return prepareDashboardView(model, assetToEdit, true, session);
    }

    private String prepareDashboardView(Model model, Asset asset, boolean openFleet, HttpSession session) {
        try {
            List<Asset> allAssets = assetRepository.findAll();
            if (allAssets == null) allAssets = new ArrayList<>();
            
            List<AssetRequest> allRequests = requestRepository.findAll();
            if (allRequests == null) allRequests = new ArrayList<>();

            List<AssetRequest> pendingRequests = allRequests.stream()
                    .filter(r -> r != null && r.getStatus() != null && "PENDING".equalsIgnoreCase(r.getStatus().toString()))
                    .collect(Collectors.toList());

            model.addAttribute("assets", allAssets);
            model.addAttribute("asset", asset); 
            model.addAttribute("pendingRequests", pendingRequests); 
            model.addAttribute("totalAssets", allAssets.size());
            
            long assigned = allAssets.stream()
                    .filter(a -> a != null && a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                    .count();
                    
            model.addAttribute("assignedCount", assigned);
            model.addAttribute("pendingCount", pendingRequests.size());
            model.addAttribute("openInventory", openFleet);
        } catch (Exception e) {
            // Fallback so the page doesn't crash
            model.addAttribute("assets", new ArrayList<>());
            model.addAttribute("asset", new Asset());
            model.addAttribute("totalAssets", 0);
            model.addAttribute("assignedCount", 0);
            model.addAttribute("pendingCount", 0);
        }
        
        return "dashboard";
    }
}

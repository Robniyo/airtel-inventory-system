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
        
        // Pass a new blank Asset for the "Register New" form
        return prepareDashboardView(model, new Asset(), false, session);
    }

    @GetMapping("/dashboard/edit/{id}")
    public String editAsset(@PathVariable Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        if (currentUser == null || !"ADMIN".equals(role)) {
            return "redirect:/dashboard";
        }

        // Find asset to edit. If not found, return a blank one to avoid null crashes
        Asset assetToEdit = assetRepository.findById(id).orElse(new Asset());
        
        // openFleet = true helps the JS switch to the Fleet tab automatically
        return prepareDashboardView(model, assetToEdit, true, session);
    }

    private String prepareDashboardView(Model model, Asset asset, boolean openFleet, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        List<Asset> allAssets = assetRepository.findAll();
        List<AssetRequest> allRequests = requestRepository.findAll();

        List<AssetRequest> pendingRequests = allRequests.stream()
                .filter(r -> r.getStatus() != null && "PENDING".equalsIgnoreCase(r.getStatus().toString()))
                .collect(Collectors.toList());

        model.addAttribute("assets", allAssets);
        model.addAttribute("asset", asset); // Crucial for th:object="${asset}"
        model.addAttribute("pendingRequests", pendingRequests); 
        
        model.addAttribute("totalAssets", allAssets.size());
        model.addAttribute("assignedCount", allAssets.stream()
                .filter(a -> a.getStatus() == Asset.Status.ASSIGNED).count());
        model.addAttribute("pendingCount", pendingRequests.size());
        
        model.addAttribute("openInventory", openFleet);
        
        return "dashboard";
    }
}

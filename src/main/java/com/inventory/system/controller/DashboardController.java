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

@Controller
public class DashboardController {

    @Autowired
    private AssetRepository assetRepository;
    
    @Autowired
    private AssetRequestRepository requestRepository;

    private static final String ADMIN_EMAIL = "adminAirtel@gmail.com";

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        return prepareDashboardView(model, new Asset(), false, currentUser);
    }

    @GetMapping("/dashboard/edit/{id}")
    public String editAsset(@PathVariable Long id, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !ADMIN_EMAIL.equalsIgnoreCase(currentUser.getEmail())) {
            return "redirect:/dashboard";
        }
        Asset assetToEdit = assetRepository.findById(id).orElse(new Asset());
        return prepareDashboardView(model, assetToEdit, true, currentUser);
    }

    private String prepareDashboardView(Model model, Asset asset, boolean openFleet, User user) {
        List<Asset> allAssets = assetRepository.findAll();
        
        // FETCH DATA FROM DATABASE
        List<AssetRequest> requests = requestRepository.findAll();

        model.addAttribute("assets", allAssets);
        model.addAttribute("asset", asset);
        
        // THIS KEY MUST MATCH THE HTML th:each
        model.addAttribute("pendingRequests", requests); 
        
        model.addAttribute("totalAssets", allAssets.size());
        model.addAttribute("assignedCount", allAssets.stream().filter(a -> a.getStatus() == Asset.Status.ASSIGNED).count());
        model.addAttribute("pendingCount", requests.size());
        
        model.addAttribute("isAdmin", ADMIN_EMAIL.equalsIgnoreCase(user.getEmail()));
        model.addAttribute("openInventory", openFleet);
        
        return "dashboard";
    }
}
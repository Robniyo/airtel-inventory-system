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
import java.util.List;

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

        // Safety: If session is lost, go back to login
        if (user == null || role == null) {
            return "redirect:/login";
        }

        // Shared data for both dashboards
        List<Asset> allAssets = assetRepository.findAll();
        model.addAttribute("user", user);
        model.addAttribute("assets", allAssets != null ? allAssets : new ArrayList<>());
        model.addAttribute("totalAssets", allAssets != null ? allAssets.size() : 0);

        // Separate Logic for Admin vs Staff
        if ("ADMIN".equals(role)) {
            model.addAttribute("asset", new Asset()); // For the 'Add Asset' form
            model.addAttribute("pendingRequests", requestRepository.findAll());
            return "admin_dashboard"; // This serves admin_dashboard.html
        } else {
            return "staff_dashboard"; // This serves staff_dashboard.html
        }
    }
}

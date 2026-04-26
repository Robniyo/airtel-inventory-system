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

        // 1. Safety check: Redirect if no user in session
        if (user == null || role == null) {
            return "redirect:/login";
        }

        // 2. Fetch data safely
        List<Asset> assets = assetRepository.findAll();
        if (assets == null) assets = new ArrayList<>();

        // 3. Add shared attributes
        model.addAttribute("user", user);
        model.addAttribute("assets", assets);
        model.addAttribute("totalAssets", assets.size());

        // 4. Role-based logic
        if ("ADMIN".equalsIgnoreCase(role)) {
            model.addAttribute("asset", new Asset()); 
            var requests = requestRepository.findAll();
            model.addAttribute("pendingRequests", requests != null ? requests : new ArrayList<>());
            return "admin_dashboard"; // Refers to admin_dashboard.html
        } else {
            return "staff_dashboard"; // Refers to staff_dashboard.html
        }
    }
}

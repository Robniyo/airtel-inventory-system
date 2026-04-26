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
        // Get the role from session
        Object roleObj = session.getAttribute("role");

        // IF NO USER IN SESSION, KICK TO LOGIN
        if (currentUser == null || roleObj == null) {
            return "redirect:/login";
        }

        String role = roleObj.toString();
        
        // COMMON DATA
        List<Asset> allAssets = assetRepository.findAll();
        model.addAttribute("assets", allAssets);
        model.addAttribute("user", currentUser);
        model.addAttribute("role", role); // CRITICAL: This is what HTML looks for
        model.addAttribute("totalAssets", allAssets.size());
        model.addAttribute("asset", new Asset());

        long assigned = allAssets.stream()
                .filter(a -> a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                .count();
        model.addAttribute("assignedCount", assigned);

        // ADMIN SPECIFIC DATA
        if ("ADMIN".equals(role)) {
            List<AssetRequest> pending = requestRepository.findAll().stream()
                .filter(r -> r.getStatus() != null && "PENDING".equalsIgnoreCase(r.getStatus().toString()))
                .collect(Collectors.toList());
            model.addAttribute("pendingRequests", pending);
        }

        return "dashboard";
    }
}

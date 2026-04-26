package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private AssetRepository assetRepository;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        // Only allow entry if the static login was successful
        if (session.getAttribute("role") == null) {
            return "redirect:/login";
        }

        model.addAttribute("assets", assetRepository.findAll());
        model.addAttribute("newAsset", new Asset()); // For the 'Add' form
        return "admin_dashboard";
    }
}

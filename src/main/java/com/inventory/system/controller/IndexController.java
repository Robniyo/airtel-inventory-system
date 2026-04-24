package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private AssetRepository assetRepository;

    // ================= HOME PAGE =================
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        
        // If a user is logged in, we fetch the counts to display on the landing page cards
        if (session.getAttribute("user") != null) {
            List<Asset> allAssets = assetRepository.findAll();
            
            long totalAssets = allAssets.size();
            long assignedCount = allAssets.stream()
                    .filter(a -> a.getStatus() != null && a.getStatus() == Asset.Status.ASSIGNED)
                    .count();

            model.addAttribute("totalAssets", totalAssets);
            model.addAttribute("assignedCount", assignedCount);
        } else {
            // Default values for guests (logged out users)
            model.addAttribute("totalAssets", 0);
            model.addAttribute("assignedCount", 0);
        }

        return "index"; // Points to templates/index.html
    }
}
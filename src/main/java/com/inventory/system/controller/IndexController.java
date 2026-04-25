package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class IndexController {

    // (required = false) prevents the app from crashing during startup if DB is down
    @Autowired(required = false)
    private AssetRepository assetRepository;

    @GetMapping("/")
    public String index(Model model) {
        try {
            // Check if repository exists and can fetch data
            if (assetRepository != null) {
                List<Asset> allAssets = assetRepository.findAll();
                
                int total = (allAssets != null) ? allAssets.size() : 0;
                model.addAttribute("totalAssets", total);

                long assigned = (allAssets == null) ? 0 : allAssets.stream()
                    .filter(a -> a != null && a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                    .count();
                model.addAttribute("assignedCount", assigned);
            } else {
                // Repository is null (DB connection issue)
                model.addAttribute("totalAssets", 0);
                model.addAttribute("assignedCount", 0);
            }
        } catch (Exception e) {
            // Log the error for you to see in Render logs, but keep the page alive
            System.err.println("Index Landing Error: " + e.getMessage());
            model.addAttribute("totalAssets", 0);
            model.addAttribute("assignedCount", 0);
        }
        
        // This MUST match your file name: src/main/resources/templates/index.html
        return "index"; 
    }
}

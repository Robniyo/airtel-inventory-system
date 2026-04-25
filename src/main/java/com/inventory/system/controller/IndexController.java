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

    @Autowired
    private AssetRepository assetRepository;

    @GetMapping("/")
    public String index(Model model) {
        try {
            List<Asset> allAssets = assetRepository.findAll();
            model.addAttribute("totalAssets", allAssets != null ? allAssets.size() : 0);
            
            // Safe count: prevents 500 error if status is null
            long assigned = (allAssets == null) ? 0 : allAssets.stream()
                .filter(a -> a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                .count();
                
            model.addAttribute("assignedCount", assigned);
        } catch (Exception e) {
            // If DB fails, we still show the page with 0 counts instead of a 500 error
            model.addAttribute("totalAssets", 0);
            model.addAttribute("assignedCount", 0);
        }
        return "index"; 
    }
}

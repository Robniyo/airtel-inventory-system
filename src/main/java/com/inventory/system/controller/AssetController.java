package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @PostMapping("/admin/assets/save")
    public String saveAsset(@ModelAttribute Asset asset, HttpSession session) {
        String role = (String) session.getAttribute("role");
        
        // Use equalsIgnoreCase for safety
        if ("ADMIN".equalsIgnoreCase(role)) {
            // Force status to AVAILABLE if it's a new asset
            if (asset.getId() == null || asset.getStatus() == null) {
                asset.setStatus(Asset.Status.AVAILABLE);
            }
            assetRepository.save(asset);
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/admin/assets/delete/{id}")
    public String deleteAsset(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("role");
        
        if ("ADMIN".equalsIgnoreCase(role)) {
            try {
                assetRepository.deleteById(id);
            } catch (Exception e) {
                // If it fails (e.g., asset is linked to a request), just ignore and redirect
                System.out.println("Delete failed: " + e.getMessage());
            }
        }
        return "redirect:/dashboard";
    }
}

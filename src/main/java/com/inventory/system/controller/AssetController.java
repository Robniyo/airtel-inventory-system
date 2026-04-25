package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpSession;

@Controller
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    // FIX: This method handles the "Register New Resource" AND "Update"
    // URL: http://localhost:8080/admin/assets/save
    @PostMapping("/admin/assets/save")
    public String saveAsset(@ModelAttribute("asset") Asset asset, HttpSession session) {
        String role = (String) session.getAttribute("role");
        
        // Security check: Only Admin can save/update
        if ("ADMIN".equals(role)) {
            // If it's a new asset, set default status
            if (asset.getId() == null) {
                asset.setStatus(Asset.Status.AVAILABLE);
            }
            assetRepository.save(asset);
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/admin/assets/delete/{id}")
    public String deleteAsset(@PathVariable Long id, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if ("ADMIN".equals(role)) {
            assetRepository.deleteById(id);
        }
        return "redirect:/dashboard";
    }
}

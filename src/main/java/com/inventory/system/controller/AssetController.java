package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/assets")
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @PostMapping("/save")
    public String saveAsset(@ModelAttribute("asset") Asset asset) {
        // Business Logic: New assets are always AVAILABLE by default
        if (asset.getId() == null) {
            asset.setStatus(Asset.Status.AVAILABLE);
        }
        
        assetRepository.save(asset);
        
        // Redirect back to dashboard and tell the JS to keep the Fleet tab open
        return "redirect:/dashboard?tab=fleet&success=true";
    }

    @GetMapping("/delete/{id}")
    public String deleteAsset(@PathVariable Long id) {
        assetRepository.deleteById(id);
        return "redirect:/dashboard?tab=fleet&deleted=true";
    }
}
package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @PostMapping("/admin/assets/save")
    public String saveAsset(@ModelAttribute("newAsset") Asset asset) {
        if (asset.getStatus() == null) {
            asset.setStatus(Asset.Status.AVAILABLE);
        }
        assetRepository.save(asset);
        return "redirect:/dashboard";
    }

    @GetMapping("/admin/assets/delete/{id}")
    public String deleteAsset(@PathVariable Long id) {
        assetRepository.deleteById(id);
        return "redirect:/dashboard";
    }
}

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
    public String saveAsset(@ModelAttribute("asset") Asset asset, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if ("ADMIN".equals(role)) {
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

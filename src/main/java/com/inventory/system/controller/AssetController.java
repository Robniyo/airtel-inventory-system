package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.entity.User;
import com.inventory.system.repository.AssetRepository;
import com.inventory.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;

    // SAVE NEW ASSET
    @PostMapping("/admin/assets/save")
    public String saveAsset(@ModelAttribute Asset asset, HttpSession session) {
        if (session.getAttribute("role") == null) return "redirect:/login";
        
        if (asset.getStatus() == null) {
            asset.setStatus(Asset.Status.AVAILABLE);
        }
        assetRepository.save(asset);
        return "redirect:/dashboard";
    }

    // DELETE ASSET
    @GetMapping("/admin/assets/delete/{id}")
    public String deleteAsset(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("role") == null) return "redirect:/login";
        
        assetRepository.deleteById(id);
        return "redirect:/dashboard";
    }

    // ASSIGN ASSET TO STAFF
    @PostMapping("/admin/assets/assign")
    public String assignAsset(@RequestParam Long assetId, @RequestParam Long userId, HttpSession session) {
        if (session.getAttribute("role") == null) return "redirect:/login";

        Asset asset = assetRepository.findById(assetId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (asset != null && user != null) {
            asset.setAssignedUser(user);
            asset.setStatus(Asset.Status.ASSIGNED);
            assetRepository.save(asset);
        }
        return "redirect:/dashboard";
    }

    // EDIT ASSET (GETS DATA FOR THE UPDATE FORM)
    @GetMapping("/admin/assets/edit/{id}")
    public String editAssetForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("role") == null) return "redirect:/login";
        
        Asset asset = assetRepository.findById(id).orElse(null);
        if (asset != null) {
            model.addAttribute("assetToUpdate", asset);
            // Re-load dashboard data so the page doesn't break
            model.addAttribute("assets", assetRepository.findAll());
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("newAsset", new Asset()); 
            model.addAttribute("newUser", new User());
            return "admin_dashboard"; 
        }
        return "redirect:/dashboard";
    }

    // UPDATE ASSET (POST UPDATED DATA)
    @PostMapping("/admin/assets/update")
    public String updateAsset(@ModelAttribute Asset asset, HttpSession session) {
        if (session.getAttribute("role") == null) return "redirect:/login";
        
        // Ensure status is preserved if not in form
        if (asset.getStatus() == null) asset.setStatus(Asset.Status.AVAILABLE);
        
        assetRepository.save(asset);
        return "redirect:/dashboard";
    }
}

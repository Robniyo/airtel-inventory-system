package com.inventory.system.controller;

import com.inventory.system.entity.Asset;
import com.inventory.system.entity.AssetRequest;
import com.inventory.system.entity.User;
import com.inventory.system.repository.AssetRepository;
import com.inventory.system.repository.AssetRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
public class RequestController {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private AssetRequestRepository requestRepository;

    // --- STAFF: Create Request ---
    @GetMapping("/assets/request/{id}")
    public String createRequest(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Asset asset = assetRepository.findById(id).orElse(null);
        
        if (asset != null && asset.getStatus() == Asset.Status.AVAILABLE) {
            AssetRequest req = new AssetRequest();
            req.setAsset(asset);
            req.setUser(user);
            req.setRequestDate(LocalDateTime.now());
            req.setStatus("PENDING");
            
            requestRepository.save(req);
        }
        return "redirect:/dashboard?requested=true";
    }

    // --- ADMIN: Approve Request ---
    @GetMapping("/admin/requests/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        AssetRequest req = requestRepository.findById(id).orElse(null);
        if (req != null) {
            Asset asset = req.getAsset();
            asset.setStatus(Asset.Status.ASSIGNED);
            asset.setAssignedUser(req.getUser());
            assetRepository.save(asset);
            
            requestRepository.delete(req); 
        }
        return "redirect:/dashboard?tab=requests";
    }

    // --- ADMIN: Reject Request ---
    @GetMapping("/admin/requests/reject/{id}")
    public String rejectRequest(@PathVariable Long id) {
        AssetRequest req = requestRepository.findById(id).orElse(null);
        if (req != null) {
            // Simply delete the request. Asset remains AVAILABLE.
            requestRepository.delete(req);
        }
        return "redirect:/dashboard?tab=requests";
    }
}

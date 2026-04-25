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

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        List<Asset> allAssets = assetRepository.findAll();
        model.addAttribute("totalAssets", allAssets.size());
        model.addAttribute("assignedCount", allAssets.stream()
                .filter(a -> a.getStatus() != null && "ASSIGNED".equalsIgnoreCase(a.getStatus().toString()))
                .count());
        return "index"; 
    }
}

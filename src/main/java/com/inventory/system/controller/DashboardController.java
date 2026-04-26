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
public class DashboardController {

    @Autowired
    private AssetRepository assetRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        if (session.getAttribute("role") == null) return "redirect:/login";

        model.addAttribute("assets", assetRepo.findAll());
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("newAsset", new Asset());
        model.addAttribute("newUser", new User());
        return "admin_dashboard";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(@ModelAttribute User user, HttpSession session) {
        if (session.getAttribute("role") == null) return "redirect:/login";
        
        userRepo.save(user);
        return "redirect:/dashboard";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("role") == null) return "redirect:/login";
        
        userRepo.deleteById(id);
        return "redirect:/dashboard";
    }
}

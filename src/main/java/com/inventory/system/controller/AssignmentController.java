package com.inventory.system.controller;

import com.inventory.system.entity.*;
import com.inventory.system.repository.*;
import com.inventory.system.service.AssignmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;

    // ================= VIEW PAGE =================
    @GetMapping
    public String viewAssignments(Model model, HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        model.addAttribute("assignments", assignmentService.getAllAssignments());
        model.addAttribute("assets", assetRepository.findAll());
        model.addAttribute("users", userRepository.findAll());

        return "assignments";
    }

    // ================= ASSIGN =================
    @PostMapping("/assign")
    public String assign(@RequestParam Long assetId,
                         @RequestParam Long userId,
                         HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        assignmentService.assignAsset(assetId, userId);
        return "redirect:/assignments";
    }

    // ================= RETURN =================
    @GetMapping("/return/{id}")
    public String returnAsset(@PathVariable Long id,
                             HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        assignmentService.returnAsset(id);
        return "redirect:/assignments";
    }
}
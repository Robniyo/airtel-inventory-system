package com.inventory.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        // This ensures that hitting the main URL takes you straight to login
        // No need for an index.html file!
        return "redirect:/login";
    }
}

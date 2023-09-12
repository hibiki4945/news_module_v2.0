package com.example.news_module.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    
//    @Autowired
//    private OnlyTestService onlyTestService;

    
    
    @RequestMapping(value = "/home")
    public String getHome(@RequestParam(name = "name", required = false, defaultValue = "home") String name, Model model) {
//        Campaign campaign = new Campaign();
//        model.addAttribute("campaign", campaign);
//        model.addAttribute("error", "");
        return "home";
    }

    @PostMapping("/home")
    public String PostHome(Model model) {
//        Campaign campaign = new Campaign();
//        model.addAttribute("campaign", campaign);
//        model.addAttribute("error", "");
//        onlyTestService.add();
        return "home";
    }

}

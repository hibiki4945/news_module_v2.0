package com.example.news_module.controller;

import com.example.news_module.entity.News;
import com.example.news_module.service.ifs.MainService;
import com.example.news_module.vo.NewsAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    
    @Autowired
    private MainService mainService;
    
    
    @RequestMapping(value = "/client_home")
    public String clientHome(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "client_home";
    }
    
    @RequestMapping(value = "/client_content")
    public String clientContent(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "client_content";
    }
    
    @RequestMapping(value = "/home")
    public String Home(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "home";
    }
    
    @RequestMapping(value = "/content")
    public String Content(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "content";
    }
    
    @RequestMapping(value = "/news_add")
    public String NewsAdd(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        News news = new News();
        model.addAttribute("news", news);
        model.addAttribute("error", "");
        
        return "news_add";
    }

    @PostMapping("/news_add")
    public String NewsAdd(@ModelAttribute("news") News news, Model model) {
//        System.out.println("123");
        System.out.println(news);
        NewsAddResponse res = mainService.newsAdd(news);
        if(res.getCode() != "200") {
            System.out.println(res.getMessage());
            model.addAttribute("error", res.getMessage());
            return "news_add";
        }
        return "news_preview";
    }
    
    @RequestMapping(value = "/news_edit")
    public String NewsEdit(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "news_edit";
    }
    
    @RequestMapping(value = "/news_preview")
    public String NewsPreview(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "news_preview";
    }
    
    @RequestMapping(value = "/category_home")
    public String categoryHome(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "category_home";
    }
    
    @RequestMapping(value = "/category_add")
    public String categoryAdd(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "category_add";
    }
    
    @RequestMapping(value = "/category_edit")
    public String categoryEdit(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "category_edit";
    }
    
    @RequestMapping(value = "/sub_category_home")
    public String subCategoryHome(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "sub_category_home";
    }
    
    @RequestMapping(value = "/sub_category_add")
    public String subCategoryAdd(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "sub_category_add";
    }
    
    @RequestMapping(value = "/sub_category_edit")
    public String subCategoryEdit(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "sub_category_edit";
    }
    
}

package com.example.news_module.controller;

import com.example.news_module.entity.Category;
import com.example.news_module.entity.News;
import com.example.news_module.entity.SubCategory;
import com.example.news_module.repository.CategoryDao;
import com.example.news_module.repository.NewsDao;
import com.example.news_module.repository.SubCategoryDao;
import com.example.news_module.service.ifs.MainService;
import com.example.news_module.vo.CategoryAddResponse;
import com.example.news_module.vo.NewsAddResponse;
import com.example.news_module.vo.SubCategoryAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    
//  newsAddCheck的回傳結果的暫存資料
    private NewsAddResponse newsAddCheckData;
    
    private String newsAddCategorySelect;
    
    @Autowired
    private MainService mainService;

    @Autowired
    private NewsDao newsDao;
    
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private SubCategoryDao subCateogryDao;
    
    
    @RequestMapping(value = "/client_home")
    public String clientHome(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "client_home";
    }
    
    @RequestMapping(value = "/client_content")
    public String clientContent(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "client_content";
    }
    
    @RequestMapping(value = "/home")
    public String Home(@RequestParam(name = "mode", required = false, defaultValue = "0") String mode, Model model) {


            News news01 = new News();
            News news02 = new News();
            News news03 = new News();
            News news04 = new News();
            News news05 = new News();
            News news06 = new News();
        
            newsAddCategorySelect = news01.getCategory();
            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
            List<String> subCategoryList = new ArrayList<>();
            for (SubCategory item : res01) {
//                System.out.println(item.getSubCategory());
                subCategoryList.add(item.getSubCategory());
            }
            model.addAttribute("subCategoryList", subCategoryList);

            List<Category> res02 = categoryDao.findAll();
            List<String> categoryList = new ArrayList<>();
            for (Category item : res02) {
//                System.out.println(item.getCategory());
                categoryList.add(item.getCategory());
            }
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("news01", news01);
            model.addAttribute("news02", news02);
            model.addAttribute("news03", news03);
            model.addAttribute("news04", news04);
            model.addAttribute("news05", news05);
            model.addAttribute("news06", news06);
            model.addAttribute("res", new ArrayList<News>());
            
            
        return "home";
    }
    
//    @PostMapping("/home")
//    public String Home(@ModelAttribute("news") News news, Model model) {
//
//        System.out.println(news);
//
//            newsAddCategorySelect = news.getCategory();
//            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
//            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
//            List<String> subCategoryList = new ArrayList<>();
//            for (SubCategory item : res01) {
//                subCategoryList.add(item.getSubCategory());
//            }
//            model.addAttribute("subCategoryList", subCategoryList);
//
//            List<Category> res02 = categoryDao.findAll();
//            List<String> categoryList = new ArrayList<>();
//            for (Category item : res02) {
//                categoryList.add(item.getCategory());
//            }
//            model.addAttribute("categoryList", categoryList);
//
//            model.addAttribute("news", news);
//            
//            return "home";
////        }
//    }
    
    @PostMapping("/home_search_category")
    public String HomeSearchCategory(@ModelAttribute("news01") News news, Model model) {

        System.out.println(news);

            newsAddCategorySelect = news.getCategory();
            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
            List<String> subCategoryList = new ArrayList<>();
            for (SubCategory item : res01) {
                subCategoryList.add(item.getSubCategory());
            }
            model.addAttribute("subCategoryList", subCategoryList);

            List<Category> res02 = categoryDao.findAll();
            List<String> categoryList = new ArrayList<>();
            for (Category item : res02) {
                categoryList.add(item.getCategory());
            }
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("news01", news);

            News news02 = new News();
            News news03 = new News();
            News news04 = new News();
            News news05 = new News();
            News news06 = new News();
            
            model.addAttribute("news02", news02);
            model.addAttribute("news03", news03);
            model.addAttribute("news04", news04);
            model.addAttribute("news05", news05);
            model.addAttribute("news06", news06);
            
            List<News> res = newsDao.findByCategory(news.getCategory());
            
            model.addAttribute("res", res);
            
            return "home";
//        }
    }
    
    @PostMapping("/home_search_sub_category")
    public String HomeSearchSubCategory(@ModelAttribute("news02") News news, Model model) {

        System.out.println(news);

//            newsAddCategorySelect = news.getCategory();
//            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
            List<String> subCategoryList = new ArrayList<>();
            for (SubCategory item : res01) {
                subCategoryList.add(item.getSubCategory());
            }
            model.addAttribute("subCategoryList", subCategoryList);

            List<Category> res02 = categoryDao.findAll();
            List<String> categoryList = new ArrayList<>();
            for (Category item : res02) {
                categoryList.add(item.getCategory());
            }
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("news02", news);

            News news01 = new News();
            News news03 = new News();
            News news04 = new News();
            News news05 = new News();
            News news06 = new News();
            
            news01.setCategory(newsAddCategorySelect);
            
            model.addAttribute("news01", news01);
            model.addAttribute("news03", news03);
            model.addAttribute("news04", news04);
            model.addAttribute("news05", news05);
            model.addAttribute("news06", news06);
            
            List<News> res = newsDao.findBySubCategory(news.getSubCategory());
            
            model.addAttribute("res", res);
            
            return "home";
//        }
    }
    
    @PostMapping("/home_search_news_title")
    public String HomeSearchNewsTitle(@ModelAttribute("news03") News news, Model model) {

        System.out.println(news);

//            newsAddCategorySelect = news.getCategory();
//            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
            List<String> subCategoryList = new ArrayList<>();
            for (SubCategory item : res01) {
                subCategoryList.add(item.getSubCategory());
            }
            model.addAttribute("subCategoryList", subCategoryList);

            List<Category> res02 = categoryDao.findAll();
            List<String> categoryList = new ArrayList<>();
            for (Category item : res02) {
                categoryList.add(item.getCategory());
            }
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("news03", news);

            News news01 = new News();
            News news02 = new News();
            News news04 = new News();
            News news05 = new News();
            News news06 = new News();
            
            news01.setCategory(newsAddCategorySelect);
            
            model.addAttribute("news01", news01);
            model.addAttribute("news02", news02);
            model.addAttribute("news04", news04);
            model.addAttribute("news05", news05);
            model.addAttribute("news06", news06);
            
            List<News> res = newsDao.findByNewsTitle(news.getNewsTitle());
            
            model.addAttribute("res", res);
            
            return "home";
//        }
    }
    
    @PostMapping("/home_search_news_sub_title")
    public String HomeSearchNewsSubTitle(@ModelAttribute("news04") News news, Model model) {

        System.out.println(news);

//            newsAddCategorySelect = news.getCategory();
//            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
            List<String> subCategoryList = new ArrayList<>();
            for (SubCategory item : res01) {
                subCategoryList.add(item.getSubCategory());
            }
            model.addAttribute("subCategoryList", subCategoryList);

            List<Category> res02 = categoryDao.findAll();
            List<String> categoryList = new ArrayList<>();
            for (Category item : res02) {
                categoryList.add(item.getCategory());
            }
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("news04", news);

            News news01 = new News();
            News news02 = new News();
            News news03 = new News();
            News news05 = new News();
            News news06 = new News();
            
            news01.setCategory(newsAddCategorySelect);
            
            model.addAttribute("news01", news01);
            model.addAttribute("news02", news02);
            model.addAttribute("news03", news03);
            model.addAttribute("news05", news05);
            model.addAttribute("news06", news06);
            
            List<News> res = newsDao.findByNewsSubTitle(news.getNewsSubTitle());
            
            model.addAttribute("res", res);
            
            return "home";
//        }
    }
    
    @PostMapping("/home_search_start_time")
    public String HomeSearchStartTime(@ModelAttribute("news05") News news, Model model) {

        System.out.println(news);

//            newsAddCategorySelect = news.getCategory();
//            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
            List<String> subCategoryList = new ArrayList<>();
            for (SubCategory item : res01) {
                subCategoryList.add(item.getSubCategory());
            }
            model.addAttribute("subCategoryList", subCategoryList);

            List<Category> res02 = categoryDao.findAll();
            List<String> categoryList = new ArrayList<>();
            for (Category item : res02) {
                categoryList.add(item.getCategory());
            }
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("news05", news);

            News news01 = new News();
            News news02 = new News();
            News news03 = new News();
            News news04 = new News();
            News news06 = new News();
            
            news01.setCategory(newsAddCategorySelect);
            
            model.addAttribute("news01", news01);
            model.addAttribute("news02", news02);
            model.addAttribute("news03", news03);
            model.addAttribute("news04", news04);
            model.addAttribute("news06", news06);
            
            List<News> res = newsDao.findByReleaseTimeGreaterThanEqual(news.getReleaseTime());
            
//            List<News> res = new ArrayList<>();
            
            model.addAttribute("res", res);
            
            return "home";
//        }
    }
    
    @PostMapping("/home_search_end_time")
    public String HomeSearchEndTime(@ModelAttribute("news06") News news, Model model) {

        System.out.println(news);

//            newsAddCategorySelect = news.getCategory();
//            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
            List<String> subCategoryList = new ArrayList<>();
            for (SubCategory item : res01) {
                subCategoryList.add(item.getSubCategory());
            }
            model.addAttribute("subCategoryList", subCategoryList);

            List<Category> res02 = categoryDao.findAll();
            List<String> categoryList = new ArrayList<>();
            for (Category item : res02) {
                categoryList.add(item.getCategory());
            }
            model.addAttribute("categoryList", categoryList);

            model.addAttribute("news06", news);

            News news01 = new News();
            News news02 = new News();
            News news03 = new News();
            News news04 = new News();
            News news05 = new News();
            
            news01.setCategory(newsAddCategorySelect);
            
            model.addAttribute("news01", news01);
            model.addAttribute("news02", news02);
            model.addAttribute("news03", news03);
            model.addAttribute("news04", news04);
            model.addAttribute("news05", news05);
            
            List<News> res = newsDao.findByReleaseTimeLessThanEqual(news.getReleaseTime());
            
//            List<News> res = new ArrayList<>();
            
            model.addAttribute("res", res);
            
            return "home";
//        }
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

        List<Category> res = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res) {
//            System.out.println(item.getCategory());
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        newsAddCategorySelect = news.getCategory();
//        System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
        List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
        List<String> subCategoryList = new ArrayList<>();
        for (SubCategory item : res01) {
//            System.out.println(item.getSubCategory());
            subCategoryList.add(item.getSubCategory());
        }
        model.addAttribute("subCategoryList", subCategoryList);
        
        return "news_add";
    }

    @PostMapping("/news_add")
    public String NewsAdd(@ModelAttribute("news") News news, Model model) {
//        System.out.println("123");
        System.out.println(news);
        NewsAddResponse res = mainService.newsAddCheck(news);
        if(res.getCode() != "200") {

            newsAddCategorySelect = news.getCategory();
            System.out.println("newsAddCategorySelect: "+newsAddCategorySelect);
            List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
            List<String> subCategoryList = new ArrayList<>();
            for (SubCategory item : res01) {
//                System.out.println(item.getSubCategory());
                subCategoryList.add(item.getSubCategory());
            }
            model.addAttribute("subCategoryList", subCategoryList);

            List<Category> res02 = categoryDao.findAll();
            List<String> categoryList = new ArrayList<>();
            for (Category item : res02) {
//                System.out.println(item.getCategory());
                categoryList.add(item.getCategory());
            }
            model.addAttribute("categoryList", categoryList);

//            news.setCategory(newsAddCategorySelect);
//            model.addAttribute("news", news);
            
            System.out.println(res.getMessage());
            model.addAttribute("error", res.getMessage());
            return "news_add";
        }
        newsAddCheckData = res;
        return "news_preview";
    }
    
    @RequestMapping(value = "/news_edit")
    public String NewsEdit(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "news_edit";
    }
    
//    @RequestMapping(value = "/news_preview")
//    public String NewsPreview(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {
//        
//        return "news_preview";
//    }

    @PostMapping("/news_preview")
    public String NewsPreview(Model model) {
//        System.out.println("123");
//        System.out.println(news);
//        NewsAddResponse res = mainService.newsAddCheck(news);
        if(newsAddCheckData.getCode() != "200") {
            System.out.println(newsAddCheckData.getMessage());
            model.addAttribute("error", newsAddCheckData.getMessage());
            return "news_preview";
        }

        newsAddCheckData = mainService.newsAdd(newsAddCheckData);

        if(newsAddCheckData.getCode() != "200") {
            System.out.println(newsAddCheckData.getMessage());
            model.addAttribute("error", newsAddCheckData.getMessage());
            return "news_preview";
        }

        newsAddCheckData = null;
        return "home";
    }
    
    @RequestMapping(value = "/category_home")
    public String categoryHome(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "category_home";
    }
    
    @RequestMapping(value = "/category_add")
    public String categoryAdd(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        Category category = new Category();
        model.addAttribute("category", category);
        model.addAttribute("error", "");
        
        return "category_add";
        
    }

    @PostMapping("/category_add")
    public String CategoryAdd(@ModelAttribute("category") Category category, Model model) {
//        System.out.println("123");
        System.out.println(category);
        CategoryAddResponse res = mainService.categoryAdd(category);
        if(res.getCode() != "200") {
            System.out.println(res.getMessage());
            model.addAttribute("error", res.getMessage());
            return "category_add";
        }
        return "category_home";
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

        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("error", "");
        
        List<Category> res = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res) {
//            System.out.println(item.getCategory());
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        
        return "sub_category_add";
    }

    @PostMapping("/sub_category_add")
    public String subCategoryAdd(@ModelAttribute("subCategory") SubCategory subCategory, Model model) {
//        System.out.println("123");
        System.out.println(subCategory);
        SubCategoryAddResponse res = mainService.subCategoryAdd(subCategory);
        if(res.getCode() != "200") {
            System.out.println(res.getMessage());
            model.addAttribute("error", res.getMessage());
            return "sub_category_add";
        }
        return "sub_category_home";
    }
    
    @RequestMapping(value = "/sub_category_edit")
    public String subCategoryEdit(@RequestParam(name = "name", required = false, defaultValue = "World0") String name, Model model) {

        return "sub_category_edit";
    }
    
}

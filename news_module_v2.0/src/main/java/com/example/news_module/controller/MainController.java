package com.example.news_module.controller;

import com.example.news_module.entity.Category;
import com.example.news_module.entity.News;
import com.example.news_module.entity.SubCategory;
import com.example.news_module.repository.CategoryDao;
import com.example.news_module.repository.NewsDao;
import com.example.news_module.repository.SubCategoryDao;
import com.example.news_module.service.ifs.MainService;
import com.example.news_module.vo.CategoryAddResponse;
import com.example.news_module.vo.CheckedRes;
import com.example.news_module.vo.MultipleSearch;
import com.example.news_module.vo.NewsAddResponse;
import com.example.news_module.vo.SubCategoryAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    
//  newsAddCheck的回傳結果的暫存資料
    private NewsAddResponse newsAddCheckData;
//  新增新聞時的分類選擇
    private String newsAddCategorySelect;
//  新聞的搜尋結果的暫存
    private List<News> resTemp = new ArrayList<News>();
    ////////////////////////////////////////////////
//  最近一次的新聞搜尋類別
    private int lastSearch = 0;
//  最近一次的新聞搜尋關鍵字
    private String lastKeyWordStr = "";
//  最近一次的新聞搜尋關鍵字(複合搜尋)
    private MultipleSearch lastKeyWordMultipleStr = new MultipleSearch();
//  升冪&降冪的設定
    private boolean sortDescFlag = false;
//  最近一次的新聞搜尋結果
    private List<News> NewsListTemp = null;
//  最近一次的勾選結果
    private CheckedRes checkedResTemp = null;
//  最近一次的勾選結果的總勾選數
    private int checkedResTempCount = 0;
//  最近一次的勾選結果的新聞id
    private int idTemp = 0;
    ////////////////////////////////////////////////
//  最近一次的分類搜尋結果
    private List<Category> CategoryListTemp = null;
//  最近一次的分類搜尋結果的分類id
    private int categoryIdTemp = 0;
    ////////////////////////////////////////////////
//  最近一次的子分類搜尋類別
    private int subCategoryLastSearch = 0;
//  最近一次的子分類搜尋關鍵字
    private String subCategorylastKeyWordStr = "";
//  最近一次的子分類搜尋結果
    private List<SubCategory> SubCategoryListTemp = null;
//  最近一次的子分類搜尋結果的子分類id
    private int subCategoryIdTemp = 0;
    ////////////////////////////////////////////////
//  每頁的頁數設定
    private int pageSize = 10;
//  目前頁數的暫存
    private int pageNumTemp = 0;
    ////////////////////////////////////////////////
//  更新前的分類暫存
    private String oldCategoryTemp = "";
//  更新前的子分類暫存
    private String oldSubCategoryTemp = "";
    ////////////////////////////////////////////////
    
//  主Service
    @Autowired
    private MainService mainService;
//  新聞的Dao
    @Autowired
    private NewsDao newsDao;
//  分類的Dao
    @Autowired
    private CategoryDao categoryDao;
//  子分類的Dao
    @Autowired
    private SubCategoryDao subCateogryDao;
    
    //////////////////////////////////////////////////
//  管理端的新聞主頁
    @RequestMapping(value = "/home/{pageNum}")
    public String Home(@PathVariable(value="pageNum",required=false) int pageNum, Model model) {
//        搜尋結果為零時 防止頁數為負
          if(pageNum == -1)
              pageNum = 0;
//        將目前頁數暫存 更新到目前頁數
          pageNumTemp = pageNum;
        
//        (開始)新聞的搜尋的初始化設定
//          新建一個 新聞的分類搜尋的輸入用變數
            News news01 = new News();
//          新建一個 新聞的子分類搜尋的輸入用變數
            News news02 = new News();
//          新建一個 新聞的新聞標題搜尋的輸入用變數
            News news03 = new News();
//          新建一個 新聞的新聞副標題搜尋的輸入用變數
            News news04 = new News();
//          新建一個 新聞的開始時間搜尋的輸入用變數
            News news05 = new News();
//          新建一個 新聞的結束時間搜尋的輸入用變數
            News news06 = new News();
//          新建一個 新聞的複合搜尋的輸入用變數
            MultipleSearch news07 = new MultipleSearch();
//        (結束)新聞的搜尋的初始化設定

//        (開始)新聞的搜尋的初始值設定
//          新聞的分類搜尋
            if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
                news01.setCategory(lastKeyWordStr);
//          新聞的子分類搜尋
            if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
                news02.setSubCategory(lastKeyWordStr);  
//          新聞的新聞標題搜尋
            if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
                news03.setNewsTitle(lastKeyWordStr);  
//          新聞的新聞副標題搜尋
            if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
                news04.setNewsSubTitle(lastKeyWordStr);
//          新聞的開始時間搜尋
            if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
                news05.setReleaseTime(lastKeyWordStr);
//          新聞的結束時間搜尋
            if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
                news06.setReleaseTime(lastKeyWordStr);
//          新聞的複合搜尋 // 20231007(記得加上(暫存資料))
            if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果(測試用)
                news07 = lastKeyWordMultipleStr;
//        (結束)新聞的搜尋的初始值設定
            
//        (開始)新聞的搜尋的Thymeleaf傳入設定
//          新建一個 新聞的分類搜尋的輸入用變數
            model.addAttribute("news01", news01);
//          新建一個 新聞的子分類搜尋的輸入用變數
            model.addAttribute("news02", news02);
//          新建一個 新聞的新聞標題搜尋的輸入用變數
            model.addAttribute("news03", news03);
//          新建一個 新聞的新聞副標題搜尋的輸入用變數
            model.addAttribute("news04", news04);
//          新建一個 新聞的開始時間搜尋的輸入用變數
            model.addAttribute("news05", news05);
//          新建一個 新聞的結束時間搜尋的輸入用變數
            model.addAttribute("news06", news06);
//          新建一個 新聞的複合搜尋的輸入用變數
            model.addAttribute("news07", news07);
//        (結束)新聞的搜尋的Thymeleaf傳入設定

//        (開始)分類列表的初始化設定
//          將 新聞的分類選擇框用的List 傳到Thymeleaf
            model.addAttribute("categoryList", categoryListInitializer());
//        (結束)分類列表的初始化設定
            
//        (開始)子分類列表的初始化設定
//          將 新聞的子分類選擇框用的List 傳到Thymeleaf
            model.addAttribute("subCategoryList", subCategoryListInitializer());
//        (結束)子分類列表的初始化設定
          
//        (開始)勾選設定
//          新建一個 勾選結果儲存用的變數
            CheckedRes checkedRes = new CheckedRes();
//          將 勾選結果儲存用的變數 傳到Thymeleaf
            model.addAttribute("checkedRes", checkedRes);
//        (結束)勾選設定

//        (開始)分頁設定
//          將 每頁最大顯示結果的筆數 改為10筆
//            pageSize = 10;
//          將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
            model.addAttribute("pageNum", pageNum);
//          將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
            model.addAttribute("pageSize", pageSize);
//        (結束)分頁設定

//        (開始)新聞搜尋結果的設定
//          將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
            model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//          將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
            model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//        (結束)新聞搜尋結果的設定
            
//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
            
//      返回 管理端的新聞主頁
        return "home";
    }
//  管理端的新聞主頁(用分類搜尋)
    @PostMapping("/home_search_category")
    public String HomeSearchCategory(@ModelAttribute("news01") News news, Model model) {
//        將目前選擇的分類暫存 更新為目前選擇的分類
          newsAddCategorySelect = news.getCategory();
//        將搜尋種類的暫存 更新為'分類'
          lastSearch = 1;
//        將搜尋種類的關鍵字的暫存 更新為最新輸入的關鍵字
          lastKeyWordStr = news.getCategory();

//        (開始)新聞的搜尋的初始化設定
//          新建一個 新聞的分類搜尋的輸入用變數
            News news01 = news;
//          新建一個 新聞的子分類搜尋的輸入用變數
            News news02 = new News();
//          新建一個 新聞的新聞標題搜尋的輸入用變數
            News news03 = new News();
//          新建一個 新聞的新聞副標題搜尋的輸入用變數
            News news04 = new News();
//          新建一個 新聞的開始時間搜尋的輸入用變數
            News news05 = new News();
//          新建一個 新聞的結束時間搜尋的輸入用變數
            News news06 = new News();
//          新建一個 新聞的複合搜尋的輸入用變數
            MultipleSearch news07 = new MultipleSearch();
//        (結束)新聞的搜尋的初始化設定

//        (開始)新聞的搜尋的Thymeleaf傳入設定
//          新建一個 新聞的分類搜尋的輸入用變數
            model.addAttribute("news01", news01);
//          新建一個 新聞的子分類搜尋的輸入用變數
            model.addAttribute("news02", news02);
//          新建一個 新聞的新聞標題搜尋的輸入用變數
            model.addAttribute("news03", news03);
//          新建一個 新聞的新聞副標題搜尋的輸入用變數
            model.addAttribute("news04", news04);
//          新建一個 新聞的開始時間搜尋的輸入用變數
            model.addAttribute("news05", news05);
//          新建一個 新聞的結束時間搜尋的輸入用變數
            model.addAttribute("news06", news06);
//          新建一個 新聞的複合搜尋的輸入用變數
            model.addAttribute("news07", news07);
//        (結束)新聞的搜尋的Thymeleaf傳入設定

//        (開始)分類列表的初始化設定
//          將 新聞的分類選擇框用的List 傳到Thymeleaf
            model.addAttribute("categoryList", categoryListInitializer());
//        (結束)分類列表的初始化設定
                
//        (開始)子分類列表的初始化設定
//          將 新聞的子分類選擇框用的List 傳到Thymeleaf
            model.addAttribute("subCategoryList", subCategoryListInitializer());
//        (結束)子分類列表的初始化設定

//        (開始)勾選設定
//          新建一個 勾選結果儲存用的變數
            CheckedRes checkedRes = new CheckedRes();
//          將 勾選結果儲存用的變數 傳到Thymeleaf
            model.addAttribute("checkedRes", checkedRes);
//        (結束)勾選設定

//        (開始)分頁設定
//          將 每頁最大顯示結果的筆數 改為10筆
            int pageNum = 0;
//            int pageSize = 10;
//          將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
            model.addAttribute("pageNum", pageNum);
//          將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
            model.addAttribute("pageSize", pageSize);
//        (結束)分頁設定

//        (開始)新聞搜尋結果的設定
//          將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
            model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//          將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
            model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//        (結束)新聞搜尋結果的設定
              
//          返回 管理者的主頁
            return "home";
        
    }
//  管理端的新聞主頁(用子分類搜尋)    
    @PostMapping("/home_search_sub_category")
    public String HomeSearchSubCategory(@ModelAttribute("news02") News news, Model model) {
//          將搜尋種類的暫存 更新為'子分類'
            lastSearch = 2;
//          將搜尋種類的關鍵字的暫存 更新為最新輸入的關鍵字
            lastKeyWordStr = news.getSubCategory();

//          (開始)新聞的搜尋的初始化設定
//            新建一個 新聞的分類搜尋的輸入用變數
              News news01 = new News();
//            新建一個 新聞的子分類搜尋的輸入用變數
              News news02 = news;
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              News news03 = new News();
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              News news04 = new News();
//            新建一個 新聞的開始時間搜尋的輸入用變數
              News news05 = new News();
//            新建一個 新聞的結束時間搜尋的輸入用變數
              News news06 = new News();
//            新建一個 新聞的複合搜尋的輸入用變數
              MultipleSearch news07 = new MultipleSearch();
//          (結束)新聞的搜尋的初始化設定

//          (開始)新聞的搜尋的Thymeleaf傳入設定
//            新建一個 新聞的分類搜尋的輸入用變數
              model.addAttribute("news01", news01);
//            新建一個 新聞的子分類搜尋的輸入用變數
              model.addAttribute("news02", news02);
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              model.addAttribute("news03", news03);
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              model.addAttribute("news04", news04);
//            新建一個 新聞的開始時間搜尋的輸入用變數
              model.addAttribute("news05", news05);
//            新建一個 新聞的結束時間搜尋的輸入用變數
              model.addAttribute("news06", news06);
//            新建一個 新聞的複合搜尋的輸入用變數
              model.addAttribute("news07", news07);
//          (結束)新聞的搜尋的Thymeleaf傳入設定

//          (開始)分類列表的初始化設定
//            將 新聞的分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("categoryList", categoryListInitializer());
//          (結束)分類列表的初始化設定
                  
//          (開始)子分類列表的初始化設定
//            將 新聞的子分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("subCategoryList", subCategoryListInitializer());
//          (結束)子分類列表的初始化設定

//          (開始)勾選設定
//            新建一個 勾選結果儲存用的變數
              CheckedRes checkedRes = new CheckedRes();
//            將 勾選結果儲存用的變數 傳到Thymeleaf
              model.addAttribute("checkedRes", checkedRes);
//          (結束)勾選設定

//          (開始)分頁設定
//            將 每頁最大顯示結果的筆數 改為10筆
              int pageNum = 0;
//              int pageSize = 10;
//            將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageNum", pageNum);
//            將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageSize", pageSize);
//          (結束)分頁設定

//          (開始)新聞搜尋結果的設定
//            將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
              model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//            將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//          (結束)新聞搜尋結果的設定

//            返回 管理者的主頁
              return "home";
            
    }
//  管理端的新聞主頁(用新聞標題搜尋)
    @PostMapping("/home_search_news_title")
    public String HomeSearchNewsTitle(@ModelAttribute("news03") News news, Model model) {
//          將搜尋種類的暫存 更新為'新聞標題'
            lastSearch = 3;
//          將搜尋種類的關鍵字的暫存 更新為最新輸入的關鍵字
            lastKeyWordStr = news.getNewsTitle();

//          (開始)新聞的搜尋的初始化設定
//            新建一個 新聞的分類搜尋的輸入用變數
              News news01 = new News();
//            新建一個 新聞的子分類搜尋的輸入用變數
              News news02 = new News();
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              News news03 = news;
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              News news04 = new News();
//            新建一個 新聞的開始時間搜尋的輸入用變數
              News news05 = new News();
//            新建一個 新聞的結束時間搜尋的輸入用變數
              News news06 = new News();
//            新建一個 新聞的複合搜尋的輸入用變數
              MultipleSearch news07 = new MultipleSearch();
//          (結束)新聞的搜尋的初始化設定

//          (開始)新聞的搜尋的Thymeleaf傳入設定
//            新建一個 新聞的分類搜尋的輸入用變數
              model.addAttribute("news01", news01);
//            新建一個 新聞的子分類搜尋的輸入用變數
              model.addAttribute("news02", news02);
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              model.addAttribute("news03", news03);
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              model.addAttribute("news04", news04);
//            新建一個 新聞的開始時間搜尋的輸入用變數
              model.addAttribute("news05", news05);
//            新建一個 新聞的結束時間搜尋的輸入用變數
              model.addAttribute("news06", news06);
//            新建一個 新聞的複合搜尋的輸入用變數
              model.addAttribute("news07", news07);
//          (結束)新聞的搜尋的Thymeleaf傳入設定

//          (開始)分類列表的初始化設定
//            將 新聞的分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("categoryList", categoryListInitializer());
//          (結束)分類列表的初始化設定
                  
//          (開始)子分類列表的初始化設定
//            將 新聞的子分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("subCategoryList", subCategoryListInitializer());
//          (結束)子分類列表的初始化設定

//          (開始)勾選設定
//            新建一個 勾選結果儲存用的變數
              CheckedRes checkedRes = new CheckedRes();
//            將 勾選結果儲存用的變數 傳到Thymeleaf
              model.addAttribute("checkedRes", checkedRes);
//          (結束)勾選設定

//          (開始)分頁設定
//            將 每頁最大顯示結果的筆數 改為10筆
              int pageNum = 0;
//              int pageSize = 10;
//            將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageNum", pageNum);
//            將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageSize", pageSize);
//          (結束)分頁設定

//          (開始)新聞搜尋結果的設定
//            將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
              model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//            將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//          (結束)新聞搜尋結果的設定

//            返回 管理者的主頁
              return "home";
            
    }
//  管理端的新聞主頁(用新聞副標題搜尋)
    @PostMapping("/home_search_news_sub_title")
    public String HomeSearchNewsSubTitle(@ModelAttribute("news04") News news, Model model) {
//          將搜尋種類的暫存 更新為'新聞副標題'
            lastSearch = 4;
//          將搜尋種類的關鍵字的暫存 更新為最新輸入的關鍵字
            lastKeyWordStr = news.getNewsSubTitle();

//          (開始)新聞的搜尋的初始化設定
//            新建一個 新聞的分類搜尋的輸入用變數
              News news01 = new News();
//            新建一個 新聞的子分類搜尋的輸入用變數
              News news02 = new News();
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              News news03 = new News();
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              News news04 = news;
//            新建一個 新聞的開始時間搜尋的輸入用變數
              News news05 = new News();
//            新建一個 新聞的結束時間搜尋的輸入用變數
              News news06 = new News();
//            新建一個 新聞的複合搜尋的輸入用變數
              MultipleSearch news07 = new MultipleSearch();
//          (結束)新聞的搜尋的初始化設定

//          (開始)新聞的搜尋的Thymeleaf傳入設定
//            新建一個 新聞的分類搜尋的輸入用變數
              model.addAttribute("news01", news01);
//            新建一個 新聞的子分類搜尋的輸入用變數
              model.addAttribute("news02", news02);
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              model.addAttribute("news03", news03);
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              model.addAttribute("news04", news04);
//            新建一個 新聞的開始時間搜尋的輸入用變數
              model.addAttribute("news05", news05);
//            新建一個 新聞的結束時間搜尋的輸入用變數
              model.addAttribute("news06", news06);
//            新建一個 新聞的複合搜尋的輸入用變數
              model.addAttribute("news07", news07);
//          (結束)新聞的搜尋的Thymeleaf傳入設定

//          (開始)分類列表的初始化設定
//            將 新聞的分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("categoryList", categoryListInitializer());
//          (結束)分類列表的初始化設定
                  
//          (開始)子分類列表的初始化設定
//            將 新聞的子分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("subCategoryList", subCategoryListInitializer());
//          (結束)子分類列表的初始化設定

//          (開始)勾選設定
//            新建一個 勾選結果儲存用的變數
              CheckedRes checkedRes = new CheckedRes();
//            將 勾選結果儲存用的變數 傳到Thymeleaf
              model.addAttribute("checkedRes", checkedRes);
//          (結束)勾選設定

//          (開始)分頁設定
//            將 每頁最大顯示結果的筆數 改為10筆
              int pageNum = 0;
//              int pageSize = 10;
//            將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageNum", pageNum);
//            將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageSize", pageSize);
//          (結束)分頁設定

//          (開始)新聞搜尋結果的設定
//            將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
              model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//            將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//          (結束)新聞搜尋結果的設定

//            返回 管理者的主頁
              return "home";
            
    }
//  管理端的新聞主頁(用新聞的發布時間的開始時間搜尋)
    @PostMapping("/home_search_start_time")
    public String HomeSearchStartTime(@ModelAttribute("news05") News news, Model model) {
//          將搜尋種類的暫存 更新為'開始時間'
            lastSearch = 5;
//          將搜尋種類的關鍵字的暫存 更新為最新輸入的關鍵字
            lastKeyWordStr = news.getReleaseTime();

//          (開始)新聞的搜尋的初始化設定
//            新建一個 新聞的分類搜尋的輸入用變數
              News news01 = new News();
//            新建一個 新聞的子分類搜尋的輸入用變數
              News news02 = new News();
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              News news03 = new News();
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              News news04 = new News();
//            新建一個 新聞的開始時間搜尋的輸入用變數
              News news05 = news;
//            新建一個 新聞的結束時間搜尋的輸入用變數
              News news06 = new News();
//            新建一個 新聞的複合搜尋的輸入用變數
              MultipleSearch news07 = new MultipleSearch();
//          (結束)新聞的搜尋的初始化設定

//          (開始)新聞的搜尋的Thymeleaf傳入設定
//            新建一個 新聞的分類搜尋的輸入用變數
              model.addAttribute("news01", news01);
//            新建一個 新聞的子分類搜尋的輸入用變數
              model.addAttribute("news02", news02);
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              model.addAttribute("news03", news03);
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              model.addAttribute("news04", news04);
//            新建一個 新聞的開始時間搜尋的輸入用變數
              model.addAttribute("news05", news05);
//            新建一個 新聞的結束時間搜尋的輸入用變數
              model.addAttribute("news06", news06);
//            新建一個 新聞的複合搜尋的輸入用變數
              model.addAttribute("news07", news07);
//          (結束)新聞的搜尋的Thymeleaf傳入設定

//          (開始)分類列表的初始化設定
//            將 新聞的分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("categoryList", categoryListInitializer());
//          (結束)分類列表的初始化設定
                  
//          (開始)子分類列表的初始化設定
//            將 新聞的子分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("subCategoryList", subCategoryListInitializer());
//          (結束)子分類列表的初始化設定

//          (開始)勾選設定
//            新建一個 勾選結果儲存用的變數
              CheckedRes checkedRes = new CheckedRes();
//            將 勾選結果儲存用的變數 傳到Thymeleaf
              model.addAttribute("checkedRes", checkedRes);
//          (結束)勾選設定

//          (開始)分頁設定
//            將 每頁最大顯示結果的筆數 改為10筆
              int pageNum = 0;
//              int pageSize = 10;
//            將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageNum", pageNum);
//            將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageSize", pageSize);
//          (結束)分頁設定

//          (開始)新聞搜尋結果的設定
//            將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
              model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//            將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//          (結束)新聞搜尋結果的設定

//            返回 管理者的主頁
              return "home";
            
    }
//  管理端的新聞主頁(用新聞的發布時間的結束時間搜尋)
    @PostMapping("/home_search_end_time")
    public String HomeSearchEndTime(@ModelAttribute("news06") News news, Model model) {
//          將搜尋種類的暫存 更新為'結束時間'
            lastSearch = 6;
//          將搜尋種類的關鍵字的暫存 更新為最新輸入的關鍵字
            lastKeyWordStr = news.getReleaseTime();

//          (開始)新聞的搜尋的初始化設定
//            新建一個 新聞的分類搜尋的輸入用變數
              News news01 = new News();
//            新建一個 新聞的子分類搜尋的輸入用變數
              News news02 = new News();
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              News news03 = new News();
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              News news04 = new News();
//            新建一個 新聞的開始時間搜尋的輸入用變數
              News news05 = new News();
//            新建一個 新聞的結束時間搜尋的輸入用變數
              News news06 = news;
//            新建一個 新聞的複合搜尋的輸入用變數
              MultipleSearch news07 = new MultipleSearch();
//          (結束)新聞的搜尋的初始化設定

//          (開始)新聞的搜尋的Thymeleaf傳入設定
//            新建一個 新聞的分類搜尋的輸入用變數
              model.addAttribute("news01", news01);
//            新建一個 新聞的子分類搜尋的輸入用變數
              model.addAttribute("news02", news02);
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              model.addAttribute("news03", news03);
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              model.addAttribute("news04", news04);
//            新建一個 新聞的開始時間搜尋的輸入用變數
              model.addAttribute("news05", news05);
//            新建一個 新聞的結束時間搜尋的輸入用變數
              model.addAttribute("news06", news06);
//            新建一個 新聞的複合搜尋的輸入用變數
              model.addAttribute("news07", news07);
//          (結束)新聞的搜尋的Thymeleaf傳入設定

//          (開始)分類列表的初始化設定
//            將 新聞的分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("categoryList", categoryListInitializer());
//          (結束)分類列表的初始化設定
                  
//          (開始)子分類列表的初始化設定
//            將 新聞的子分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("subCategoryList", subCategoryListInitializer());
//          (結束)子分類列表的初始化設定

//          (開始)勾選設定
//            新建一個 勾選結果儲存用的變數
              CheckedRes checkedRes = new CheckedRes();
//            將 勾選結果儲存用的變數 傳到Thymeleaf
              model.addAttribute("checkedRes", checkedRes);
//          (結束)勾選設定

//          (開始)分頁設定
//            將 每頁最大顯示結果的筆數 改為10筆
              int pageNum = 0;
//              int pageSize = 10;
//            將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageNum", pageNum);
//            將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageSize", pageSize);
//          (結束)分頁設定

//          (開始)新聞搜尋結果的設定
//            將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
              model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//            將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//          (結束)新聞搜尋結果的設定
            
        return "home";
            
    }
//  管理端的新聞主頁(用新聞的複合條件搜尋)
    @PostMapping("/news_multiple_search")
    public String NewsMultipleSearch(@ModelAttribute("news07") MultipleSearch news, Model model) {

            lastSearch = 7;
            lastKeyWordMultipleStr = news;

//          (開始)新聞的搜尋的初始化設定
//            新建一個 新聞的分類搜尋的輸入用變數
              News news01 = new News();
//            新建一個 新聞的子分類搜尋的輸入用變數
              News news02 = new News();
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              News news03 = new News();
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              News news04 = new News();
//            新建一個 新聞的開始時間搜尋的輸入用變數
              News news05 = new News();
//            新建一個 新聞的結束時間搜尋的輸入用變數
              News news06 = new News();
//            新建一個 新聞的複合搜尋的輸入用變數
              MultipleSearch news07 = news;
//          (結束)新聞的搜尋的初始化設定

//          (開始)新聞的搜尋的Thymeleaf傳入設定
//            新建一個 新聞的分類搜尋的輸入用變數
              model.addAttribute("news01", news01);
//            新建一個 新聞的子分類搜尋的輸入用變數
              model.addAttribute("news02", news02);
//            新建一個 新聞的新聞標題搜尋的輸入用變數
              model.addAttribute("news03", news03);
//            新建一個 新聞的新聞副標題搜尋的輸入用變數
              model.addAttribute("news04", news04);
//            新建一個 新聞的開始時間搜尋的輸入用變數
              model.addAttribute("news05", news05);
//            新建一個 新聞的結束時間搜尋的輸入用變數
              model.addAttribute("news06", news06);
//            新建一個 新聞的複合搜尋的輸入用變數
              model.addAttribute("news07", news07);
//          (結束)新聞的搜尋的Thymeleaf傳入設定

//          (開始)分類列表的初始化設定
//            將 新聞的分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("categoryList", categoryListInitializer());
//          (結束)分類列表的初始化設定
                  
//          (開始)子分類列表的初始化設定
//            將 新聞的子分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("subCategoryList", subCategoryListInitializer());
//          (結束)子分類列表的初始化設定

//          (開始)勾選設定
//            新建一個 勾選結果儲存用的變數
              CheckedRes checkedRes = new CheckedRes();
//            將 勾選結果儲存用的變數 傳到Thymeleaf
              model.addAttribute("checkedRes", checkedRes);
//          (結束)勾選設定

//          (開始)分頁設定
//            將 每頁最大顯示結果的筆數 改為10筆
              int pageNum = 0;
//              int pageSize = 10;
//            將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageNum", pageNum);
//            將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("pageSize", pageSize);
//          (結束)分頁設定

//          (開始)新聞搜尋結果的設定
//            將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
              model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//            將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
              model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//          (結束)新聞搜尋結果的設定
            
        return "home";
            
    }
//  管理端的勾選動作
    @PostMapping("/home")
    public String HomeChecked(@ModelAttribute("checkedRes") CheckedRes checkedRes, Model model) {
//    將勾選結果的暫存 更新為最新的勾選結果
      checkedResTemp = checkedRes;
//    對勾選結果的數量 做初始化
      checkedResTempCount = 0;
//    統計 勾選結果
      if(checkedRes.isChecked1())
          checkedResTempCount++;
      if(checkedRes.isChecked2())
          checkedResTempCount++;
      if(checkedRes.isChecked3())
          checkedResTempCount++;
      if(checkedRes.isChecked4())
          checkedResTempCount++;
      if(checkedRes.isChecked5())
          checkedResTempCount++;
      if(checkedRes.isChecked6())
          checkedResTempCount++;
      if(checkedRes.isChecked7())
          checkedResTempCount++;
      if(checkedRes.isChecked8())
          checkedResTempCount++;
      if(checkedRes.isChecked9())
          checkedResTempCount++;
      if(checkedRes.isChecked10())
          checkedResTempCount++;
      if(checkedRes.isChecked11())
          checkedResTempCount++;
      if(checkedRes.isChecked12())
          checkedResTempCount++;
      if(checkedRes.isChecked13())
          checkedResTempCount++;
      if(checkedRes.isChecked14())
          checkedResTempCount++;
      if(checkedRes.isChecked15())
          checkedResTempCount++;
      if(checkedRes.isChecked16())
          checkedResTempCount++;
      if(checkedRes.isChecked17())
          checkedResTempCount++;
      if(checkedRes.isChecked18())
          checkedResTempCount++;
      if(checkedRes.isChecked19())
          checkedResTempCount++;
      if(checkedRes.isChecked20())
          checkedResTempCount++;
      if(checkedRes.isChecked21())
          checkedResTempCount++;
      if(checkedRes.isChecked22())
          checkedResTempCount++;
      if(checkedRes.isChecked23())
          checkedResTempCount++;
      if(checkedRes.isChecked24())
          checkedResTempCount++;
      if(checkedRes.isChecked25())
          checkedResTempCount++;
      if(checkedRes.isChecked26())
          checkedResTempCount++;
      if(checkedRes.isChecked27())
          checkedResTempCount++;
      if(checkedRes.isChecked28())
          checkedResTempCount++;
      if(checkedRes.isChecked29())
          checkedResTempCount++;
      if(checkedRes.isChecked30())
          checkedResTempCount++;
      if(checkedRes.isChecked31())
          checkedResTempCount++;
      if(checkedRes.isChecked32())
          checkedResTempCount++;
      if(checkedRes.isChecked33())
          checkedResTempCount++;
      if(checkedRes.isChecked34())
          checkedResTempCount++;
      if(checkedRes.isChecked35())
          checkedResTempCount++;
      if(checkedRes.isChecked36())
          checkedResTempCount++;
      if(checkedRes.isChecked37())
          checkedResTempCount++;
      if(checkedRes.isChecked38())
          checkedResTempCount++;
      if(checkedRes.isChecked39())
          checkedResTempCount++;
      if(checkedRes.isChecked40())
          checkedResTempCount++;
      if(checkedRes.isChecked41())
          checkedResTempCount++;
      if(checkedRes.isChecked42())
          checkedResTempCount++;
      if(checkedRes.isChecked43())
          checkedResTempCount++;
      if(checkedRes.isChecked44())
          checkedResTempCount++;
      if(checkedRes.isChecked45())
          checkedResTempCount++;
      if(checkedRes.isChecked46())
          checkedResTempCount++;
      if(checkedRes.isChecked47())
          checkedResTempCount++;
      if(checkedRes.isChecked48())
          checkedResTempCount++;
      if(checkedRes.isChecked49())
          checkedResTempCount++;
      if(checkedRes.isChecked50())
          checkedResTempCount++;
      if(checkedRes.isChecked51())
          checkedResTempCount++;
      if(checkedRes.isChecked52())
          checkedResTempCount++;
      if(checkedRes.isChecked53())
          checkedResTempCount++;
      if(checkedRes.isChecked54())
          checkedResTempCount++;
      if(checkedRes.isChecked55())
          checkedResTempCount++;
      if(checkedRes.isChecked56())
          checkedResTempCount++;
      if(checkedRes.isChecked57())
          checkedResTempCount++;
      if(checkedRes.isChecked58())
          checkedResTempCount++;
      if(checkedRes.isChecked59())
          checkedResTempCount++;
      if(checkedRes.isChecked60())
          checkedResTempCount++;
      if(checkedRes.isChecked61())
          checkedResTempCount++;
      if(checkedRes.isChecked62())
          checkedResTempCount++;
      if(checkedRes.isChecked63())
          checkedResTempCount++;
      if(checkedRes.isChecked64())
          checkedResTempCount++;
      if(checkedRes.isChecked65())
          checkedResTempCount++;
      if(checkedRes.isChecked66())
          checkedResTempCount++;
      if(checkedRes.isChecked67())
          checkedResTempCount++;
      if(checkedRes.isChecked68())
          checkedResTempCount++;
      if(checkedRes.isChecked69())
          checkedResTempCount++;
      if(checkedRes.isChecked70())
          checkedResTempCount++;
      if(checkedRes.isChecked71())
          checkedResTempCount++;
      if(checkedRes.isChecked72())
          checkedResTempCount++;
      if(checkedRes.isChecked73())
          checkedResTempCount++;
      if(checkedRes.isChecked74())
          checkedResTempCount++;
      if(checkedRes.isChecked75())
          checkedResTempCount++;
      if(checkedRes.isChecked76())
          checkedResTempCount++;
      if(checkedRes.isChecked77())
          checkedResTempCount++;
      if(checkedRes.isChecked78())
          checkedResTempCount++;
      if(checkedRes.isChecked79())
          checkedResTempCount++;
      if(checkedRes.isChecked80())
          checkedResTempCount++;
      if(checkedRes.isChecked81())
          checkedResTempCount++;
      if(checkedRes.isChecked82())
          checkedResTempCount++;
      if(checkedRes.isChecked83())
          checkedResTempCount++;
      if(checkedRes.isChecked84())
          checkedResTempCount++;
      if(checkedRes.isChecked85())
          checkedResTempCount++;
      if(checkedRes.isChecked86())
          checkedResTempCount++;
      if(checkedRes.isChecked87())
          checkedResTempCount++;
      if(checkedRes.isChecked88())
          checkedResTempCount++;
      if(checkedRes.isChecked89())
          checkedResTempCount++;
      if(checkedRes.isChecked90())
          checkedResTempCount++;
      if(checkedRes.isChecked91())
          checkedResTempCount++;
      if(checkedRes.isChecked92())
          checkedResTempCount++;
      if(checkedRes.isChecked93())
          checkedResTempCount++;
      if(checkedRes.isChecked94())
          checkedResTempCount++;
      if(checkedRes.isChecked95())
          checkedResTempCount++;
      if(checkedRes.isChecked96())
          checkedResTempCount++;
      if(checkedRes.isChecked97())
          checkedResTempCount++;
      if(checkedRes.isChecked98())
          checkedResTempCount++;
      if(checkedRes.isChecked99())
          checkedResTempCount++;
      if(checkedRes.isChecked100())
          checkedResTempCount++;
//    (開始)新聞的搜尋的初始化設定
//    新建一個 新聞的分類搜尋的輸入用變數
      News news01 = new News();
//    新建一個 新聞的子分類搜尋的輸入用變數
      News news02 = new News();
//    新建一個 新聞的新聞標題搜尋的輸入用變數
      News news03 = new News();
//    新建一個 新聞的新聞副標題搜尋的輸入用變數
      News news04 = new News();
//    新建一個 新聞的開始時間搜尋的輸入用變數
      News news05 = new News();
//    新建一個 新聞的結束時間搜尋的輸入用變數
      News news06 = new News();
//    新建一個 新聞的複合搜尋的輸入用變數
      MultipleSearch news07 = new MultipleSearch();
//  (結束)新聞的搜尋的初始化設定

//  (開始)新聞的搜尋的初始值設定
//    新聞的分類搜尋
      if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
          news01.setCategory(lastKeyWordStr);
//    新聞的子分類搜尋
      if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
          news02.setSubCategory(lastKeyWordStr);  
//    新聞的新聞標題搜尋
      if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
          news03.setNewsTitle(lastKeyWordStr);  
//    新聞的新聞副標題搜尋
      if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
          news04.setNewsSubTitle(lastKeyWordStr);
//    新聞的開始時間搜尋
      if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
          news05.setReleaseTime(lastKeyWordStr);
//    新聞的結束時間搜尋
      if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
          news06.setReleaseTime(lastKeyWordStr);
//    新聞的複合搜尋
      if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
          news07 = lastKeyWordMultipleStr;
//  (結束)新聞的搜尋的初始值設定
      
//  (開始)新聞的搜尋的Thymeleaf傳入設定
//    新建一個 新聞的分類搜尋的輸入用變數
      model.addAttribute("news01", news01);
//    新建一個 新聞的子分類搜尋的輸入用變數
      model.addAttribute("news02", news02);
//    新建一個 新聞的新聞標題搜尋的輸入用變數
      model.addAttribute("news03", news03);
//    新建一個 新聞的新聞副標題搜尋的輸入用變數
      model.addAttribute("news04", news04);
//    新建一個 新聞的開始時間搜尋的輸入用變數
      model.addAttribute("news05", news05);
//    新建一個 新聞的結束時間搜尋的輸入用變數
      model.addAttribute("news06", news06);
//    新建一個 新聞的複合搜尋的輸入用變數
      model.addAttribute("news07", news07);
//  (結束)新聞的搜尋的Thymeleaf傳入設定

//  (開始)分類列表的初始化設定
//    將 新聞的分類選擇框用的List 傳到Thymeleaf
      model.addAttribute("categoryList", categoryListInitializer());
//  (結束)分類列表的初始化設定
      
//  (開始)子分類列表的初始化設定
//    將 新聞的子分類選擇框用的List 傳到Thymeleaf
      model.addAttribute("subCategoryList", subCategoryListInitializer());
//  (結束)子分類列表的初始化設定
    
//  (開始)勾選設定
//    勾選結果儲存用的變數 設定為初始值
      checkedRes = new CheckedRes();
//    將 勾選結果儲存用的變數 傳到Thymeleaf
      model.addAttribute("checkedRes", checkedRes);
//  (結束)勾選設定

//  (開始)分頁設定
//    將 空的目前選擇的分頁 改為目前的頁數
      int pageNum = pageNumTemp;
//    將 每頁最大顯示結果的筆數 改為10筆
//      int pageSize = 10;
//    將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
      model.addAttribute("pageNum", pageNum);
//    將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
      model.addAttribute("pageSize", pageSize);
//  (結束)分頁設定

//  (開始)新聞搜尋結果的設定
//    將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
      model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//    將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
      model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//  (結束)新聞搜尋結果的設定
      
//    返回 管理端的新聞主頁
      return "home";
    }
//  管理端的全選動作
    @RequestMapping("/all_check")
    public String HomeAllChecked(Model model) {
//    新建一個 勾選用的變數
      CheckedRes checkedRes = new CheckedRes();
//    將每個欄位的勾選結果 都改成true
      checkedRes.setChecked1(true);
      checkedRes.setChecked2(true);
      checkedRes.setChecked3(true);
      checkedRes.setChecked4(true);
      checkedRes.setChecked5(true);
      checkedRes.setChecked6(true);
      checkedRes.setChecked7(true);
      checkedRes.setChecked8(true);
      checkedRes.setChecked9(true);
      checkedRes.setChecked10(true);
      checkedRes.setChecked11(true);
      checkedRes.setChecked12(true);
      checkedRes.setChecked13(true);
      checkedRes.setChecked14(true);
      checkedRes.setChecked15(true);
      checkedRes.setChecked16(true);
      checkedRes.setChecked17(true);
      checkedRes.setChecked18(true);
      checkedRes.setChecked19(true);
      checkedRes.setChecked20(true);
      checkedRes.setChecked21(true);
      checkedRes.setChecked22(true);
      checkedRes.setChecked23(true);
      checkedRes.setChecked24(true);
      checkedRes.setChecked25(true);
      checkedRes.setChecked26(true);
      checkedRes.setChecked27(true);
      checkedRes.setChecked28(true);
      checkedRes.setChecked29(true);
      checkedRes.setChecked30(true);
      checkedRes.setChecked31(true);
      checkedRes.setChecked32(true);
      checkedRes.setChecked33(true);
      checkedRes.setChecked34(true);
      checkedRes.setChecked35(true);
      checkedRes.setChecked36(true);
      checkedRes.setChecked37(true);
      checkedRes.setChecked38(true);
      checkedRes.setChecked39(true);
      checkedRes.setChecked40(true);
      checkedRes.setChecked41(true);
      checkedRes.setChecked42(true);
      checkedRes.setChecked43(true);
      checkedRes.setChecked44(true);
      checkedRes.setChecked45(true);
      checkedRes.setChecked46(true);
      checkedRes.setChecked47(true);
      checkedRes.setChecked48(true);
      checkedRes.setChecked49(true);
      checkedRes.setChecked50(true);
      checkedRes.setChecked51(true);
      checkedRes.setChecked52(true);
      checkedRes.setChecked53(true);
      checkedRes.setChecked54(true);
      checkedRes.setChecked55(true);
      checkedRes.setChecked56(true);
      checkedRes.setChecked57(true);
      checkedRes.setChecked58(true);
      checkedRes.setChecked59(true);
      checkedRes.setChecked60(true);
      checkedRes.setChecked61(true);
      checkedRes.setChecked62(true);
      checkedRes.setChecked63(true);
      checkedRes.setChecked64(true);
      checkedRes.setChecked65(true);
      checkedRes.setChecked66(true);
      checkedRes.setChecked67(true);
      checkedRes.setChecked68(true);
      checkedRes.setChecked69(true);
      checkedRes.setChecked70(true);
      checkedRes.setChecked71(true);
      checkedRes.setChecked72(true);
      checkedRes.setChecked73(true);
      checkedRes.setChecked74(true);
      checkedRes.setChecked75(true);
      checkedRes.setChecked76(true);
      checkedRes.setChecked77(true);
      checkedRes.setChecked78(true);
      checkedRes.setChecked79(true);
      checkedRes.setChecked80(true);
      checkedRes.setChecked81(true);
      checkedRes.setChecked82(true);
      checkedRes.setChecked83(true);
      checkedRes.setChecked84(true);
      checkedRes.setChecked85(true);
      checkedRes.setChecked86(true);
      checkedRes.setChecked87(true);
      checkedRes.setChecked88(true);
      checkedRes.setChecked89(true);
      checkedRes.setChecked90(true);
      checkedRes.setChecked91(true);
      checkedRes.setChecked92(true);
      checkedRes.setChecked93(true);
      checkedRes.setChecked94(true);
      checkedRes.setChecked95(true);
      checkedRes.setChecked96(true);
      checkedRes.setChecked97(true);
      checkedRes.setChecked98(true);
      checkedRes.setChecked99(true);
      checkedRes.setChecked100(true);
      
//    (開始)新聞的搜尋的初始化設定
//    新建一個 新聞的分類搜尋的輸入用變數
      News news01 = new News();
//    新建一個 新聞的子分類搜尋的輸入用變數
      News news02 = new News();
//    新建一個 新聞的新聞標題搜尋的輸入用變數
      News news03 = new News();
//    新建一個 新聞的新聞副標題搜尋的輸入用變數
      News news04 = new News();
//    新建一個 新聞的開始時間搜尋的輸入用變數
      News news05 = new News();
//    新建一個 新聞的結束時間搜尋的輸入用變數
      News news06 = new News();
//    新建一個 新聞的複合搜尋的輸入用變數
      MultipleSearch news07 = new MultipleSearch();
//  (結束)新聞的搜尋的初始化設定

//  (開始)新聞的搜尋的初始值設定
//    新聞的分類搜尋
      if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
          news01.setCategory(lastKeyWordStr);
//    新聞的子分類搜尋
      if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
          news02.setSubCategory(lastKeyWordStr);  
//    新聞的新聞標題搜尋
      if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
          news03.setNewsTitle(lastKeyWordStr);  
//    新聞的新聞副標題搜尋
      if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
          news04.setNewsSubTitle(lastKeyWordStr);
//    新聞的開始時間搜尋
      if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
          news05.setReleaseTime(lastKeyWordStr);
//    新聞的結束時間搜尋
      if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
          news06.setReleaseTime(lastKeyWordStr);
//    新聞的複合搜尋
      if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
          news07 = lastKeyWordMultipleStr;
//  (結束)新聞的搜尋的初始值設定
      
//  (開始)新聞的搜尋的Thymeleaf傳入設定
//    新建一個 新聞的分類搜尋的輸入用變數
      model.addAttribute("news01", news01);
//    新建一個 新聞的子分類搜尋的輸入用變數
      model.addAttribute("news02", news02);
//    新建一個 新聞的新聞標題搜尋的輸入用變數
      model.addAttribute("news03", news03);
//    新建一個 新聞的新聞副標題搜尋的輸入用變數
      model.addAttribute("news04", news04);
//    新建一個 新聞的開始時間搜尋的輸入用變數
      model.addAttribute("news05", news05);
//    新建一個 新聞的結束時間搜尋的輸入用變數
      model.addAttribute("news06", news06);
//    新建一個 新聞的結束時間搜尋的輸入用變數
      model.addAttribute("news07", news07);
//  (結束)新聞的搜尋的Thymeleaf傳入設定

//  (開始)分類列表的初始化設定
//    將 新聞的分類選擇框用的List 傳到Thymeleaf
      model.addAttribute("categoryList", categoryListInitializer());
//  (結束)分類列表的初始化設定
      
//  (開始)子分類列表的初始化設定
//    將 新聞的子分類選擇框用的List 傳到Thymeleaf
      model.addAttribute("subCategoryList", subCategoryListInitializer());
//  (結束)子分類列表的初始化設定
    
//  (開始)勾選設定
//    勾選結果儲存用的變數 設定為初始值
//      checkedRes = new CheckedRes();
//    將 勾選結果儲存用的變數 傳到Thymeleaf
      model.addAttribute("checkedRes", checkedRes);
//  (結束)勾選設定

//  (開始)分頁設定
//    將 空的目前選擇的分頁 改為目前的頁數
      int pageNum = pageNumTemp;
//    將 每頁最大顯示結果的筆數 改為10筆
//      int pageSize = 10;
//    將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
      model.addAttribute("pageNum", pageNum);
//    將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
      model.addAttribute("pageSize", pageSize);
//  (結束)分頁設定

//  (開始)新聞搜尋結果的設定
//    將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
      model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//    將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
      model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//  (結束)新聞搜尋結果的設定
      
//    返回 管理端的新聞主頁
      return "home";
    }
    
    //////////////////////////////////////////////////
//  管理端的新聞預新增動作(填寫)
    @RequestMapping(value = "/news_add")
    public String NewsAdd(Model model) {
//      新建一個 新聞型別的變數
        News news = new News();
//      將 空的新聞型別的變數 傳到Thymeleaf
        model.addAttribute("news", news);
//      將 空的錯誤訊息 傳到Thymeleaf
        model.addAttribute("error", "");

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定

//      返回 新聞的新增畫面          
        return "news_add";
    }
//  管理端的新聞預新增動作(提交)
    @PostMapping("/news_add")
    public String NewsAdd(@ModelAttribute("news") News news, Model model) {
//      進行 新聞的新增動作
        NewsAddResponse res = mainService.newsEditCheck(news);
//      判斷 新聞的編輯動作是否失敗
        if(res.getCode() != "200") {
//          將 新聞的分選擇的暫存 更新為最新選擇的分類
            newsAddCategorySelect = news.getCategory();

//          (開始)分類列表的初始化設定
//            將 新聞的分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("categoryList", categoryListInitializer());
//          (結束)分類列表的初始化設定
              
//          (開始)子分類列表的初始化設定
//            將 新聞的子分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("subCategoryList", subCategoryListInitializer());
//          (結束)子分類列表的初始化設定
            
//          將錯誤訊息 傳到Thymeleaf
            model.addAttribute("error", res.getMessage());
//          返回 新聞的新增頁面
            return "news_add";
        }
//      將 新聞新增的新聞暫存 更新為此次新增的新聞
        newsAddCheckData = res;
//      跳轉到 新聞預覽的頁面
        return "news_add_preview";
    }
//  管理端的新聞預編輯動作(填寫)    
    @RequestMapping(value = "/news_edit")
    public String NewsEdit(Model model) {

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定

//        (開始)新聞的搜尋的初始化設定
//          新建一個 新聞的分類搜尋的輸入用變數
            News news01 = new News();
//          新建一個 新聞的子分類搜尋的輸入用變數
            News news02 = new News();
//          新建一個 新聞的新聞標題搜尋的輸入用變數
            News news03 = new News();
//          新建一個 新聞的新聞副標題搜尋的輸入用變數
            News news04 = new News();
//          新建一個 新聞的開始時間搜尋的輸入用變數
            News news05 = new News();
//          新建一個 新聞的結束時間搜尋的輸入用變數
            News news06 = new News();
//          新建一個 新聞的複合搜尋的輸入用變數
            MultipleSearch news07 = new MultipleSearch();
//        (結束)新聞的搜尋的初始化設定

//        (開始)新聞的搜尋的初始值設定
//          新聞的分類搜尋
            if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
                news01.setCategory(lastKeyWordStr);
//          新聞的子分類搜尋
            if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
                news02.setSubCategory(lastKeyWordStr);  
//          新聞的新聞標題搜尋
            if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
                news03.setNewsTitle(lastKeyWordStr);  
//          新聞的新聞副標題搜尋
            if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
                news04.setNewsSubTitle(lastKeyWordStr);
//          新聞的開始時間搜尋
            if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
                news05.setReleaseTime(lastKeyWordStr);
//          新聞的結束時間搜尋
            if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
                news06.setReleaseTime(lastKeyWordStr);
//          新聞的複合搜尋
            if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
                news07 = lastKeyWordMultipleStr;
//        (結束)新聞的搜尋的初始值設定
            
//        (開始)新聞的搜尋的Thymeleaf傳入設定
//          新建一個 新聞的分類搜尋的輸入用變數
            model.addAttribute("news01", news01);
//          新建一個 新聞的子分類搜尋的輸入用變數
            model.addAttribute("news02", news02);
//          新建一個 新聞的新聞標題搜尋的輸入用變數
            model.addAttribute("news03", news03);
//          新建一個 新聞的新聞副標題搜尋的輸入用變數
            model.addAttribute("news04", news04);
//          新建一個 新聞的開始時間搜尋的輸入用變數
            model.addAttribute("news05", news05);
//          新建一個 新聞的結束時間搜尋的輸入用變數
            model.addAttribute("news06", news06);
//          新建一個 新聞的複合搜尋的輸入用變數
            model.addAttribute("news07", news07);
//        (結束)新聞的搜尋的Thymeleaf傳入設定

//        (開始)勾選設定
//          新建一個 勾選結果儲存用的變數
            CheckedRes checkedRes = new CheckedRes();
//          將 勾選結果儲存用的變數 傳到Thymeleaf
            model.addAttribute("checkedRes", checkedRes);
//        (結束)勾選設定

//        (開始)分頁設定
            int pageNum = 0;
//          將 每頁最大顯示結果的筆數 改為10筆
            int pageSize = 10;
//          將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
            model.addAttribute("pageNum", pageNum);
//          將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
            model.addAttribute("pageSize", pageSize);
//        (結束)分頁設定

//        (開始)新聞搜尋結果的設定
//          將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
            model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//          將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
            model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//        (結束)新聞搜尋結果的設定

//      判斷 是否未勾選
        if(checkedResTemp == null) {
            
//          返回 管理端的新聞主頁
            return "home";
        }
//      判斷 是否勾選2個以上
        if(checkedResTempCount >= 2) {
            
//          返回 管理端的新聞主頁
            return "home";
        }
//      讀取 目前選擇的新聞
        News news = NewsListTempReader();
//      將 目前選擇的新聞的id暫存 更新為目前選擇的新聞的id
        idTemp = news.getId();
//      將 目前選擇的新聞 傳到Thymeleaf
        model.addAttribute("news", news);
//      將 空的錯誤訊息 傳到Thymeleaf
        model.addAttribute("error", "");
//      對勾選結果 進行初始化
        checkedResTemp = null;
//      跳轉到 新聞的編輯畫面
        return "news_edit";
    }
//  管理端的新聞預編輯動作(提交)
    @PostMapping("/news_edit")
    public String NewsEdit(@ModelAttribute("news") News news, Model model) {
//      進行 新聞的編輯動作
        NewsAddResponse res = mainService.newsEditCheck(news);
//      判斷 新聞的編輯動作是否失敗
        if(res.getCode() != "200") {
//          將 目前選擇的新聞的分類暫存 更新為最新選擇的新聞的分類
            newsAddCategorySelect = news.getCategory();

//          (開始)分類列表的初始化設定
//            將 新聞的分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("categoryList", categoryListInitializer());
//          (結束)分類列表的初始化設定
              
//          (開始)子分類列表的初始化設定
//            將 新聞的子分類選擇框用的List 傳到Thymeleaf
              model.addAttribute("subCategoryList", subCategoryListInitializer());
//          (結束)子分類列表的初始化設定

//          將 錯誤訊息 傳到Thymeleaf
            model.addAttribute("error", res.getMessage());
//          跳轉到 新聞的編輯畫面
            return "news_edit";
        }
//      將 編輯新聞的新聞暫存 更新到最新
        newsAddCheckData = res;
//      跳轉到 新聞編輯的預覽頁面
        return "news_edit_preview";
    }
//  管理端的新聞新增的預覽(確認後新增)
    @PostMapping("/news_add_preview")
    public String NewsAddPreview(Model model) {
//      判斷 新增新聞的動作是否失敗
        if(newsAddCheckData.getCode() != "200") {
//          將錯誤訊息 傳到Thymeleaf
            model.addAttribute("error", newsAddCheckData.getMessage());
//          跳轉到 新增新聞的預覽頁面
            return "news_add_preview";
        }
//      進行 新聞的新增動作
        newsAddCheckData = mainService.newsAdd(newsAddCheckData);
//      判斷 新聞的新增動作是否失敗
        if(newsAddCheckData.getCode() != "200") {
//          將錯誤訊息 傳到Thymeleaf
            model.addAttribute("error", newsAddCheckData.getMessage());
//          跳轉到 新增新聞的預覽頁面
            return "news_add_preview";
        }
//      對新聞的暫存 進行初始化
        newsAddCheckData = null;
        
//      (開始)新聞的搜尋的初始化設定
//        新建一個 新聞的分類搜尋的輸入用變數
          News news01 = new News();
//        新建一個 新聞的子分類搜尋的輸入用變數
          News news02 = new News();
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          News news03 = new News();
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          News news04 = new News();
//        新建一個 新聞的開始時間搜尋的輸入用變數
          News news05 = new News();
//        新建一個 新聞的結束時間搜尋的輸入用變數
          News news06 = new News();
//        新建一個 新聞的複合搜尋的輸入用變數
          MultipleSearch news07 = new MultipleSearch();
//      (結束)新聞的搜尋的初始化設定

//      (開始)新聞的搜尋的初始值設定
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              news01.setCategory(lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              news02.setSubCategory(lastKeyWordStr);  
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              news03.setNewsTitle(lastKeyWordStr);  
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              news04.setNewsSubTitle(lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              news05.setReleaseTime(lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              news06.setReleaseTime(lastKeyWordStr);
//        新聞的複合搜尋
          if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
              news07 = lastKeyWordMultipleStr;
//      (結束)新聞的搜尋的初始值設定
          
//      (開始)新聞的搜尋的Thymeleaf傳入設定
//        新建一個 新聞的分類搜尋的輸入用變數
          model.addAttribute("news01", news01);
//        新建一個 新聞的子分類搜尋的輸入用變數
          model.addAttribute("news02", news02);
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          model.addAttribute("news03", news03);
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          model.addAttribute("news04", news04);
//        新建一個 新聞的開始時間搜尋的輸入用變數
          model.addAttribute("news05", news05);
//        新建一個 新聞的結束時間搜尋的輸入用變數
          model.addAttribute("news06", news06);
//        新建一個 新聞的複合搜尋的輸入用變數
          model.addAttribute("news07", news07);
//      (結束)新聞的搜尋的Thymeleaf傳入設定

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定
        
//      (開始)勾選設定
//        新建一個 勾選結果儲存用的變數
          CheckedRes checkedRes = new CheckedRes();
//        將 勾選結果儲存用的變數 傳到Thymeleaf
          model.addAttribute("checkedRes", checkedRes);
//      (結束)勾選設定

//      (開始)分頁設定
          int pageNum = 0;
//        將 每頁最大顯示結果的筆數 改為10筆
          int pageSize = 10;
//        將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageNum", pageNum);
//        將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageSize", pageSize);
//      (結束)分頁設定

//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定
          
//      返回 管理端的新聞主頁
        return "home";
    }
//  管理端的新聞編輯的預覽(確認後編輯)    
    @PostMapping("/news_edit_preview")
    public String NewsEditPreview(Model model) {

//      (開始)新聞的搜尋的初始化設定
//        新建一個 新聞的分類搜尋的輸入用變數
          News news01 = new News();
//        新建一個 新聞的子分類搜尋的輸入用變數
          News news02 = new News();
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          News news03 = new News();
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          News news04 = new News();
//        新建一個 新聞的開始時間搜尋的輸入用變數
          News news05 = new News();
//        新建一個 新聞的結束時間搜尋的輸入用變數
          News news06 = new News();
//        新建一個 新聞的複合搜尋的輸入用變數
          MultipleSearch news07 = new MultipleSearch();
//      (結束)新聞的搜尋的初始化設定

//      (開始)新聞的搜尋的初始值設定
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              news01.setCategory(lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              news02.setSubCategory(lastKeyWordStr);  
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              news03.setNewsTitle(lastKeyWordStr);  
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              news04.setNewsSubTitle(lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              news05.setReleaseTime(lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              news06.setReleaseTime(lastKeyWordStr);
//        新聞的複合搜尋
          if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
              news07 = lastKeyWordMultipleStr;
//      (結束)新聞的搜尋的初始值設定
          
//      (開始)新聞的搜尋的Thymeleaf傳入設定
//        新建一個 新聞的分類搜尋的輸入用變數
          model.addAttribute("news01", news01);
//        新建一個 新聞的子分類搜尋的輸入用變數
          model.addAttribute("news02", news02);
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          model.addAttribute("news03", news03);
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          model.addAttribute("news04", news04);
//        新建一個 新聞的開始時間搜尋的輸入用變數
          model.addAttribute("news05", news05);
//        新建一個 新聞的結束時間搜尋的輸入用變數
          model.addAttribute("news06", news06);
//        新建一個 新聞的複合搜尋的輸入用變數
          model.addAttribute("news07", news07);
//      (結束)新聞的搜尋的Thymeleaf傳入設定

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定
        
//      (開始)勾選設定
//        新建一個 勾選結果儲存用的變數
          CheckedRes checkedRes = new CheckedRes();
//        將 勾選結果儲存用的變數 傳到Thymeleaf
          model.addAttribute("checkedRes", checkedRes);
//      (結束)勾選設定

//      (開始)分頁設定
          int pageNum = 0;
//        將 每頁最大顯示結果的筆數 改為10筆
          int pageSize = 10;
//        將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageNum", pageNum);
//        將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageSize", pageSize);
//      (結束)分頁設定

//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定
      
//    對新聞的暫存的id 更新到最新
      newsAddCheckData.getNews().setId(idTemp); 
//    進行 編輯新聞的動作
      newsAddCheckData = mainService.newsEdit(newsAddCheckData);
//    判斷 編輯新聞的動作是否失敗
      if(newsAddCheckData.getCode() != "200") {
//        將 錯誤訊息 傳到Thymeleaf
          model.addAttribute("error", newsAddCheckData.getMessage());
//        跳轉到 編輯新聞的預覽頁面
          return "news_edit_preview";
      }
//    對新聞的暫存 進行初始化
      newsAddCheckData = null;
//    跳轉到 管理者的主頁
      return "home";
    }
    
//  管理端的新聞刪除動作
    @RequestMapping(value = "/news_delete")
    public String NewsDelete(@RequestParam(value = "pageSize", defaultValue = "3") int pageSize, Model model) {

//      (開始)新聞的搜尋的初始化設定
//        新建一個 新聞的分類搜尋的輸入用變數
          News news01 = new News();
//        新建一個 新聞的子分類搜尋的輸入用變數
          News news02 = new News();
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          News news03 = new News();
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          News news04 = new News();
//        新建一個 新聞的開始時間搜尋的輸入用變數
          News news05 = new News();
//        新建一個 新聞的結束時間搜尋的輸入用變數
          News news06 = new News();
//        新建一個 新聞的複合搜尋的輸入用變數
          MultipleSearch news07 = new MultipleSearch();
//      (結束)新聞的搜尋的初始化設定

//      (開始)新聞的搜尋的初始值設定
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              news01.setCategory(lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              news02.setSubCategory(lastKeyWordStr);  
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              news03.setNewsTitle(lastKeyWordStr);  
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              news04.setNewsSubTitle(lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              news05.setReleaseTime(lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              news06.setReleaseTime(lastKeyWordStr);
//        新聞的複合搜尋
          if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
              news07 = lastKeyWordMultipleStr;
//      (結束)新聞的搜尋的初始值設定
          
//      (開始)新聞的搜尋的Thymeleaf傳入設定
//        新建一個 新聞的分類搜尋的輸入用變數
          model.addAttribute("news01", news01);
//        新建一個 新聞的子分類搜尋的輸入用變數
          model.addAttribute("news02", news02);
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          model.addAttribute("news03", news03);
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          model.addAttribute("news04", news04);
//        新建一個 新聞的開始時間搜尋的輸入用變數
          model.addAttribute("news05", news05);
//        新建一個 新聞的結束時間搜尋的輸入用變數
          model.addAttribute("news06", news06);
//        新建一個 新聞的複合搜尋的輸入用變數
          model.addAttribute("news07", news07);
//      (結束)新聞的搜尋的Thymeleaf傳入設定

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定
        
//      (開始)勾選設定
//        新建一個 勾選結果儲存用的變數
          CheckedRes checkedRes = new CheckedRes();
//        將 勾選結果儲存用的變數 傳到Thymeleaf
          model.addAttribute("checkedRes", checkedRes);
//      (結束)勾選設定

//      (開始)分頁設定
          int pageNum = 0;
//        將 每頁最大顯示結果的筆數 改為10筆
          pageSize = 10;
//        將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageNum", pageNum);
//        將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageSize", pageSize);
//      (結束)分頁設定

//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定
          
//        判斷 是否未勾選
          if(checkedResTemp == null) {
//            跳轉到 管理者的主頁
              return "home";
          }
//        勾選用的陣列的初始化 
          int[] checked = new int[100];
          for(int i = 0; i < 100; i++) {
              checked[i] = 0;
          }
//        統計 勾選結果
          if((checkedResTemp.isChecked1() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked1() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked1() && pageSize == 50 && pageNumTemp != 0))
              checked[0] = 1;
          if((checkedResTemp.isChecked2() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked2() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked2() && pageSize == 50 && pageNumTemp != 0))
              checked[1] = 1;
          if((checkedResTemp.isChecked3() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked3() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked3() && pageSize == 50 && pageNumTemp != 0))
              checked[2] = 1;
          if((checkedResTemp.isChecked4() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked4() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked4() && pageSize == 50 && pageNumTemp != 0))
              checked[3] = 1;
          if((checkedResTemp.isChecked5() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked5() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked5() && pageSize == 50 && pageNumTemp != 0))
              checked[4] = 1;
          if((checkedResTemp.isChecked6() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked6() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked6() && pageSize == 50 && pageNumTemp != 0))
              checked[5] = 1;
          if((checkedResTemp.isChecked7() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked7() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked7() && pageSize == 50 && pageNumTemp != 0))
              checked[6] = 1;
          if((checkedResTemp.isChecked8() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked8() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked8() && pageSize == 50 && pageNumTemp != 0))
              checked[7] = 1;
          if((checkedResTemp.isChecked9() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked9() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked9() && pageSize == 50 && pageNumTemp != 0))
              checked[8] = 1;
          if((checkedResTemp.isChecked10() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked10() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked10() && pageSize == 50 && pageNumTemp != 0))
              checked[9] = 1;
          if((checkedResTemp.isChecked11() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked11() && pageSize == 50 && pageNumTemp != 0))
              checked[10] = 1;
          if((checkedResTemp.isChecked12() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked12() && pageSize == 50 && pageNumTemp != 0))
              checked[11] = 1;
          if((checkedResTemp.isChecked13() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked13() && pageSize == 50 && pageNumTemp != 0))
              checked[12] = 1;
          if((checkedResTemp.isChecked14() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked14() && pageSize == 50 && pageNumTemp != 0))
              checked[13] = 1;
          if((checkedResTemp.isChecked15() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked15() && pageSize == 50 && pageNumTemp != 0))
              checked[14] = 1;
          if((checkedResTemp.isChecked16() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked16() && pageSize == 50 && pageNumTemp != 0))
              checked[15] = 1;
          if((checkedResTemp.isChecked17() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked17() && pageSize == 50 && pageNumTemp != 0))
              checked[16] = 1;
          if((checkedResTemp.isChecked18() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked18() && pageSize == 50 && pageNumTemp != 0))
              checked[17] = 1;
          if((checkedResTemp.isChecked19() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked19() && pageSize == 50 && pageNumTemp != 0))
              checked[18] = 1;
          if((checkedResTemp.isChecked20() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked20() && pageSize == 50 && pageNumTemp != 0))
              checked[19] = 1;
          if((checkedResTemp.isChecked21() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked21() && pageSize == 50 && pageNumTemp != 0))
              checked[20] = 1;
          if((checkedResTemp.isChecked22() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked22() && pageSize == 50 && pageNumTemp != 0))
              checked[21] = 1;
          if((checkedResTemp.isChecked23() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked23() && pageSize == 50 && pageNumTemp != 0))
              checked[22] = 1;
          if((checkedResTemp.isChecked24() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked24() && pageSize == 50 && pageNumTemp != 0))
              checked[23] = 1;
          if((checkedResTemp.isChecked25() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked25() && pageSize == 50 && pageNumTemp != 0))
              checked[24] = 1;
          if((checkedResTemp.isChecked26() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked26() && pageSize == 50 && pageNumTemp != 0))
              checked[25] = 1;
          if((checkedResTemp.isChecked27() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked27() && pageSize == 50 && pageNumTemp != 0))
              checked[26] = 1;
          if((checkedResTemp.isChecked28() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked28() && pageSize == 50 && pageNumTemp != 0))
              checked[27] = 1;
          if((checkedResTemp.isChecked29() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked29() && pageSize == 50 && pageNumTemp != 0))
              checked[28] = 1;
          if((checkedResTemp.isChecked30() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked30() && pageSize == 50 && pageNumTemp != 0))
              checked[29] = 1;
          if((checkedResTemp.isChecked31() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked31() && pageSize == 50 && pageNumTemp != 0))
              checked[30] = 1;
          if((checkedResTemp.isChecked32() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked32() && pageSize == 50 && pageNumTemp != 0))
              checked[31] = 1;
          if((checkedResTemp.isChecked33() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked33() && pageSize == 50 && pageNumTemp != 0))
              checked[32] = 1;
          if((checkedResTemp.isChecked34() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked34() && pageSize == 50 && pageNumTemp != 0))
              checked[33] = 1;
          if((checkedResTemp.isChecked35() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked35() && pageSize == 50 && pageNumTemp != 0))
              checked[34] = 1;
          if((checkedResTemp.isChecked36() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked36() && pageSize == 50 && pageNumTemp != 0))
              checked[35] = 1;
          if((checkedResTemp.isChecked37() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked37() && pageSize == 50 && pageNumTemp != 0))
              checked[36] = 1;
          if((checkedResTemp.isChecked38() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked38() && pageSize == 50 && pageNumTemp != 0))
              checked[37] = 1;
          if((checkedResTemp.isChecked39() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked39() && pageSize == 50 && pageNumTemp != 0))
              checked[38] = 1;
          if((checkedResTemp.isChecked40() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked40() && pageSize == 50 && pageNumTemp != 0))
              checked[39] = 1;
          if((checkedResTemp.isChecked41() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked41() && pageSize == 50 && pageNumTemp != 0))
              checked[40] = 1;
          if((checkedResTemp.isChecked42() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked42() && pageSize == 50 && pageNumTemp != 0))
              checked[41] = 1;
          if((checkedResTemp.isChecked43() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked43() && pageSize == 50 && pageNumTemp != 0))
              checked[42] = 1;
          if((checkedResTemp.isChecked44() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked44() && pageSize == 50 && pageNumTemp != 0))
              checked[43] = 1;
          if((checkedResTemp.isChecked45() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked45() && pageSize == 50 && pageNumTemp != 0))
              checked[44] = 1;
          if((checkedResTemp.isChecked46() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked46() && pageSize == 50 && pageNumTemp != 0))
              checked[45] = 1;
          if((checkedResTemp.isChecked47() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked47() && pageSize == 50 && pageNumTemp != 0))
              checked[46] = 1;
          if((checkedResTemp.isChecked48() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked48() && pageSize == 50 && pageNumTemp != 0))
              checked[47] = 1;
          if((checkedResTemp.isChecked49() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked49() && pageSize == 50 && pageNumTemp != 0))
              checked[48] = 1;
          if((checkedResTemp.isChecked50() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked50() && pageSize == 50 && pageNumTemp != 0))
              checked[49] = 1;
          if(checkedResTemp.isChecked51() && pageNumTemp == 0)
              checked[50] = 1;
          if(checkedResTemp.isChecked52() && pageNumTemp == 0)
              checked[51] = 1;
          if(checkedResTemp.isChecked53() && pageNumTemp == 0)
              checked[52] = 1;
          if(checkedResTemp.isChecked54() && pageNumTemp == 0)
              checked[53] = 1;
          if(checkedResTemp.isChecked55() && pageNumTemp == 0)
              checked[54] = 1;
          if(checkedResTemp.isChecked56() && pageNumTemp == 0)
              checked[55] = 1;
          if(checkedResTemp.isChecked57() && pageNumTemp == 0)
              checked[56] = 1;
          if(checkedResTemp.isChecked58() && pageNumTemp == 0)
              checked[57] = 1;
          if(checkedResTemp.isChecked59() && pageNumTemp == 0)
              checked[58] = 1;
          if(checkedResTemp.isChecked60() && pageNumTemp == 0)
              checked[59] = 1;
          if(checkedResTemp.isChecked61() && pageNumTemp == 0)
              checked[60] = 1;
          if(checkedResTemp.isChecked62() && pageNumTemp == 0)
              checked[61] = 1;
          if(checkedResTemp.isChecked63() && pageNumTemp == 0)
              checked[62] = 1;
          if(checkedResTemp.isChecked64() && pageNumTemp == 0)
              checked[63] = 1;
          if(checkedResTemp.isChecked65() && pageNumTemp == 0)
              checked[64] = 1;
          if(checkedResTemp.isChecked66() && pageNumTemp == 0)
              checked[65] = 1;
          if(checkedResTemp.isChecked67() && pageNumTemp == 0)
              checked[66] = 1;
          if(checkedResTemp.isChecked68() && pageNumTemp == 0)
              checked[67] = 1;
          if(checkedResTemp.isChecked69() && pageNumTemp == 0)
              checked[68] = 1;
          if(checkedResTemp.isChecked70() && pageNumTemp == 0)
              checked[69] = 1;
          if(checkedResTemp.isChecked71() && pageNumTemp == 0)
              checked[70] = 1;
          if(checkedResTemp.isChecked72() && pageNumTemp == 0)
              checked[71] = 1;
          if(checkedResTemp.isChecked73() && pageNumTemp == 0)
              checked[72] = 1;
          if(checkedResTemp.isChecked74() && pageNumTemp == 0)
              checked[73] = 1;
          if(checkedResTemp.isChecked75() && pageNumTemp == 0)
              checked[74] = 1;
          if(checkedResTemp.isChecked76() && pageNumTemp == 0)
              checked[75] = 1;
          if(checkedResTemp.isChecked77() && pageNumTemp == 0)
              checked[76] = 1;
          if(checkedResTemp.isChecked78() && pageNumTemp == 0)
              checked[77] = 1;
          if(checkedResTemp.isChecked79() && pageNumTemp == 0)
              checked[78] = 1;
          if(checkedResTemp.isChecked80() && pageNumTemp == 0)
              checked[79] = 1;
          if(checkedResTemp.isChecked81() && pageNumTemp == 0)
              checked[80] = 1;
          if(checkedResTemp.isChecked82() && pageNumTemp == 0)
              checked[81] = 1;
          if(checkedResTemp.isChecked83() && pageNumTemp == 0)
              checked[82] = 1;
          if(checkedResTemp.isChecked84() && pageNumTemp == 0)
              checked[83] = 1;
          if(checkedResTemp.isChecked85() && pageNumTemp == 0)
              checked[84] = 1;
          if(checkedResTemp.isChecked86() && pageNumTemp == 0)
              checked[85] = 1;
          if(checkedResTemp.isChecked87() && pageNumTemp == 0)
              checked[86] = 1;
          if(checkedResTemp.isChecked88() && pageNumTemp == 0)
              checked[87] = 1;
          if(checkedResTemp.isChecked89() && pageNumTemp == 0)
              checked[88] = 1;
          if(checkedResTemp.isChecked90() && pageNumTemp == 0)
              checked[89] = 1;
          if(checkedResTemp.isChecked91() && pageNumTemp == 0)
              checked[90] = 1;
          if(checkedResTemp.isChecked92() && pageNumTemp == 0)
              checked[91] = 1;
          if(checkedResTemp.isChecked93() && pageNumTemp == 0)
              checked[92] = 1;
          if(checkedResTemp.isChecked94() && pageNumTemp == 0)
              checked[93] = 1;
          if(checkedResTemp.isChecked95() && pageNumTemp == 0)
              checked[94] = 1;
          if(checkedResTemp.isChecked96() && pageNumTemp == 0)
              checked[95] = 1;
          if(checkedResTemp.isChecked97() && pageNumTemp == 0)
              checked[96] = 1;
          if(checkedResTemp.isChecked98() && pageNumTemp == 0)
              checked[97] = 1;
          if(checkedResTemp.isChecked99() && pageNumTemp == 0)
              checked[98] = 1;
          if(checkedResTemp.isChecked100() && pageNumTemp == 0)
              checked[99] = 1;
          
          int counter = 0;
          for (News item : NewsListTemp) {
              if(checked[counter] == 1) {
//                對勾選的新聞 進行刪除
                  mainService.newsDelete(item.getId());
              }
              counter++;
          }
          
//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定
          
//      對新聞的暫存 進行初始化
        newsAddCheckData = null;
//      對勾選結果 進行初始化
        checkedResTemp = null;
//      跳轉到 管理者的主頁
        return "home";
    }
    
    
//  管理端的新聞詳細頁面
    @RequestMapping("/news_zoom")
    public String NewsZoom(Model model) {
//      (開始)新聞的搜尋的初始化設定
//        新建一個 新聞的分類搜尋的輸入用變數
          News news01 = new News();
//        新建一個 新聞的子分類搜尋的輸入用變數
          News news02 = new News();
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          News news03 = new News();
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          News news04 = new News();
//        新建一個 新聞的開始時間搜尋的輸入用變數
          News news05 = new News();
//        新建一個 新聞的結束時間搜尋的輸入用變數
          News news06 = new News();
//        新建一個 新聞的複合搜尋的輸入用變數
          MultipleSearch news07 = new MultipleSearch();
//      (結束)新聞的搜尋的初始化設定

//      (開始)新聞的搜尋的初始值設定
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              news01.setCategory(lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              news02.setSubCategory(lastKeyWordStr);  
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              news03.setNewsTitle(lastKeyWordStr);  
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              news04.setNewsSubTitle(lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              news05.setReleaseTime(lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              news06.setReleaseTime(lastKeyWordStr);
//        新聞的複合搜尋
          if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
              news07 = lastKeyWordMultipleStr;
//      (結束)新聞的搜尋的初始值設定
          
//      (開始)新聞的搜尋的Thymeleaf傳入設定
//        新建一個 新聞的分類搜尋的輸入用變數
          model.addAttribute("news01", news01);
//        新建一個 新聞的子分類搜尋的輸入用變數
          model.addAttribute("news02", news02);
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          model.addAttribute("news03", news03);
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          model.addAttribute("news04", news04);
//        新建一個 新聞的開始時間搜尋的輸入用變數
          model.addAttribute("news05", news05);
//        新建一個 新聞的結束時間搜尋的輸入用變數
          model.addAttribute("news06", news06);
//        新建一個 新聞的複合搜尋的輸入用變數
          model.addAttribute("news07", news07);
//      (結束)新聞的搜尋的Thymeleaf傳入設定

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定
        
//      (開始)勾選設定
//        新建一個 勾選結果儲存用的變數
          CheckedRes checkedRes = new CheckedRes();
//        將 勾選結果儲存用的變數 傳到Thymeleaf
          model.addAttribute("checkedRes", checkedRes);
//      (結束)勾選設定

//      (開始)分頁設定
          int pageNum = 0;
//        將 每頁最大顯示結果的筆數 改為10筆
          int pageSize = 10;
//        將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageNum", pageNum);
//        將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageSize", pageSize);
//      (結束)分頁設定

//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定

//      當未勾選 則返回管理者主頁
        if(checkedResTemp == null)
            return "home";
//      當勾選2個以上 則返回管理者主頁
        if(checkedResTempCount >= 2)
            return "home";
        
        int counter0 = 0;
        int counter = 0;
//      統計 勾選結果
        if(checkedResTemp.isChecked1())
            counter0 = 1;
        if(checkedResTemp.isChecked2())
            counter0 = 2;
        if(checkedResTemp.isChecked3())
            counter0 = 3;
        if(checkedResTemp.isChecked4())
            counter0 = 4;
        if(checkedResTemp.isChecked5())
            counter0 = 5;
        if(checkedResTemp.isChecked6())
            counter0 = 6;
        if(checkedResTemp.isChecked7())
            counter0 = 7;
        if(checkedResTemp.isChecked8())
            counter0 = 8;
        if(checkedResTemp.isChecked9())
            counter0 = 9;
        if(checkedResTemp.isChecked10())
            counter0 = 10;
        if(checkedResTemp.isChecked11())
            counter0 = 11;
        if(checkedResTemp.isChecked12())
            counter0 = 12;
        if(checkedResTemp.isChecked13())
            counter0 = 13;
        if(checkedResTemp.isChecked14())
            counter0 = 14;
        if(checkedResTemp.isChecked15())
            counter0 = 15;
        if(checkedResTemp.isChecked16())
            counter0 = 16;
        if(checkedResTemp.isChecked17())
            counter0 = 17;
        if(checkedResTemp.isChecked18())
            counter0 = 18;
        if(checkedResTemp.isChecked19())
            counter0 = 19;
        if(checkedResTemp.isChecked20())
            counter0 = 20;
        if(checkedResTemp.isChecked21())
            counter0 = 21;
        if(checkedResTemp.isChecked22())
            counter0 = 22;
        if(checkedResTemp.isChecked23())
            counter0 = 23;
        if(checkedResTemp.isChecked24())
            counter0 = 24;
        if(checkedResTemp.isChecked25())
            counter0 = 25;
        if(checkedResTemp.isChecked26())
            counter0 = 26;
        if(checkedResTemp.isChecked27())
            counter0 = 27;
        if(checkedResTemp.isChecked28())
            counter0 = 28;
        if(checkedResTemp.isChecked29())
            counter0 = 29;
        if(checkedResTemp.isChecked30())
            counter0 = 30;
        if(checkedResTemp.isChecked31())
            counter0 = 31;
        if(checkedResTemp.isChecked32())
            counter0 = 32;
        if(checkedResTemp.isChecked33())
            counter0 = 33;
        if(checkedResTemp.isChecked34())
            counter0 = 34;
        if(checkedResTemp.isChecked35())
            counter0 = 35;
        if(checkedResTemp.isChecked36())
            counter0 = 36;
        if(checkedResTemp.isChecked37())
            counter0 = 37;
        if(checkedResTemp.isChecked38())
            counter0 = 38;
        if(checkedResTemp.isChecked39())
            counter0 = 39;
        if(checkedResTemp.isChecked40())
            counter0 = 40;
        if(checkedResTemp.isChecked41())
            counter0 = 41;
        if(checkedResTemp.isChecked42())
            counter0 = 42;
        if(checkedResTemp.isChecked43())
            counter0 = 43;
        if(checkedResTemp.isChecked44())
            counter0 = 44;
        if(checkedResTemp.isChecked45())
            counter0 = 45;
        if(checkedResTemp.isChecked46())
            counter0 = 46;
        if(checkedResTemp.isChecked47())
            counter0 = 47;
        if(checkedResTemp.isChecked48())
            counter0 = 48;
        if(checkedResTemp.isChecked49())
            counter0 = 49;
        if(checkedResTemp.isChecked50())
            counter0 = 50;
        if(checkedResTemp.isChecked51())
            counter0 = 51;
        if(checkedResTemp.isChecked52())
            counter0 = 52;
        if(checkedResTemp.isChecked53())
            counter0 = 53;
        if(checkedResTemp.isChecked54())
            counter0 = 54;
        if(checkedResTemp.isChecked55())
            counter0 = 55;
        if(checkedResTemp.isChecked56())
            counter0 = 56;
        if(checkedResTemp.isChecked57())
            counter0 = 57;
        if(checkedResTemp.isChecked58())
            counter0 = 58;
        if(checkedResTemp.isChecked59())
            counter0 = 59;
        if(checkedResTemp.isChecked60())
            counter0 = 60;
        if(checkedResTemp.isChecked61())
            counter0 = 61;
        if(checkedResTemp.isChecked62())
            counter0 = 62;
        if(checkedResTemp.isChecked63())
            counter0 = 63;
        if(checkedResTemp.isChecked64())
            counter0 = 64;
        if(checkedResTemp.isChecked65())
            counter0 = 65;
        if(checkedResTemp.isChecked66())
            counter0 = 66;
        if(checkedResTemp.isChecked67())
            counter0 = 67;
        if(checkedResTemp.isChecked68())
            counter0 = 68;
        if(checkedResTemp.isChecked69())
            counter0 = 69;
        if(checkedResTemp.isChecked70())
            counter0 = 70;
        if(checkedResTemp.isChecked71())
            counter0 = 71;
        if(checkedResTemp.isChecked72())
            counter0 = 72;
        if(checkedResTemp.isChecked73())
            counter0 = 73;
        if(checkedResTemp.isChecked74())
            counter0 = 74;
        if(checkedResTemp.isChecked75())
            counter0 = 75;
        if(checkedResTemp.isChecked76())
            counter0 = 76;
        if(checkedResTemp.isChecked77())
            counter0 = 77;
        if(checkedResTemp.isChecked78())
            counter0 = 78;
        if(checkedResTemp.isChecked79())
            counter0 = 79;
        if(checkedResTemp.isChecked80())
            counter0 = 80;
        if(checkedResTemp.isChecked81())
            counter0 = 81;
        if(checkedResTemp.isChecked82())
            counter0 = 82;
        if(checkedResTemp.isChecked83())
            counter0 = 83;
        if(checkedResTemp.isChecked84())
            counter0 = 84;
        if(checkedResTemp.isChecked85())
            counter0 = 85;
        if(checkedResTemp.isChecked86())
            counter0 = 86;
        if(checkedResTemp.isChecked87())
            counter0 = 87;
        if(checkedResTemp.isChecked88())
            counter0 = 88;
        if(checkedResTemp.isChecked89())
            counter0 = 89;
        if(checkedResTemp.isChecked90())
            counter0 = 90;
        if(checkedResTemp.isChecked91())
            counter0 = 91;
        if(checkedResTemp.isChecked92())
            counter0 = 92;
        if(checkedResTemp.isChecked93())
            counter0 = 93;
        if(checkedResTemp.isChecked94())
            counter0 = 94;
        if(checkedResTemp.isChecked95())
            counter0 = 95;
        if(checkedResTemp.isChecked96())
            counter0 = 96;
        if(checkedResTemp.isChecked97())
            counter0 = 97;
        if(checkedResTemp.isChecked98())
            counter0 = 98;
        if(checkedResTemp.isChecked99())
            counter0 = 99;
        if(checkedResTemp.isChecked100())
            counter0 = 100;
        News news = new News();
        for (News item : NewsListTemp) {
            counter++;
            if(counter == counter0) {
//              找到勾選的新聞 並將此新聞做暫存
                news = item;
                break;
            }
        }
//      將勾選的新聞 傳到Thymeleaf
        model.addAttribute("news", news);
//      對勾選結果 進行初始化
        checkedResTemp = null;
//      跳轉到 新聞的詳細頁面
        return "news_zoom";
    }

    //////////////////////////////////////////////////
//  管理端的分類主頁
    @RequestMapping(value = "/category_home/{pageNum}")
    public String categoryHome(@PathVariable(value="pageNum",required=false) int pageNum, Model model) {
//      搜尋結果為零時 防止頁數為負
        if(pageNum == -1)
            pageNum = 0;
//      勾選結果的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋結果的初始化
        Page<Category> categoryPage = null;
//      搜尋所有分類
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
//      將搜尋結果 轉存為List
        CategoryListTemp = categoryPage.getContent();
//      將搜尋結果 傳到Thymeleaf
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到 分類的主頁
        return "category_home";
    }
    
//  管理端的分類主頁的勾選動作    
    @PostMapping("/category_home")
    public String CategoryHomeChecked(@ModelAttribute("checkedRes") CheckedRes checkedRes, Model model) {
//    勾選結果的統計
      checkedResTemp = checkedRes;
      checkedResTempCount = 0;
      if(checkedRes.isChecked1())
          checkedResTempCount++;
      if(checkedRes.isChecked2())
          checkedResTempCount++;
      if(checkedRes.isChecked3())
          checkedResTempCount++;
      if(checkedRes.isChecked4())
          checkedResTempCount++;
      if(checkedRes.isChecked5())
          checkedResTempCount++;
      if(checkedRes.isChecked6())
          checkedResTempCount++;
      if(checkedRes.isChecked7())
          checkedResTempCount++;
      if(checkedRes.isChecked8())
          checkedResTempCount++;
      if(checkedRes.isChecked9())
          checkedResTempCount++;
      if(checkedRes.isChecked10())
          checkedResTempCount++;
      if(checkedRes.isChecked11())
          checkedResTempCount++;
      if(checkedRes.isChecked12())
          checkedResTempCount++;
      if(checkedRes.isChecked13())
          checkedResTempCount++;
      if(checkedRes.isChecked14())
          checkedResTempCount++;
      if(checkedRes.isChecked15())
          checkedResTempCount++;
      if(checkedRes.isChecked16())
          checkedResTempCount++;
      if(checkedRes.isChecked17())
          checkedResTempCount++;
      if(checkedRes.isChecked18())
          checkedResTempCount++;
      if(checkedRes.isChecked19())
          checkedResTempCount++;
      if(checkedRes.isChecked20())
          checkedResTempCount++;
      if(checkedRes.isChecked21())
          checkedResTempCount++;
      if(checkedRes.isChecked22())
          checkedResTempCount++;
      if(checkedRes.isChecked23())
          checkedResTempCount++;
      if(checkedRes.isChecked24())
          checkedResTempCount++;
      if(checkedRes.isChecked25())
          checkedResTempCount++;
      if(checkedRes.isChecked26())
          checkedResTempCount++;
      if(checkedRes.isChecked27())
          checkedResTempCount++;
      if(checkedRes.isChecked28())
          checkedResTempCount++;
      if(checkedRes.isChecked29())
          checkedResTempCount++;
      if(checkedRes.isChecked30())
          checkedResTempCount++;
      if(checkedRes.isChecked31())
          checkedResTempCount++;
      if(checkedRes.isChecked32())
          checkedResTempCount++;
      if(checkedRes.isChecked33())
          checkedResTempCount++;
      if(checkedRes.isChecked34())
          checkedResTempCount++;
      if(checkedRes.isChecked35())
          checkedResTempCount++;
      if(checkedRes.isChecked36())
          checkedResTempCount++;
      if(checkedRes.isChecked37())
          checkedResTempCount++;
      if(checkedRes.isChecked38())
          checkedResTempCount++;
      if(checkedRes.isChecked39())
          checkedResTempCount++;
      if(checkedRes.isChecked40())
          checkedResTempCount++;
      if(checkedRes.isChecked41())
          checkedResTempCount++;
      if(checkedRes.isChecked42())
          checkedResTempCount++;
      if(checkedRes.isChecked43())
          checkedResTempCount++;
      if(checkedRes.isChecked44())
          checkedResTempCount++;
      if(checkedRes.isChecked45())
          checkedResTempCount++;
      if(checkedRes.isChecked46())
          checkedResTempCount++;
      if(checkedRes.isChecked47())
          checkedResTempCount++;
      if(checkedRes.isChecked48())
          checkedResTempCount++;
      if(checkedRes.isChecked49())
          checkedResTempCount++;
      if(checkedRes.isChecked50())
          checkedResTempCount++;
      if(checkedRes.isChecked51())
          checkedResTempCount++;
      if(checkedRes.isChecked52())
          checkedResTempCount++;
      if(checkedRes.isChecked53())
          checkedResTempCount++;
      if(checkedRes.isChecked54())
          checkedResTempCount++;
      if(checkedRes.isChecked55())
          checkedResTempCount++;
      if(checkedRes.isChecked56())
          checkedResTempCount++;
      if(checkedRes.isChecked57())
          checkedResTempCount++;
      if(checkedRes.isChecked58())
          checkedResTempCount++;
      if(checkedRes.isChecked59())
          checkedResTempCount++;
      if(checkedRes.isChecked60())
          checkedResTempCount++;
      if(checkedRes.isChecked61())
          checkedResTempCount++;
      if(checkedRes.isChecked62())
          checkedResTempCount++;
      if(checkedRes.isChecked63())
          checkedResTempCount++;
      if(checkedRes.isChecked64())
          checkedResTempCount++;
      if(checkedRes.isChecked65())
          checkedResTempCount++;
      if(checkedRes.isChecked66())
          checkedResTempCount++;
      if(checkedRes.isChecked67())
          checkedResTempCount++;
      if(checkedRes.isChecked68())
          checkedResTempCount++;
      if(checkedRes.isChecked69())
          checkedResTempCount++;
      if(checkedRes.isChecked70())
          checkedResTempCount++;
      if(checkedRes.isChecked71())
          checkedResTempCount++;
      if(checkedRes.isChecked72())
          checkedResTempCount++;
      if(checkedRes.isChecked73())
          checkedResTempCount++;
      if(checkedRes.isChecked74())
          checkedResTempCount++;
      if(checkedRes.isChecked75())
          checkedResTempCount++;
      if(checkedRes.isChecked76())
          checkedResTempCount++;
      if(checkedRes.isChecked77())
          checkedResTempCount++;
      if(checkedRes.isChecked78())
          checkedResTempCount++;
      if(checkedRes.isChecked79())
          checkedResTempCount++;
      if(checkedRes.isChecked80())
          checkedResTempCount++;
      if(checkedRes.isChecked81())
          checkedResTempCount++;
      if(checkedRes.isChecked82())
          checkedResTempCount++;
      if(checkedRes.isChecked83())
          checkedResTempCount++;
      if(checkedRes.isChecked84())
          checkedResTempCount++;
      if(checkedRes.isChecked85())
          checkedResTempCount++;
      if(checkedRes.isChecked86())
          checkedResTempCount++;
      if(checkedRes.isChecked87())
          checkedResTempCount++;
      if(checkedRes.isChecked88())
          checkedResTempCount++;
      if(checkedRes.isChecked89())
          checkedResTempCount++;
      if(checkedRes.isChecked90())
          checkedResTempCount++;
      if(checkedRes.isChecked91())
          checkedResTempCount++;
      if(checkedRes.isChecked92())
          checkedResTempCount++;
      if(checkedRes.isChecked93())
          checkedResTempCount++;
      if(checkedRes.isChecked94())
          checkedResTempCount++;
      if(checkedRes.isChecked95())
          checkedResTempCount++;
      if(checkedRes.isChecked96())
          checkedResTempCount++;
      if(checkedRes.isChecked97())
          checkedResTempCount++;
      if(checkedRes.isChecked98())
          checkedResTempCount++;
      if(checkedRes.isChecked99())
          checkedResTempCount++;
      if(checkedRes.isChecked100())
          checkedResTempCount++;
      
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋所有分類 再傳到Thymeleaf
        Page<Category> categoryPage = null;
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());
//      跳轉到 分類的主頁
        return "category_home";
            
    }

//  管理端的分類主頁的全選動作
    @RequestMapping(value = "/category_home_all_check")
    public String categoryHomeAllCheck(Model model) {
//      勾選結果的全選動作
        CheckedRes checkedRes = new CheckedRes();
        checkedRes.setChecked1(true);
        checkedRes.setChecked2(true);
        checkedRes.setChecked3(true);
        checkedRes.setChecked4(true);
        checkedRes.setChecked5(true);
        checkedRes.setChecked6(true);
        checkedRes.setChecked7(true);
        checkedRes.setChecked8(true);
        checkedRes.setChecked9(true);
        checkedRes.setChecked10(true);
        checkedRes.setChecked11(true);
        checkedRes.setChecked12(true);
        checkedRes.setChecked13(true);
        checkedRes.setChecked14(true);
        checkedRes.setChecked15(true);
        checkedRes.setChecked16(true);
        checkedRes.setChecked17(true);
        checkedRes.setChecked18(true);
        checkedRes.setChecked19(true);
        checkedRes.setChecked20(true);
        checkedRes.setChecked21(true);
        checkedRes.setChecked22(true);
        checkedRes.setChecked23(true);
        checkedRes.setChecked24(true);
        checkedRes.setChecked25(true);
        checkedRes.setChecked26(true);
        checkedRes.setChecked27(true);
        checkedRes.setChecked28(true);
        checkedRes.setChecked29(true);
        checkedRes.setChecked30(true);
        checkedRes.setChecked31(true);
        checkedRes.setChecked32(true);
        checkedRes.setChecked33(true);
        checkedRes.setChecked34(true);
        checkedRes.setChecked35(true);
        checkedRes.setChecked36(true);
        checkedRes.setChecked37(true);
        checkedRes.setChecked38(true);
        checkedRes.setChecked39(true);
        checkedRes.setChecked40(true);
        checkedRes.setChecked41(true);
        checkedRes.setChecked42(true);
        checkedRes.setChecked43(true);
        checkedRes.setChecked44(true);
        checkedRes.setChecked45(true);
        checkedRes.setChecked46(true);
        checkedRes.setChecked47(true);
        checkedRes.setChecked48(true);
        checkedRes.setChecked49(true);
        checkedRes.setChecked50(true);
        checkedRes.setChecked51(true);
        checkedRes.setChecked52(true);
        checkedRes.setChecked53(true);
        checkedRes.setChecked54(true);
        checkedRes.setChecked55(true);
        checkedRes.setChecked56(true);
        checkedRes.setChecked57(true);
        checkedRes.setChecked58(true);
        checkedRes.setChecked59(true);
        checkedRes.setChecked60(true);
        checkedRes.setChecked61(true);
        checkedRes.setChecked62(true);
        checkedRes.setChecked63(true);
        checkedRes.setChecked64(true);
        checkedRes.setChecked65(true);
        checkedRes.setChecked66(true);
        checkedRes.setChecked67(true);
        checkedRes.setChecked68(true);
        checkedRes.setChecked69(true);
        checkedRes.setChecked70(true);
        checkedRes.setChecked71(true);
        checkedRes.setChecked72(true);
        checkedRes.setChecked73(true);
        checkedRes.setChecked74(true);
        checkedRes.setChecked75(true);
        checkedRes.setChecked76(true);
        checkedRes.setChecked77(true);
        checkedRes.setChecked78(true);
        checkedRes.setChecked79(true);
        checkedRes.setChecked80(true);
        checkedRes.setChecked81(true);
        checkedRes.setChecked82(true);
        checkedRes.setChecked83(true);
        checkedRes.setChecked84(true);
        checkedRes.setChecked85(true);
        checkedRes.setChecked86(true);
        checkedRes.setChecked87(true);
        checkedRes.setChecked88(true);
        checkedRes.setChecked89(true);
        checkedRes.setChecked90(true);
        checkedRes.setChecked91(true);
        checkedRes.setChecked92(true);
        checkedRes.setChecked93(true);
        checkedRes.setChecked94(true);
        checkedRes.setChecked95(true);
        checkedRes.setChecked96(true);
        checkedRes.setChecked97(true);
        checkedRes.setChecked98(true);
        checkedRes.setChecked99(true);
        checkedRes.setChecked100(true);
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋所有分類 再傳到Thymeleaf
        Page<Category> categoryPage = null;
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        CategoryListTemp = categoryPage.getContent();
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());
//      跳轉到分類的主頁
        return "category_home";
    }
    
//  管理端的分類新增動作(填寫)
    @RequestMapping(value = "/category_add")
    public String categoryAdd(Model model) {
//      Thymeleaf的填入用變數的初始化
        Category categoryInput = new Category();
        model.addAttribute("categoryInput", categoryInput);
        model.addAttribute("error", "");
//      跳轉到分類的新增頁面
        return "category_add";
        
    }

//  管理端的分類新增動作(提交)    
    @PostMapping("/category_add")
    public String CategoryAdd(@ModelAttribute("categoryInput") Category category, Model model) {
//      確認 新增分類的動作是否正常
        CategoryAddResponse res = mainService.categoryAdd(category);
        if(res.getCode() != "200") {
            model.addAttribute("error", res.getMessage());
            return "category_add";
        }
//      勾選結果的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋所有的分類 再傳到Thymeleaf
        Page<Category> categoryPage = null;
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());
//      跳轉到分類的主頁
        return "category_home";
    }
    
//  管理端的分類預編輯動作(填寫)
    @RequestMapping(value = "/category_edit")
    public String CategoryEdit(Model model) {
//      Thymeleaf的糰入用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋所有分類 再傳到Thymeleaf
        Page<Category> categoryPage = null;
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());
//      未勾選 則返回分類的主頁
        if(checkedResTemp == null)
            return "category_home";
//      勾選2個以上 則返回分類的主頁
        if(checkedResTempCount >= 2)
            return "category_home";
//      統計 勾選結果
        Category category = new Category();
        int counter0 = 0;
        int counter = 0;
        if((checkedResTemp.isChecked1() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked1() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked1() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 1;
        if((checkedResTemp.isChecked2() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked2() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked2() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 2;
        if((checkedResTemp.isChecked3() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked3() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked3() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 3;
        if((checkedResTemp.isChecked4() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked4() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked4() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 4;
        if((checkedResTemp.isChecked5() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked5() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked5() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 5;
        if((checkedResTemp.isChecked6() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked6() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked6() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 6;
        if((checkedResTemp.isChecked7() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked7() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked7() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 7;
        if((checkedResTemp.isChecked8() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked8() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked8() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 8;
        if((checkedResTemp.isChecked9() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked9() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked9() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 9;
        if((checkedResTemp.isChecked10() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked10() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked10() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 10;
        if((checkedResTemp.isChecked11() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked11() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 11;
        if((checkedResTemp.isChecked12() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked12() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 12;
        if((checkedResTemp.isChecked13() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked13() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 13;
        if((checkedResTemp.isChecked14() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked14() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 14;
        if((checkedResTemp.isChecked15() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked15() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 15;
        if((checkedResTemp.isChecked16() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked16() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 16;
        if((checkedResTemp.isChecked17() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked17() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 17;
        if((checkedResTemp.isChecked18() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked18() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 18;
        if((checkedResTemp.isChecked19() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked19() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 19;
        if((checkedResTemp.isChecked20() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked20() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 20;
        if((checkedResTemp.isChecked21() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked21() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 21;
        if((checkedResTemp.isChecked22() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked22() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 22;
        if((checkedResTemp.isChecked23() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked23() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 23;
        if((checkedResTemp.isChecked24() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked24() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 24;
        if((checkedResTemp.isChecked25() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked25() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 25;
        if((checkedResTemp.isChecked26() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked26() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 26;
        if((checkedResTemp.isChecked27() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked27() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 27;
        if((checkedResTemp.isChecked28() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked28() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 28;
        if((checkedResTemp.isChecked29() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked29() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 29;
        if((checkedResTemp.isChecked30() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked30() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 30;
        if((checkedResTemp.isChecked31() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked31() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 31;
        if((checkedResTemp.isChecked32() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked32() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 32;
        if((checkedResTemp.isChecked33() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked33() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 33;
        if((checkedResTemp.isChecked34() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked34() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 34;
        if((checkedResTemp.isChecked35() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked35() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 35;
        if((checkedResTemp.isChecked36() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked36() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 36;
        if((checkedResTemp.isChecked37() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked37() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 37;
        if((checkedResTemp.isChecked38() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked38() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 38;
        if((checkedResTemp.isChecked39() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked39() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 39;
        if((checkedResTemp.isChecked40() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked40() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 40;
        if((checkedResTemp.isChecked41() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked41() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 41;
        if((checkedResTemp.isChecked42() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked42() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 42;
        if((checkedResTemp.isChecked43() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked43() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 43;
        if((checkedResTemp.isChecked44() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked44() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 44;
        if((checkedResTemp.isChecked45() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked45() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 45;
        if((checkedResTemp.isChecked46() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked46() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 46;
        if((checkedResTemp.isChecked47() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked47() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 47;
        if((checkedResTemp.isChecked48() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked48() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 48;
        if((checkedResTemp.isChecked49() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked49() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 49;
        if((checkedResTemp.isChecked50() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked50() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 50;
        if(checkedResTemp.isChecked51() && pageNumTemp == 0)
            counter0 = 51;
        if(checkedResTemp.isChecked52() && pageNumTemp == 0)
            counter0 = 52;
        if(checkedResTemp.isChecked53() && pageNumTemp == 0)
            counter0 = 53;
        if(checkedResTemp.isChecked54() && pageNumTemp == 0)
            counter0 = 54;
        if(checkedResTemp.isChecked55() && pageNumTemp == 0)
            counter0 = 55;
        if(checkedResTemp.isChecked56() && pageNumTemp == 0)
            counter0 = 56;
        if(checkedResTemp.isChecked57() && pageNumTemp == 0)
            counter0 = 57;
        if(checkedResTemp.isChecked58() && pageNumTemp == 0)
            counter0 = 58;
        if(checkedResTemp.isChecked59() && pageNumTemp == 0)
            counter0 = 59;
        if(checkedResTemp.isChecked60() && pageNumTemp == 0)
            counter0 = 60;
        if(checkedResTemp.isChecked61() && pageNumTemp == 0)
            counter0 = 61;
        if(checkedResTemp.isChecked62() && pageNumTemp == 0)
            counter0 = 62;
        if(checkedResTemp.isChecked63() && pageNumTemp == 0)
            counter0 = 63;
        if(checkedResTemp.isChecked64() && pageNumTemp == 0)
            counter0 = 64;
        if(checkedResTemp.isChecked65() && pageNumTemp == 0)
            counter0 = 65;
        if(checkedResTemp.isChecked66() && pageNumTemp == 0)
            counter0 = 66;
        if(checkedResTemp.isChecked67() && pageNumTemp == 0)
            counter0 = 67;
        if(checkedResTemp.isChecked68() && pageNumTemp == 0)
            counter0 = 68;
        if(checkedResTemp.isChecked69() && pageNumTemp == 0)
            counter0 = 69;
        if(checkedResTemp.isChecked70() && pageNumTemp == 0)
            counter0 = 70;
        if(checkedResTemp.isChecked71() && pageNumTemp == 0)
            counter0 = 71;
        if(checkedResTemp.isChecked72() && pageNumTemp == 0)
            counter0 = 72;
        if(checkedResTemp.isChecked73() && pageNumTemp == 0)
            counter0 = 73;
        if(checkedResTemp.isChecked74() && pageNumTemp == 0)
            counter0 = 74;
        if(checkedResTemp.isChecked75() && pageNumTemp == 0)
            counter0 = 75;
        if(checkedResTemp.isChecked76() && pageNumTemp == 0)
            counter0 = 76;
        if(checkedResTemp.isChecked77() && pageNumTemp == 0)
            counter0 = 77;
        if(checkedResTemp.isChecked78() && pageNumTemp == 0)
            counter0 = 78;
        if(checkedResTemp.isChecked79() && pageNumTemp == 0)
            counter0 = 79;
        if(checkedResTemp.isChecked80() && pageNumTemp == 0)
            counter0 = 80;
        if(checkedResTemp.isChecked81() && pageNumTemp == 0)
            counter0 = 81;
        if(checkedResTemp.isChecked82() && pageNumTemp == 0)
            counter0 = 82;
        if(checkedResTemp.isChecked83() && pageNumTemp == 0)
            counter0 = 83;
        if(checkedResTemp.isChecked84() && pageNumTemp == 0)
            counter0 = 84;
        if(checkedResTemp.isChecked85() && pageNumTemp == 0)
            counter0 = 85;
        if(checkedResTemp.isChecked86() && pageNumTemp == 0)
            counter0 = 86;
        if(checkedResTemp.isChecked87() && pageNumTemp == 0)
            counter0 = 87;
        if(checkedResTemp.isChecked88() && pageNumTemp == 0)
            counter0 = 88;
        if(checkedResTemp.isChecked89() && pageNumTemp == 0)
            counter0 = 89;
        if(checkedResTemp.isChecked90() && pageNumTemp == 0)
            counter0 = 90;
        if(checkedResTemp.isChecked91() && pageNumTemp == 0)
            counter0 = 91;
        if(checkedResTemp.isChecked92() && pageNumTemp == 0)
            counter0 = 92;
        if(checkedResTemp.isChecked93() && pageNumTemp == 0)
            counter0 = 93;
        if(checkedResTemp.isChecked94() && pageNumTemp == 0)
            counter0 = 94;
        if(checkedResTemp.isChecked95() && pageNumTemp == 0)
            counter0 = 95;
        if(checkedResTemp.isChecked96() && pageNumTemp == 0)
            counter0 = 96;
        if(checkedResTemp.isChecked97() && pageNumTemp == 0)
            counter0 = 97;
        if(checkedResTemp.isChecked98() && pageNumTemp == 0)
            counter0 = 98;
        if(checkedResTemp.isChecked99() && pageNumTemp == 0)
            counter0 = 99;
        if(checkedResTemp.isChecked100() && pageNumTemp == 0)
            counter0 = 100;
        for (Category item : CategoryListTemp) {
            counter++;
            if(counter == counter0) {
                category = item;
                categoryIdTemp = item.getId();
                oldCategoryTemp = item.getCategory();
                break;
            }
        }
//      將 選擇的分類 傳到Thymeleaf
        model.addAttribute("category", category);
//      錯誤訊息的初始化
        model.addAttribute("error", "");
//      勾選結果的初始化
        checkedResTemp = null;
//      跳轉到編輯分類的頁面
        return "category_edit";
    }

//  管理端的分類預編輯動作(提交)    
    @PostMapping("/category_edit")
    public String CategoryEditPost(@ModelAttribute("category") String category01, Model model) {
//      準備 編輯用的分類型別的變數
        Category category = new Category();
        category.setId(categoryIdTemp);
        category.setCategory(category01);
//      分類的id的初始化
        categoryIdTemp = 0;
//      進行 分類的編輯動作
        CategoryAddResponse res = mainService.categoryEdit(category);
        if(res.getCode() != "200") {
            model.addAttribute("category", category);
            model.addAttribute("error", res.getMessage());
            return "category_edit";
        }
//      更新 對應分類&子分類的新聞數量
        newsDao.updateNewsCategoryByOldCategory(category01, oldCategoryTemp);
        subCateogryDao.updateSubCategoryCategoryByOldCategory(category01, oldCategoryTemp);
//      勾選結果的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋所有分類 再傳到Thymeleaf
        Page<Category> categoryPage = null;
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());
//      跳轉到分類的主頁
        return "category_home";
    }
    
//  管理端的分類刪除動作
    @RequestMapping(value = "/category_delete")
    public String CategoryDelete(Model model) {
//        勾選結果的初始化
          CheckedRes checkedRes = new CheckedRes();
          model.addAttribute("checkedRes", checkedRes);
//        頁數的初始化
          int pageNum = 0;
          model.addAttribute("pageNum", pageNum);
          model.addAttribute("pageSize", pageSize);
//        搜尋所有的分類 再傳到Thymeleaf
          Page<Category> categoryPage = null;
          categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
          model.addAttribute("categoryPage", categoryPage);
          model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());
//        未勾選 則返回分類的主頁
          if(checkedResTemp == null)
              return "category_home";
//        統計 勾選結果
          int[] checked = new int[100];
          for(int i = 0; i < 100; i++) {
              checked[i] = 0;
          }
          if((checkedResTemp.isChecked1() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked1() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked1() && pageSize == 50 && pageNumTemp != 0))
              checked[0] = 1;
          if((checkedResTemp.isChecked2() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked2() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked2() && pageSize == 50 && pageNumTemp != 0))
              checked[1] = 1;
          if((checkedResTemp.isChecked3() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked3() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked3() && pageSize == 50 && pageNumTemp != 0))
              checked[2] = 1;
          if((checkedResTemp.isChecked4() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked4() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked4() && pageSize == 50 && pageNumTemp != 0))
              checked[3] = 1;
          if((checkedResTemp.isChecked5() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked5() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked5() && pageSize == 50 && pageNumTemp != 0))
              checked[4] = 1;
          if((checkedResTemp.isChecked6() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked6() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked6() && pageSize == 50 && pageNumTemp != 0))
              checked[5] = 1;
          if((checkedResTemp.isChecked7() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked7() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked7() && pageSize == 50 && pageNumTemp != 0))
              checked[6] = 1;
          if((checkedResTemp.isChecked8() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked8() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked8() && pageSize == 50 && pageNumTemp != 0))
              checked[7] = 1;
          if((checkedResTemp.isChecked9() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked9() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked9() && pageSize == 50 && pageNumTemp != 0))
              checked[8] = 1;
          if((checkedResTemp.isChecked10() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked10() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked10() && pageSize == 50 && pageNumTemp != 0))
              checked[9] = 1;
          if((checkedResTemp.isChecked11() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked11() && pageSize == 50 && pageNumTemp != 0))
              checked[10] = 1;
          if((checkedResTemp.isChecked12() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked12() && pageSize == 50 && pageNumTemp != 0))
              checked[11] = 1;
          if((checkedResTemp.isChecked13() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked13() && pageSize == 50 && pageNumTemp != 0))
              checked[12] = 1;
          if((checkedResTemp.isChecked14() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked14() && pageSize == 50 && pageNumTemp != 0))
              checked[13] = 1;
          if((checkedResTemp.isChecked15() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked15() && pageSize == 50 && pageNumTemp != 0))
              checked[14] = 1;
          if((checkedResTemp.isChecked16() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked16() && pageSize == 50 && pageNumTemp != 0))
              checked[15] = 1;
          if((checkedResTemp.isChecked17() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked17() && pageSize == 50 && pageNumTemp != 0))
              checked[16] = 1;
          if((checkedResTemp.isChecked18() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked18() && pageSize == 50 && pageNumTemp != 0))
              checked[17] = 1;
          if((checkedResTemp.isChecked19() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked19() && pageSize == 50 && pageNumTemp != 0))
              checked[18] = 1;
          if((checkedResTemp.isChecked20() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked20() && pageSize == 50 && pageNumTemp != 0))
              checked[19] = 1;
          if((checkedResTemp.isChecked21() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked21() && pageSize == 50 && pageNumTemp != 0))
              checked[20] = 1;
          if((checkedResTemp.isChecked22() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked22() && pageSize == 50 && pageNumTemp != 0))
              checked[21] = 1;
          if((checkedResTemp.isChecked23() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked23() && pageSize == 50 && pageNumTemp != 0))
              checked[22] = 1;
          if((checkedResTemp.isChecked24() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked24() && pageSize == 50 && pageNumTemp != 0))
              checked[23] = 1;
          if((checkedResTemp.isChecked25() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked25() && pageSize == 50 && pageNumTemp != 0))
              checked[24] = 1;
          if((checkedResTemp.isChecked26() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked26() && pageSize == 50 && pageNumTemp != 0))
              checked[25] = 1;
          if((checkedResTemp.isChecked27() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked27() && pageSize == 50 && pageNumTemp != 0))
              checked[26] = 1;
          if((checkedResTemp.isChecked28() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked28() && pageSize == 50 && pageNumTemp != 0))
              checked[27] = 1;
          if((checkedResTemp.isChecked29() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked29() && pageSize == 50 && pageNumTemp != 0))
              checked[28] = 1;
          if((checkedResTemp.isChecked30() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked30() && pageSize == 50 && pageNumTemp != 0))
              checked[29] = 1;
          if((checkedResTemp.isChecked31() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked31() && pageSize == 50 && pageNumTemp != 0))
              checked[30] = 1;
          if((checkedResTemp.isChecked32() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked32() && pageSize == 50 && pageNumTemp != 0))
              checked[31] = 1;
          if((checkedResTemp.isChecked33() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked33() && pageSize == 50 && pageNumTemp != 0))
              checked[32] = 1;
          if((checkedResTemp.isChecked34() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked34() && pageSize == 50 && pageNumTemp != 0))
              checked[33] = 1;
          if((checkedResTemp.isChecked35() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked35() && pageSize == 50 && pageNumTemp != 0))
              checked[34] = 1;
          if((checkedResTemp.isChecked36() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked36() && pageSize == 50 && pageNumTemp != 0))
              checked[35] = 1;
          if((checkedResTemp.isChecked37() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked37() && pageSize == 50 && pageNumTemp != 0))
              checked[36] = 1;
          if((checkedResTemp.isChecked38() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked38() && pageSize == 50 && pageNumTemp != 0))
              checked[37] = 1;
          if((checkedResTemp.isChecked39() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked39() && pageSize == 50 && pageNumTemp != 0))
              checked[38] = 1;
          if((checkedResTemp.isChecked40() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked40() && pageSize == 50 && pageNumTemp != 0))
              checked[39] = 1;
          if((checkedResTemp.isChecked41() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked41() && pageSize == 50 && pageNumTemp != 0))
              checked[40] = 1;
          if((checkedResTemp.isChecked42() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked42() && pageSize == 50 && pageNumTemp != 0))
              checked[41] = 1;
          if((checkedResTemp.isChecked43() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked43() && pageSize == 50 && pageNumTemp != 0))
              checked[42] = 1;
          if((checkedResTemp.isChecked44() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked44() && pageSize == 50 && pageNumTemp != 0))
              checked[43] = 1;
          if((checkedResTemp.isChecked45() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked45() && pageSize == 50 && pageNumTemp != 0))
              checked[44] = 1;
          if((checkedResTemp.isChecked46() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked46() && pageSize == 50 && pageNumTemp != 0))
              checked[45] = 1;
          if((checkedResTemp.isChecked47() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked47() && pageSize == 50 && pageNumTemp != 0))
              checked[46] = 1;
          if((checkedResTemp.isChecked48() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked48() && pageSize == 50 && pageNumTemp != 0))
              checked[47] = 1;
          if((checkedResTemp.isChecked49() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked49() && pageSize == 50 && pageNumTemp != 0))
              checked[48] = 1;
          if((checkedResTemp.isChecked50() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked50() && pageSize == 50 && pageNumTemp != 0))
              checked[49] = 1;
          if(checkedResTemp.isChecked51() && pageNumTemp == 0)
              checked[50] = 1;
          if(checkedResTemp.isChecked52() && pageNumTemp == 0)
              checked[51] = 1;
          if(checkedResTemp.isChecked53() && pageNumTemp == 0)
              checked[52] = 1;
          if(checkedResTemp.isChecked54() && pageNumTemp == 0)
              checked[53] = 1;
          if(checkedResTemp.isChecked55() && pageNumTemp == 0)
              checked[54] = 1;
          if(checkedResTemp.isChecked56() && pageNumTemp == 0)
              checked[55] = 1;
          if(checkedResTemp.isChecked57() && pageNumTemp == 0)
              checked[56] = 1;
          if(checkedResTemp.isChecked58() && pageNumTemp == 0)
              checked[57] = 1;
          if(checkedResTemp.isChecked59() && pageNumTemp == 0)
              checked[58] = 1;
          if(checkedResTemp.isChecked60() && pageNumTemp == 0)
              checked[59] = 1;
          if(checkedResTemp.isChecked61() && pageNumTemp == 0)
              checked[60] = 1;
          if(checkedResTemp.isChecked62() && pageNumTemp == 0)
              checked[61] = 1;
          if(checkedResTemp.isChecked63() && pageNumTemp == 0)
              checked[62] = 1;
          if(checkedResTemp.isChecked64() && pageNumTemp == 0)
              checked[63] = 1;
          if(checkedResTemp.isChecked65() && pageNumTemp == 0)
              checked[64] = 1;
          if(checkedResTemp.isChecked66() && pageNumTemp == 0)
              checked[65] = 1;
          if(checkedResTemp.isChecked67() && pageNumTemp == 0)
              checked[66] = 1;
          if(checkedResTemp.isChecked68() && pageNumTemp == 0)
              checked[67] = 1;
          if(checkedResTemp.isChecked69() && pageNumTemp == 0)
              checked[68] = 1;
          if(checkedResTemp.isChecked70() && pageNumTemp == 0)
              checked[69] = 1;
          if(checkedResTemp.isChecked71() && pageNumTemp == 0)
              checked[70] = 1;
          if(checkedResTemp.isChecked72() && pageNumTemp == 0)
              checked[71] = 1;
          if(checkedResTemp.isChecked73() && pageNumTemp == 0)
              checked[72] = 1;
          if(checkedResTemp.isChecked74() && pageNumTemp == 0)
              checked[73] = 1;
          if(checkedResTemp.isChecked75() && pageNumTemp == 0)
              checked[74] = 1;
          if(checkedResTemp.isChecked76() && pageNumTemp == 0)
              checked[75] = 1;
          if(checkedResTemp.isChecked77() && pageNumTemp == 0)
              checked[76] = 1;
          if(checkedResTemp.isChecked78() && pageNumTemp == 0)
              checked[77] = 1;
          if(checkedResTemp.isChecked79() && pageNumTemp == 0)
              checked[78] = 1;
          if(checkedResTemp.isChecked80() && pageNumTemp == 0)
              checked[79] = 1;
          if(checkedResTemp.isChecked81() && pageNumTemp == 0)
              checked[80] = 1;
          if(checkedResTemp.isChecked82() && pageNumTemp == 0)
              checked[81] = 1;
          if(checkedResTemp.isChecked83() && pageNumTemp == 0)
              checked[82] = 1;
          if(checkedResTemp.isChecked84() && pageNumTemp == 0)
              checked[83] = 1;
          if(checkedResTemp.isChecked85() && pageNumTemp == 0)
              checked[84] = 1;
          if(checkedResTemp.isChecked86() && pageNumTemp == 0)
              checked[85] = 1;
          if(checkedResTemp.isChecked87() && pageNumTemp == 0)
              checked[86] = 1;
          if(checkedResTemp.isChecked88() && pageNumTemp == 0)
              checked[87] = 1;
          if(checkedResTemp.isChecked89() && pageNumTemp == 0)
              checked[88] = 1;
          if(checkedResTemp.isChecked90() && pageNumTemp == 0)
              checked[89] = 1;
          if(checkedResTemp.isChecked91() && pageNumTemp == 0)
              checked[90] = 1;
          if(checkedResTemp.isChecked92() && pageNumTemp == 0)
              checked[91] = 1;
          if(checkedResTemp.isChecked93() && pageNumTemp == 0)
              checked[92] = 1;
          if(checkedResTemp.isChecked94() && pageNumTemp == 0)
              checked[93] = 1;
          if(checkedResTemp.isChecked95() && pageNumTemp == 0)
              checked[94] = 1;
          if(checkedResTemp.isChecked96() && pageNumTemp == 0)
              checked[95] = 1;
          if(checkedResTemp.isChecked97() && pageNumTemp == 0)
              checked[96] = 1;
          if(checkedResTemp.isChecked98() && pageNumTemp == 0)
              checked[97] = 1;
          if(checkedResTemp.isChecked99() && pageNumTemp == 0)
              checked[98] = 1;
          if(checkedResTemp.isChecked100() && pageNumTemp == 0)
              checked[99] = 1;
//        找到勾選的分類 再做刪除
          int counter = 0;
          for (Category item : CategoryListTemp) {
              if(checked[counter] == 1 && item.getNewsCount() == 0) {

                  mainService.categoryDelete(item.getId());
                     
              }
              counter++;
          }

          categoryPage = null;
//        搜尋 刪除後的所有分類 再傳到Thymeleaf
          categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
          model.addAttribute("categoryPage", categoryPage);
          model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());
//        勾選結果的初始化
          checkedResTemp = null;
//        跳轉到分類的主頁
          return "category_home";
    }

    //////////////////////////////////////////////////
//  管理端的子分類主頁
    @RequestMapping(value = "/sub_category_home/{pageNum}")
    public String subCategoryHome(@PathVariable(value="pageNum",required=false) int pageNum, Model model) {
//      搜尋結果為零時 防止頁數為負
        if(pageNum == -1)
            pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      找到所有分類 再傳到Thymeleaf
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋完子分類 再傳到Thymeleaf        
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
        model.addAttribute("subCategoryPage", subCategoryPage);

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到子分類的主頁
        return "sub_category_home";
    }

//  管理端的子分類主頁的勾選動作 
    @PostMapping("/sub_category_home")
    public String SubCategoryHomeChecked(@ModelAttribute("checkedRes") CheckedRes checkedRes, Model model) {
//      勾選結果的統計
        checkedResTemp = checkedRes;
        checkedResTempCount = 0;
        if(checkedRes.isChecked1())
            checkedResTempCount++;
        if(checkedRes.isChecked2())
            checkedResTempCount++;
        if(checkedRes.isChecked3())
            checkedResTempCount++;
        if(checkedRes.isChecked4())
            checkedResTempCount++;
        if(checkedRes.isChecked5())
            checkedResTempCount++;
        if(checkedRes.isChecked6())
            checkedResTempCount++;
        if(checkedRes.isChecked7())
            checkedResTempCount++;
        if(checkedRes.isChecked8())
            checkedResTempCount++;
        if(checkedRes.isChecked9())
            checkedResTempCount++;
        if(checkedRes.isChecked10())
            checkedResTempCount++;
        if(checkedRes.isChecked11())
            checkedResTempCount++;
        if(checkedRes.isChecked12())
            checkedResTempCount++;
        if(checkedRes.isChecked13())
            checkedResTempCount++;
        if(checkedRes.isChecked14())
            checkedResTempCount++;
        if(checkedRes.isChecked15())
            checkedResTempCount++;
        if(checkedRes.isChecked16())
            checkedResTempCount++;
        if(checkedRes.isChecked17())
            checkedResTempCount++;
        if(checkedRes.isChecked18())
            checkedResTempCount++;
        if(checkedRes.isChecked19())
            checkedResTempCount++;
        if(checkedRes.isChecked20())
            checkedResTempCount++;
        if(checkedRes.isChecked21())
            checkedResTempCount++;
        if(checkedRes.isChecked22())
            checkedResTempCount++;
        if(checkedRes.isChecked23())
            checkedResTempCount++;
        if(checkedRes.isChecked24())
            checkedResTempCount++;
        if(checkedRes.isChecked25())
            checkedResTempCount++;
        if(checkedRes.isChecked26())
            checkedResTempCount++;
        if(checkedRes.isChecked27())
            checkedResTempCount++;
        if(checkedRes.isChecked28())
            checkedResTempCount++;
        if(checkedRes.isChecked29())
            checkedResTempCount++;
        if(checkedRes.isChecked30())
            checkedResTempCount++;
        if(checkedRes.isChecked31())
            checkedResTempCount++;
        if(checkedRes.isChecked32())
            checkedResTempCount++;
        if(checkedRes.isChecked33())
            checkedResTempCount++;
        if(checkedRes.isChecked34())
            checkedResTempCount++;
        if(checkedRes.isChecked35())
            checkedResTempCount++;
        if(checkedRes.isChecked36())
            checkedResTempCount++;
        if(checkedRes.isChecked37())
            checkedResTempCount++;
        if(checkedRes.isChecked38())
            checkedResTempCount++;
        if(checkedRes.isChecked39())
            checkedResTempCount++;
        if(checkedRes.isChecked40())
            checkedResTempCount++;
        if(checkedRes.isChecked41())
            checkedResTempCount++;
        if(checkedRes.isChecked42())
            checkedResTempCount++;
        if(checkedRes.isChecked43())
            checkedResTempCount++;
        if(checkedRes.isChecked44())
            checkedResTempCount++;
        if(checkedRes.isChecked45())
            checkedResTempCount++;
        if(checkedRes.isChecked46())
            checkedResTempCount++;
        if(checkedRes.isChecked47())
            checkedResTempCount++;
        if(checkedRes.isChecked48())
            checkedResTempCount++;
        if(checkedRes.isChecked49())
            checkedResTempCount++;
        if(checkedRes.isChecked50())
            checkedResTempCount++;
        if(checkedRes.isChecked51())
            checkedResTempCount++;
        if(checkedRes.isChecked52())
            checkedResTempCount++;
        if(checkedRes.isChecked53())
            checkedResTempCount++;
        if(checkedRes.isChecked54())
            checkedResTempCount++;
        if(checkedRes.isChecked55())
            checkedResTempCount++;
        if(checkedRes.isChecked56())
            checkedResTempCount++;
        if(checkedRes.isChecked57())
            checkedResTempCount++;
        if(checkedRes.isChecked58())
            checkedResTempCount++;
        if(checkedRes.isChecked59())
            checkedResTempCount++;
        if(checkedRes.isChecked60())
            checkedResTempCount++;
        if(checkedRes.isChecked61())
            checkedResTempCount++;
        if(checkedRes.isChecked62())
            checkedResTempCount++;
        if(checkedRes.isChecked63())
            checkedResTempCount++;
        if(checkedRes.isChecked64())
            checkedResTempCount++;
        if(checkedRes.isChecked65())
            checkedResTempCount++;
        if(checkedRes.isChecked66())
            checkedResTempCount++;
        if(checkedRes.isChecked67())
            checkedResTempCount++;
        if(checkedRes.isChecked68())
            checkedResTempCount++;
        if(checkedRes.isChecked69())
            checkedResTempCount++;
        if(checkedRes.isChecked70())
            checkedResTempCount++;
        if(checkedRes.isChecked71())
            checkedResTempCount++;
        if(checkedRes.isChecked72())
            checkedResTempCount++;
        if(checkedRes.isChecked73())
            checkedResTempCount++;
        if(checkedRes.isChecked74())
            checkedResTempCount++;
        if(checkedRes.isChecked75())
            checkedResTempCount++;
        if(checkedRes.isChecked76())
            checkedResTempCount++;
        if(checkedRes.isChecked77())
            checkedResTempCount++;
        if(checkedRes.isChecked78())
            checkedResTempCount++;
        if(checkedRes.isChecked79())
            checkedResTempCount++;
        if(checkedRes.isChecked80())
            checkedResTempCount++;
        if(checkedRes.isChecked81())
            checkedResTempCount++;
        if(checkedRes.isChecked82())
            checkedResTempCount++;
        if(checkedRes.isChecked83())
            checkedResTempCount++;
        if(checkedRes.isChecked84())
            checkedResTempCount++;
        if(checkedRes.isChecked85())
            checkedResTempCount++;
        if(checkedRes.isChecked86())
            checkedResTempCount++;
        if(checkedRes.isChecked87())
            checkedResTempCount++;
        if(checkedRes.isChecked88())
            checkedResTempCount++;
        if(checkedRes.isChecked89())
            checkedResTempCount++;
        if(checkedRes.isChecked90())
            checkedResTempCount++;
        if(checkedRes.isChecked91())
            checkedResTempCount++;
        if(checkedRes.isChecked92())
            checkedResTempCount++;
        if(checkedRes.isChecked93())
            checkedResTempCount++;
        if(checkedRes.isChecked94())
            checkedResTempCount++;
        if(checkedRes.isChecked95())
            checkedResTempCount++;
        if(checkedRes.isChecked96())
            checkedResTempCount++;
        if(checkedRes.isChecked97())
            checkedResTempCount++;
        if(checkedRes.isChecked98())
            checkedResTempCount++;
        if(checkedRes.isChecked99())
            checkedResTempCount++;
        if(checkedRes.isChecked100())
            checkedResTempCount++;
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      找到所有分類 再傳到Thymeleaf
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋子分類 再將結果傳到Thymeleaf
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
//      跳轉到子分類的主頁
        return "sub_category_home";
    }

//  管理端的子分類主頁的全選動作
    @RequestMapping(value = "/sub_category_home_all_check")
    public String subCategoryHomeAllCheck(Model model) {
//      勾選結果的全選動作
        CheckedRes checkedRes = new CheckedRes();
        checkedRes.setChecked1(true);
        checkedRes.setChecked2(true);
        checkedRes.setChecked3(true);
        checkedRes.setChecked4(true);
        checkedRes.setChecked5(true);
        checkedRes.setChecked6(true);
        checkedRes.setChecked7(true);
        checkedRes.setChecked8(true);
        checkedRes.setChecked9(true);
        checkedRes.setChecked10(true);
        checkedRes.setChecked11(true);
        checkedRes.setChecked12(true);
        checkedRes.setChecked13(true);
        checkedRes.setChecked14(true);
        checkedRes.setChecked15(true);
        checkedRes.setChecked16(true);
        checkedRes.setChecked17(true);
        checkedRes.setChecked18(true);
        checkedRes.setChecked19(true);
        checkedRes.setChecked20(true);
        checkedRes.setChecked21(true);
        checkedRes.setChecked22(true);
        checkedRes.setChecked23(true);
        checkedRes.setChecked24(true);
        checkedRes.setChecked25(true);
        checkedRes.setChecked26(true);
        checkedRes.setChecked27(true);
        checkedRes.setChecked28(true);
        checkedRes.setChecked29(true);
        checkedRes.setChecked30(true);
        checkedRes.setChecked31(true);
        checkedRes.setChecked32(true);
        checkedRes.setChecked33(true);
        checkedRes.setChecked34(true);
        checkedRes.setChecked35(true);
        checkedRes.setChecked36(true);
        checkedRes.setChecked37(true);
        checkedRes.setChecked38(true);
        checkedRes.setChecked39(true);
        checkedRes.setChecked40(true);
        checkedRes.setChecked41(true);
        checkedRes.setChecked42(true);
        checkedRes.setChecked43(true);
        checkedRes.setChecked44(true);
        checkedRes.setChecked45(true);
        checkedRes.setChecked46(true);
        checkedRes.setChecked47(true);
        checkedRes.setChecked48(true);
        checkedRes.setChecked49(true);
        checkedRes.setChecked50(true);
        checkedRes.setChecked51(true);
        checkedRes.setChecked52(true);
        checkedRes.setChecked53(true);
        checkedRes.setChecked54(true);
        checkedRes.setChecked55(true);
        checkedRes.setChecked56(true);
        checkedRes.setChecked57(true);
        checkedRes.setChecked58(true);
        checkedRes.setChecked59(true);
        checkedRes.setChecked60(true);
        checkedRes.setChecked61(true);
        checkedRes.setChecked62(true);
        checkedRes.setChecked63(true);
        checkedRes.setChecked64(true);
        checkedRes.setChecked65(true);
        checkedRes.setChecked66(true);
        checkedRes.setChecked67(true);
        checkedRes.setChecked68(true);
        checkedRes.setChecked69(true);
        checkedRes.setChecked70(true);
        checkedRes.setChecked71(true);
        checkedRes.setChecked72(true);
        checkedRes.setChecked73(true);
        checkedRes.setChecked74(true);
        checkedRes.setChecked75(true);
        checkedRes.setChecked76(true);
        checkedRes.setChecked77(true);
        checkedRes.setChecked78(true);
        checkedRes.setChecked79(true);
        checkedRes.setChecked80(true);
        checkedRes.setChecked81(true);
        checkedRes.setChecked82(true);
        checkedRes.setChecked83(true);
        checkedRes.setChecked84(true);
        checkedRes.setChecked85(true);
        checkedRes.setChecked86(true);
        checkedRes.setChecked87(true);
        checkedRes.setChecked88(true);
        checkedRes.setChecked89(true);
        checkedRes.setChecked90(true);
        checkedRes.setChecked91(true);
        checkedRes.setChecked92(true);
        checkedRes.setChecked93(true);
        checkedRes.setChecked94(true);
        checkedRes.setChecked95(true);
        checkedRes.setChecked96(true);
        checkedRes.setChecked97(true);
        checkedRes.setChecked98(true);
        checkedRes.setChecked99(true);
        checkedRes.setChecked100(true);
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      準備 選擇用的分類列表
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋子分類 再傳到Thymeleaf
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
        model.addAttribute("subCategoryPage", subCategoryPage);
//      跳轉到 子分類的主頁
        return "sub_category_home";
    }

//  管理端的子分類主頁(用分類搜尋)    
    @PostMapping("/sub_category_home_search_category")
    public String SubCategoryHomeSearchCategory(@ModelAttribute("subCategory") SubCategory subCategory, Model model) {
//        準備 選擇用的分類列表
          List<Category> res02 = categoryDao.findAll();
          List<String> categoryList = new ArrayList<>();
          for (Category item : res02) {
              categoryList.add(item.getCategory());
          }
          model.addAttribute("categoryList", categoryList);
          model.addAttribute("subCategory", subCategory);
//      勾選結果的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      用選擇的分類搜尋子分類 再將結果傳到Thymeleaf
        Page<SubCategory> subCategoryPage=mainService.findSubCategoryPageByCategory(sortDescFlag, pageNum, pageSize, subCategory.getCategory());
        subCategoryLastSearch = 1;
        subCategorylastKeyWordStr = subCategory.getCategory();
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
//      跳轉到子分類的主頁 
        return "sub_category_home";
        
    }
    
//  管理端的子分類新增動作(填寫)
    @RequestMapping(value = "/sub_category_add")
    public String subCategoryAdd(Model model) {
//      輸入用變數的初始化
        SubCategory subCategoryInput = new SubCategory();
        model.addAttribute("subCategoryInput", subCategoryInput);
        model.addAttribute("error", "");
//      準備 選擇用的分類列表
        List<Category> res = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
//      跳轉到新增子分類的頁面
        return "sub_category_add";
    }

//  管理端的子分類新增動作(提交)  
    @PostMapping("/sub_category_add")
    public String subCategoryAdd(@ModelAttribute("subCategoryInput") SubCategory subCategoryInput, Model model) {
//      勾選結果的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      準備 選擇用的分類列表
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      新增子分類 並確認是否成功
        SubCategoryAddResponse res = mainService.subCategoryAdd(subCategoryInput);
        if(res.getCode() != "200") {
            model.addAttribute("error", res.getMessage());
            return "sub_category_add";
        }
//      搜尋子分類 再將結果傳到Thymeleaf
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
//      跳轉到子分類的主頁
        return "sub_category_home";
    }

//  管理端的子分類編輯動作(填寫)
    @RequestMapping(value = "/sub_category_edit")
    public String subCategoryEdit(Model model) {
//      準備 選擇用的分類列表
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
//      勾選結果的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      輸入用變數的初始化
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋子分類 再傳到Thymeleaf
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
//      未勾選 則返回子分類主頁
        if(checkedResTemp == null)
            return "sub_category_home";
//      勾選2個以上 則返回子分類主頁
        if(checkedResTempCount >= 2)
            return "sub_category_home";
//      統計 勾選結果
        int counter0 = 0;
        int counter = 0;
        if((checkedResTemp.isChecked1() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked1() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked1() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 1;
        if((checkedResTemp.isChecked2() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked2() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked2() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 2;
        if((checkedResTemp.isChecked3() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked3() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked3() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 3;
        if((checkedResTemp.isChecked4() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked4() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked4() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 4;
        if((checkedResTemp.isChecked5() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked5() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked5() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 5;
        if((checkedResTemp.isChecked6() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked6() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked6() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 6;
        if((checkedResTemp.isChecked7() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked7() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked7() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 7;
        if((checkedResTemp.isChecked8() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked8() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked8() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 8;
        if((checkedResTemp.isChecked9() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked9() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked9() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 9;
        if((checkedResTemp.isChecked10() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked10() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked10() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 10;
        if((checkedResTemp.isChecked11() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked11() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 11;
        if((checkedResTemp.isChecked12() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked12() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 12;
        if((checkedResTemp.isChecked13() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked13() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 13;
        if((checkedResTemp.isChecked14() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked14() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 14;
        if((checkedResTemp.isChecked15() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked15() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 15;
        if((checkedResTemp.isChecked16() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked16() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 16;
        if((checkedResTemp.isChecked17() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked17() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 17;
        if((checkedResTemp.isChecked18() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked18() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 18;
        if((checkedResTemp.isChecked19() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked19() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 19;
        if((checkedResTemp.isChecked20() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked20() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 20;
        if((checkedResTemp.isChecked21() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked21() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 21;
        if((checkedResTemp.isChecked22() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked22() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 22;
        if((checkedResTemp.isChecked23() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked23() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 23;
        if((checkedResTemp.isChecked24() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked24() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 24;
        if((checkedResTemp.isChecked25() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked25() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 25;
        if((checkedResTemp.isChecked26() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked26() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 26;
        if((checkedResTemp.isChecked27() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked27() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 27;
        if((checkedResTemp.isChecked28() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked28() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 28;
        if((checkedResTemp.isChecked29() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked29() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 29;
        if((checkedResTemp.isChecked30() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked30() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 30;
        if((checkedResTemp.isChecked31() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked31() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 31;
        if((checkedResTemp.isChecked32() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked32() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 32;
        if((checkedResTemp.isChecked33() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked33() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 33;
        if((checkedResTemp.isChecked34() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked34() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 34;
        if((checkedResTemp.isChecked35() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked35() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 35;
        if((checkedResTemp.isChecked36() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked36() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 36;
        if((checkedResTemp.isChecked37() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked37() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 37;
        if((checkedResTemp.isChecked38() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked38() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 38;
        if((checkedResTemp.isChecked39() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked39() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 39;
        if((checkedResTemp.isChecked40() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked40() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 40;
        if((checkedResTemp.isChecked41() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked41() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 41;
        if((checkedResTemp.isChecked42() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked42() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 42;
        if((checkedResTemp.isChecked43() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked43() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 43;
        if((checkedResTemp.isChecked44() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked44() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 44;
        if((checkedResTemp.isChecked45() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked45() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 45;
        if((checkedResTemp.isChecked46() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked46() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 46;
        if((checkedResTemp.isChecked47() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked47() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 47;
        if((checkedResTemp.isChecked48() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked48() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 48;
        if((checkedResTemp.isChecked49() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked49() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 49;
        if((checkedResTemp.isChecked50() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked50() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 50;
        if(checkedResTemp.isChecked51() && pageNumTemp == 0)
            counter0 = 51;
        if(checkedResTemp.isChecked52() && pageNumTemp == 0)
            counter0 = 52;
        if(checkedResTemp.isChecked53() && pageNumTemp == 0)
            counter0 = 53;
        if(checkedResTemp.isChecked54() && pageNumTemp == 0)
            counter0 = 54;
        if(checkedResTemp.isChecked55() && pageNumTemp == 0)
            counter0 = 55;
        if(checkedResTemp.isChecked56() && pageNumTemp == 0)
            counter0 = 56;
        if(checkedResTemp.isChecked57() && pageNumTemp == 0)
            counter0 = 57;
        if(checkedResTemp.isChecked58() && pageNumTemp == 0)
            counter0 = 58;
        if(checkedResTemp.isChecked59() && pageNumTemp == 0)
            counter0 = 59;
        if(checkedResTemp.isChecked60() && pageNumTemp == 0)
            counter0 = 60;
        if(checkedResTemp.isChecked61() && pageNumTemp == 0)
            counter0 = 61;
        if(checkedResTemp.isChecked62() && pageNumTemp == 0)
            counter0 = 62;
        if(checkedResTemp.isChecked63() && pageNumTemp == 0)
            counter0 = 63;
        if(checkedResTemp.isChecked64() && pageNumTemp == 0)
            counter0 = 64;
        if(checkedResTemp.isChecked65() && pageNumTemp == 0)
            counter0 = 65;
        if(checkedResTemp.isChecked66() && pageNumTemp == 0)
            counter0 = 66;
        if(checkedResTemp.isChecked67() && pageNumTemp == 0)
            counter0 = 67;
        if(checkedResTemp.isChecked68() && pageNumTemp == 0)
            counter0 = 68;
        if(checkedResTemp.isChecked69() && pageNumTemp == 0)
            counter0 = 69;
        if(checkedResTemp.isChecked70() && pageNumTemp == 0)
            counter0 = 70;
        if(checkedResTemp.isChecked71() && pageNumTemp == 0)
            counter0 = 71;
        if(checkedResTemp.isChecked72() && pageNumTemp == 0)
            counter0 = 72;
        if(checkedResTemp.isChecked73() && pageNumTemp == 0)
            counter0 = 73;
        if(checkedResTemp.isChecked74() && pageNumTemp == 0)
            counter0 = 74;
        if(checkedResTemp.isChecked75() && pageNumTemp == 0)
            counter0 = 75;
        if(checkedResTemp.isChecked76() && pageNumTemp == 0)
            counter0 = 76;
        if(checkedResTemp.isChecked77() && pageNumTemp == 0)
            counter0 = 77;
        if(checkedResTemp.isChecked78() && pageNumTemp == 0)
            counter0 = 78;
        if(checkedResTemp.isChecked79() && pageNumTemp == 0)
            counter0 = 79;
        if(checkedResTemp.isChecked80() && pageNumTemp == 0)
            counter0 = 80;
        if(checkedResTemp.isChecked81() && pageNumTemp == 0)
            counter0 = 81;
        if(checkedResTemp.isChecked82() && pageNumTemp == 0)
            counter0 = 82;
        if(checkedResTemp.isChecked83() && pageNumTemp == 0)
            counter0 = 83;
        if(checkedResTemp.isChecked84() && pageNumTemp == 0)
            counter0 = 84;
        if(checkedResTemp.isChecked85() && pageNumTemp == 0)
            counter0 = 85;
        if(checkedResTemp.isChecked86() && pageNumTemp == 0)
            counter0 = 86;
        if(checkedResTemp.isChecked87() && pageNumTemp == 0)
            counter0 = 87;
        if(checkedResTemp.isChecked88() && pageNumTemp == 0)
            counter0 = 88;
        if(checkedResTemp.isChecked89() && pageNumTemp == 0)
            counter0 = 89;
        if(checkedResTemp.isChecked90() && pageNumTemp == 0)
            counter0 = 90;
        if(checkedResTemp.isChecked91() && pageNumTemp == 0)
            counter0 = 91;
        if(checkedResTemp.isChecked92() && pageNumTemp == 0)
            counter0 = 92;
        if(checkedResTemp.isChecked93() && pageNumTemp == 0)
            counter0 = 93;
        if(checkedResTemp.isChecked94() && pageNumTemp == 0)
            counter0 = 94;
        if(checkedResTemp.isChecked95() && pageNumTemp == 0)
            counter0 = 95;
        if(checkedResTemp.isChecked96() && pageNumTemp == 0)
            counter0 = 96;
        if(checkedResTemp.isChecked97() && pageNumTemp == 0)
            counter0 = 97;
        if(checkedResTemp.isChecked98() && pageNumTemp == 0)
            counter0 = 98;
        if(checkedResTemp.isChecked99() && pageNumTemp == 0)
            counter0 = 99;
        if(checkedResTemp.isChecked100() && pageNumTemp == 0)
            counter0 = 100;
        SubCategory subCategoryInput = new SubCategory();
        for (SubCategory item : SubCategoryListTemp) {
            counter++;
            if(counter == counter0) {
                subCategoryInput = item;
                subCategoryIdTemp = item.getId();
                oldSubCategoryTemp = item.getSubCategory();
                break;
            }
        }
        model.addAttribute("subCategoryInput", subCategoryInput);
        model.addAttribute("error", "");
//      勾選結果的初始化
        checkedResTemp = null;
//      跳轉到編輯子分類的頁面
        return "sub_category_edit";
    }

//  管理端的子分類編輯動作(提交)    
    @PostMapping("/sub_category_edit")
    public String subCategoryEditPost(@ModelAttribute("subCategoryInput") SubCategory subCategoryInput, Model model) {
//      勾選結果的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        int pageNum = 0;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      準備 選擇用的分類列表
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      編輯子分類 並確認是否成功
        subCategoryInput.setId(subCategoryIdTemp);
        SubCategoryAddResponse res = mainService.subCategoryEdit(subCategoryInput);
        if(res.getCode() != "200") {
            model.addAttribute("subCategoryInput", subCategoryInput);
            model.addAttribute("error", res.getMessage());
            return "sub_category_edit";
        }
//      更新 以前發布的新聞資料
        newsDao.updateNewsSubCategoryByOldSubCategory(subCategoryInput.getSubCategory(), oldSubCategoryTemp);
//      搜尋子分類 再傳到Thymeleaf
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
//      跳轉到子分類的主頁
        return "sub_category_home";
    }

//  管理端的子分類刪除動作
    @RequestMapping(value = "/sub_category_delete")
    public String SubCategoryDelete(Model model) {
//        勾選結果的初始化
          CheckedRes checkedRes = new CheckedRes();
          model.addAttribute("checkedRes", checkedRes);
//        頁數的初始化
          int pageNum = 0;
          model.addAttribute("pageNum", pageNum);
          model.addAttribute("pageSize", pageSize);
//        準備 選擇用的分類列表
          List<Category> res02 = categoryDao.findAll();
          List<String> categoryList = new ArrayList<>();
          for (Category item : res02) {
              categoryList.add(item.getCategory());
          }
          model.addAttribute("categoryList", categoryList);
          SubCategory subCategory = new SubCategory();
          model.addAttribute("subCategory", subCategory);
//        搜尋子分類 再傳到Thymeleaf
          Page<SubCategory> subCategoryPage = null;
//        預設搜尋
          if(subCategoryLastSearch == 0) {
              subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
          }
//       分類搜尋
          if(subCategoryLastSearch == 1) {
              subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
          }
          SubCategoryListTemp = subCategoryPage.getContent();
          model.addAttribute("subCategoryPage", subCategoryPage);
          model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
//        未勾選 則返回子分類的主頁
          if(checkedResTemp == null)
              return "sub_category_home";
//        統計 勾選結果 再做刪除
          int[] checked = new int[100];
          for(int i = 0; i < 100; i++) {
              checked[i] = 0;
          }
          if((checkedResTemp.isChecked1() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked1() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked1() && pageSize == 50 && pageNumTemp != 0))
              checked[0] = 1;
          if((checkedResTemp.isChecked2() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked2() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked2() && pageSize == 50 && pageNumTemp != 0))
              checked[1] = 1;
          if((checkedResTemp.isChecked3() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked3() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked3() && pageSize == 50 && pageNumTemp != 0))
              checked[2] = 1;
          if((checkedResTemp.isChecked4() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked4() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked4() && pageSize == 50 && pageNumTemp != 0))
              checked[3] = 1;
          if((checkedResTemp.isChecked5() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked5() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked5() && pageSize == 50 && pageNumTemp != 0))
              checked[4] = 1;
          if((checkedResTemp.isChecked6() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked6() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked6() && pageSize == 50 && pageNumTemp != 0))
              checked[5] = 1;
          if((checkedResTemp.isChecked7() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked7() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked7() && pageSize == 50 && pageNumTemp != 0))
              checked[6] = 1;
          if((checkedResTemp.isChecked8() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked8() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked8() && pageSize == 50 && pageNumTemp != 0))
              checked[7] = 1;
          if((checkedResTemp.isChecked9() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked9() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked9() && pageSize == 50 && pageNumTemp != 0))
              checked[8] = 1;
          if((checkedResTemp.isChecked10() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked10() && pageSize == 10 && pageNumTemp != 0)
              ||(checkedResTemp.isChecked10() && pageSize == 50 && pageNumTemp != 0))
              checked[9] = 1;
          if((checkedResTemp.isChecked11() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked11() && pageSize == 50 && pageNumTemp != 0))
              checked[10] = 1;
          if((checkedResTemp.isChecked12() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked12() && pageSize == 50 && pageNumTemp != 0))
              checked[11] = 1;
          if((checkedResTemp.isChecked13() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked13() && pageSize == 50 && pageNumTemp != 0))
              checked[12] = 1;
          if((checkedResTemp.isChecked14() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked14() && pageSize == 50 && pageNumTemp != 0))
              checked[13] = 1;
          if((checkedResTemp.isChecked15() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked15() && pageSize == 50 && pageNumTemp != 0))
              checked[14] = 1;
          if((checkedResTemp.isChecked16() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked16() && pageSize == 50 && pageNumTemp != 0))
              checked[15] = 1;
          if((checkedResTemp.isChecked17() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked17() && pageSize == 50 && pageNumTemp != 0))
              checked[16] = 1;
          if((checkedResTemp.isChecked18() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked18() && pageSize == 50 && pageNumTemp != 0))
              checked[17] = 1;
          if((checkedResTemp.isChecked19() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked19() && pageSize == 50 && pageNumTemp != 0))
              checked[18] = 1;
          if((checkedResTemp.isChecked20() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked20() && pageSize == 50 && pageNumTemp != 0))
              checked[19] = 1;
          if((checkedResTemp.isChecked21() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked21() && pageSize == 50 && pageNumTemp != 0))
              checked[20] = 1;
          if((checkedResTemp.isChecked22() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked22() && pageSize == 50 && pageNumTemp != 0))
              checked[21] = 1;
          if((checkedResTemp.isChecked23() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked23() && pageSize == 50 && pageNumTemp != 0))
              checked[22] = 1;
          if((checkedResTemp.isChecked24() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked24() && pageSize == 50 && pageNumTemp != 0))
              checked[23] = 1;
          if((checkedResTemp.isChecked25() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked25() && pageSize == 50 && pageNumTemp != 0))
              checked[24] = 1;
          if((checkedResTemp.isChecked26() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked26() && pageSize == 50 && pageNumTemp != 0))
              checked[25] = 1;
          if((checkedResTemp.isChecked27() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked27() && pageSize == 50 && pageNumTemp != 0))
              checked[26] = 1;
          if((checkedResTemp.isChecked28() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked28() && pageSize == 50 && pageNumTemp != 0))
              checked[27] = 1;
          if((checkedResTemp.isChecked29() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked29() && pageSize == 50 && pageNumTemp != 0))
              checked[28] = 1;
          if((checkedResTemp.isChecked30() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked30() && pageSize == 50 && pageNumTemp != 0))
              checked[29] = 1;
          if((checkedResTemp.isChecked31() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked31() && pageSize == 50 && pageNumTemp != 0))
              checked[30] = 1;
          if((checkedResTemp.isChecked32() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked32() && pageSize == 50 && pageNumTemp != 0))
              checked[31] = 1;
          if((checkedResTemp.isChecked33() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked33() && pageSize == 50 && pageNumTemp != 0))
              checked[32] = 1;
          if((checkedResTemp.isChecked34() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked34() && pageSize == 50 && pageNumTemp != 0))
              checked[33] = 1;
          if((checkedResTemp.isChecked35() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked35() && pageSize == 50 && pageNumTemp != 0))
              checked[34] = 1;
          if((checkedResTemp.isChecked36() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked36() && pageSize == 50 && pageNumTemp != 0))
              checked[35] = 1;
          if((checkedResTemp.isChecked37() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked37() && pageSize == 50 && pageNumTemp != 0))
              checked[36] = 1;
          if((checkedResTemp.isChecked38() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked38() && pageSize == 50 && pageNumTemp != 0))
              checked[37] = 1;
          if((checkedResTemp.isChecked39() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked39() && pageSize == 50 && pageNumTemp != 0))
              checked[38] = 1;
          if((checkedResTemp.isChecked40() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked40() && pageSize == 50 && pageNumTemp != 0))
              checked[39] = 1;
          if((checkedResTemp.isChecked41() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked41() && pageSize == 50 && pageNumTemp != 0))
              checked[40] = 1;
          if((checkedResTemp.isChecked42() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked42() && pageSize == 50 && pageNumTemp != 0))
              checked[41] = 1;
          if((checkedResTemp.isChecked43() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked43() && pageSize == 50 && pageNumTemp != 0))
              checked[42] = 1;
          if((checkedResTemp.isChecked44() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked44() && pageSize == 50 && pageNumTemp != 0))
              checked[43] = 1;
          if((checkedResTemp.isChecked45() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked45() && pageSize == 50 && pageNumTemp != 0))
              checked[44] = 1;
          if((checkedResTemp.isChecked46() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked46() && pageSize == 50 && pageNumTemp != 0))
              checked[45] = 1;
          if((checkedResTemp.isChecked47() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked47() && pageSize == 50 && pageNumTemp != 0))
              checked[46] = 1;
          if((checkedResTemp.isChecked48() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked48() && pageSize == 50 && pageNumTemp != 0))
              checked[47] = 1;
          if((checkedResTemp.isChecked49() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked49() && pageSize == 50 && pageNumTemp != 0))
              checked[48] = 1;
          if((checkedResTemp.isChecked50() && pageNumTemp == 0)
              ||(checkedResTemp.isChecked50() && pageSize == 50 && pageNumTemp != 0))
              checked[49] = 1;
          if(checkedResTemp.isChecked51() && pageNumTemp == 0)
              checked[50] = 1;
          if(checkedResTemp.isChecked52() && pageNumTemp == 0)
              checked[51] = 1;
          if(checkedResTemp.isChecked53() && pageNumTemp == 0)
              checked[52] = 1;
          if(checkedResTemp.isChecked54() && pageNumTemp == 0)
              checked[53] = 1;
          if(checkedResTemp.isChecked55() && pageNumTemp == 0)
              checked[54] = 1;
          if(checkedResTemp.isChecked56() && pageNumTemp == 0)
              checked[55] = 1;
          if(checkedResTemp.isChecked57() && pageNumTemp == 0)
              checked[56] = 1;
          if(checkedResTemp.isChecked58() && pageNumTemp == 0)
              checked[57] = 1;
          if(checkedResTemp.isChecked59() && pageNumTemp == 0)
              checked[58] = 1;
          if(checkedResTemp.isChecked60() && pageNumTemp == 0)
              checked[59] = 1;
          if(checkedResTemp.isChecked61() && pageNumTemp == 0)
              checked[60] = 1;
          if(checkedResTemp.isChecked62() && pageNumTemp == 0)
              checked[61] = 1;
          if(checkedResTemp.isChecked63() && pageNumTemp == 0)
              checked[62] = 1;
          if(checkedResTemp.isChecked64() && pageNumTemp == 0)
              checked[63] = 1;
          if(checkedResTemp.isChecked65() && pageNumTemp == 0)
              checked[64] = 1;
          if(checkedResTemp.isChecked66() && pageNumTemp == 0)
              checked[65] = 1;
          if(checkedResTemp.isChecked67() && pageNumTemp == 0)
              checked[66] = 1;
          if(checkedResTemp.isChecked68() && pageNumTemp == 0)
              checked[67] = 1;
          if(checkedResTemp.isChecked69() && pageNumTemp == 0)
              checked[68] = 1;
          if(checkedResTemp.isChecked70() && pageNumTemp == 0)
              checked[69] = 1;
          if(checkedResTemp.isChecked71() && pageNumTemp == 0)
              checked[70] = 1;
          if(checkedResTemp.isChecked72() && pageNumTemp == 0)
              checked[71] = 1;
          if(checkedResTemp.isChecked73() && pageNumTemp == 0)
              checked[72] = 1;
          if(checkedResTemp.isChecked74() && pageNumTemp == 0)
              checked[73] = 1;
          if(checkedResTemp.isChecked75() && pageNumTemp == 0)
              checked[74] = 1;
          if(checkedResTemp.isChecked76() && pageNumTemp == 0)
              checked[75] = 1;
          if(checkedResTemp.isChecked77() && pageNumTemp == 0)
              checked[76] = 1;
          if(checkedResTemp.isChecked78() && pageNumTemp == 0)
              checked[77] = 1;
          if(checkedResTemp.isChecked79() && pageNumTemp == 0)
              checked[78] = 1;
          if(checkedResTemp.isChecked80() && pageNumTemp == 0)
              checked[79] = 1;
          if(checkedResTemp.isChecked81() && pageNumTemp == 0)
              checked[80] = 1;
          if(checkedResTemp.isChecked82() && pageNumTemp == 0)
              checked[81] = 1;
          if(checkedResTemp.isChecked83() && pageNumTemp == 0)
              checked[82] = 1;
          if(checkedResTemp.isChecked84() && pageNumTemp == 0)
              checked[83] = 1;
          if(checkedResTemp.isChecked85() && pageNumTemp == 0)
              checked[84] = 1;
          if(checkedResTemp.isChecked86() && pageNumTemp == 0)
              checked[85] = 1;
          if(checkedResTemp.isChecked87() && pageNumTemp == 0)
              checked[86] = 1;
          if(checkedResTemp.isChecked88() && pageNumTemp == 0)
              checked[87] = 1;
          if(checkedResTemp.isChecked89() && pageNumTemp == 0)
              checked[88] = 1;
          if(checkedResTemp.isChecked90() && pageNumTemp == 0)
              checked[89] = 1;
          if(checkedResTemp.isChecked91() && pageNumTemp == 0)
              checked[90] = 1;
          if(checkedResTemp.isChecked92() && pageNumTemp == 0)
              checked[91] = 1;
          if(checkedResTemp.isChecked93() && pageNumTemp == 0)
              checked[92] = 1;
          if(checkedResTemp.isChecked94() && pageNumTemp == 0)
              checked[93] = 1;
          if(checkedResTemp.isChecked95() && pageNumTemp == 0)
              checked[94] = 1;
          if(checkedResTemp.isChecked96() && pageNumTemp == 0)
              checked[95] = 1;
          if(checkedResTemp.isChecked97() && pageNumTemp == 0)
              checked[96] = 1;
          if(checkedResTemp.isChecked98() && pageNumTemp == 0)
              checked[97] = 1;
          if(checkedResTemp.isChecked99() && pageNumTemp == 0)
              checked[98] = 1;
          if(checkedResTemp.isChecked100() && pageNumTemp == 0)
              checked[99] = 1;
          int counter = 0;
          for (SubCategory item : SubCategoryListTemp) {
              if(checked[counter] == 1 && item.getSubCategoryNewsCount() == 0) {

                  mainService.subCategoryDelete(item.getId());
                     
              }
              counter++;
          }
//        搜尋子分類 再傳到Thymeleaf
          subCategoryPage = null;
//        預設搜尋
          if(subCategoryLastSearch == 0) {
              subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
          }
//       分類搜尋
          if(subCategoryLastSearch == 1) {
              subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
          }
          SubCategoryListTemp = subCategoryPage.getContent();
          model.addAttribute("subCategoryPage", subCategoryPage);
          model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
//        勾選結果的初始化
          checkedResTemp = null;
//        跳轉到子分類的主頁
          return "sub_category_home";
    }

    //////////////////////////////////////////////////
//  管理端的主頁的頁數設定動作(每頁10筆)
    @RequestMapping(value = "/home_set_pageSize_10")
    public String HomeSetPageSize10(Model model) {
//      每頁筆數 設為10筆
        pageSize = 10;

//      (開始)新聞的搜尋的初始化設定
//        新建一個 新聞的分類搜尋的輸入用變數
          News news01 = new News();
//        新建一個 新聞的子分類搜尋的輸入用變數
          News news02 = new News();
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          News news03 = new News();
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          News news04 = new News();
//        新建一個 新聞的開始時間搜尋的輸入用變數
          News news05 = new News();
//        新建一個 新聞的結束時間搜尋的輸入用變數
          News news06 = new News();
//        新建一個 新聞的複合搜尋的輸入用變數
          MultipleSearch news07 = new MultipleSearch();
//      (結束)新聞的搜尋的初始化設定

//      (開始)新聞的搜尋的初始值設定
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              news01.setCategory(lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              news02.setSubCategory(lastKeyWordStr);  
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              news03.setNewsTitle(lastKeyWordStr);  
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              news04.setNewsSubTitle(lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              news05.setReleaseTime(lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              news06.setReleaseTime(lastKeyWordStr);
//      (結束)新聞的搜尋的初始值設定
          
//      (開始)新聞的搜尋的Thymeleaf傳入設定
//        新建一個 新聞的分類搜尋的輸入用變數
          model.addAttribute("news01", news01);
//        新建一個 新聞的子分類搜尋的輸入用變數
          model.addAttribute("news02", news02);
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          model.addAttribute("news03", news03);
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          model.addAttribute("news04", news04);
//        新建一個 新聞的開始時間搜尋的輸入用變數
          model.addAttribute("news05", news05);
//        新建一個 新聞的結束時間搜尋的輸入用變數
          model.addAttribute("news06", news06);
//        新建一個 新聞的複合搜尋的輸入用變數
          model.addAttribute("news07", news07);
//      (結束)新聞的搜尋的Thymeleaf傳入設定

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定
        
//      (開始)勾選設定
//        新建一個 勾選結果儲存用的變數
          CheckedRes checkedRes = new CheckedRes();
//        將 勾選結果儲存用的變數 傳到Thymeleaf
          model.addAttribute("checkedRes", checkedRes);
//      (結束)勾選設定

//      (開始)分頁設定
          int pageNum = 0;
//        將 每頁最大顯示結果的筆數 改為10筆
//          pageSize = 10;
//        將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageNum", pageNum);
//        將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageSize", pageSize);
//      (結束)分頁設定

//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定
          
//    返回 管理端的新聞主頁
      return "home";
    }
//  管理端的主頁的頁數設定動作(每頁50筆)    
    @RequestMapping(value = "/home_set_pageSize_50")
    public String HomeSetPageSize50(Model model) {
//      每頁筆數 設為50筆
        pageSize = 50;
//      (開始)新聞的搜尋的初始化設定
//      新建一個 新聞的分類搜尋的輸入用變數
        News news01 = new News();
//      新建一個 新聞的子分類搜尋的輸入用變數
        News news02 = new News();
//      新建一個 新聞的新聞標題搜尋的輸入用變數
        News news03 = new News();
//      新建一個 新聞的新聞副標題搜尋的輸入用變數
        News news04 = new News();
//      新建一個 新聞的開始時間搜尋的輸入用變數
        News news05 = new News();
//      新建一個 新聞的結束時間搜尋的輸入用變數
        News news06 = new News();
//      新建一個 新聞的複合搜尋的輸入用變數
        MultipleSearch news07 = new MultipleSearch();
//    (結束)新聞的搜尋的初始化設定

//    (開始)新聞的搜尋的初始值設定
//      新聞的分類搜尋
        if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
            news01.setCategory(lastKeyWordStr);
//      新聞的子分類搜尋
        if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
            news02.setSubCategory(lastKeyWordStr);  
//      新聞的新聞標題搜尋
        if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
            news03.setNewsTitle(lastKeyWordStr);  
//      新聞的新聞副標題搜尋
        if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
            news04.setNewsSubTitle(lastKeyWordStr);
//      新聞的開始時間搜尋
        if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
            news05.setReleaseTime(lastKeyWordStr);
//      新聞的結束時間搜尋
        if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
            news06.setReleaseTime(lastKeyWordStr);
//    (結束)新聞的搜尋的初始值設定
        
//    (開始)新聞的搜尋的Thymeleaf傳入設定
//      新建一個 新聞的分類搜尋的輸入用變數
        model.addAttribute("news01", news01);
//      新建一個 新聞的子分類搜尋的輸入用變數
        model.addAttribute("news02", news02);
//      新建一個 新聞的新聞標題搜尋的輸入用變數
        model.addAttribute("news03", news03);
//      新建一個 新聞的新聞副標題搜尋的輸入用變數
        model.addAttribute("news04", news04);
//      新建一個 新聞的開始時間搜尋的輸入用變數
        model.addAttribute("news05", news05);
//      新建一個 新聞的結束時間搜尋的輸入用變數
        model.addAttribute("news06", news06);
//      新建一個 新聞的複合搜尋的輸入用變數
        model.addAttribute("news07", news07);
//    (結束)新聞的搜尋的Thymeleaf傳入設定

//    (開始)分類列表的初始化設定
//      將 新聞的分類選擇框用的List 傳到Thymeleaf
        model.addAttribute("categoryList", categoryListInitializer());
//    (結束)分類列表的初始化設定
        
//    (開始)子分類列表的初始化設定
//      將 新聞的子分類選擇框用的List 傳到Thymeleaf
        model.addAttribute("subCategoryList", subCategoryListInitializer());
//    (結束)子分類列表的初始化設定
      
//    (開始)勾選設定
//      新建一個 勾選結果儲存用的變數
        CheckedRes checkedRes = new CheckedRes();
//      將 勾選結果儲存用的變數 傳到Thymeleaf
        model.addAttribute("checkedRes", checkedRes);
//    (結束)勾選設定

//    (開始)分頁設定
        int pageNum = 0;
//      將 每頁最大顯示結果的筆數 改為10筆
//        pageSize = 10;
//      將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
        model.addAttribute("pageNum", pageNum);
//      將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
        model.addAttribute("pageSize", pageSize);
//    (結束)分頁設定

//    (開始)新聞搜尋結果的設定
//      將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
        model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//      將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
        model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//    (結束)新聞搜尋結果的設定
        
//    返回 管理端的新聞主頁
      return "home";
        
    }
//  管理端的主頁的頁數設定動作(每頁100筆)
    @RequestMapping(value = "/home_set_pageSize_100")
    public String HomePageSize100(Model model) {
//      每頁筆數 設為100筆
        pageSize = 100;
//      (開始)新聞的搜尋的初始化設定
//      新建一個 新聞的分類搜尋的輸入用變數
        News news01 = new News();
//      新建一個 新聞的子分類搜尋的輸入用變數
        News news02 = new News();
//      新建一個 新聞的新聞標題搜尋的輸入用變數
        News news03 = new News();
//      新建一個 新聞的新聞副標題搜尋的輸入用變數
        News news04 = new News();
//      新建一個 新聞的開始時間搜尋的輸入用變數
        News news05 = new News();
//      新建一個 新聞的結束時間搜尋的輸入用變數
        News news06 = new News();
//      新建一個 新聞的複合搜尋的輸入用變數
        MultipleSearch news07 = new MultipleSearch();
//    (結束)新聞的搜尋的初始化設定

//    (開始)新聞的搜尋的初始值設定
//      新聞的分類搜尋
        if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
            news01.setCategory(lastKeyWordStr);
//      新聞的子分類搜尋
        if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
            news02.setSubCategory(lastKeyWordStr);  
//      新聞的新聞標題搜尋
        if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
            news03.setNewsTitle(lastKeyWordStr);  
//      新聞的新聞副標題搜尋
        if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
            news04.setNewsSubTitle(lastKeyWordStr);
//      新聞的開始時間搜尋
        if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
            news05.setReleaseTime(lastKeyWordStr);
//      新聞的結束時間搜尋
        if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
            news06.setReleaseTime(lastKeyWordStr);
//    (結束)新聞的搜尋的初始值設定
        
//    (開始)新聞的搜尋的Thymeleaf傳入設定
//      新建一個 新聞的分類搜尋的輸入用變數
        model.addAttribute("news01", news01);
//      新建一個 新聞的子分類搜尋的輸入用變數
        model.addAttribute("news02", news02);
//      新建一個 新聞的新聞標題搜尋的輸入用變數
        model.addAttribute("news03", news03);
//      新建一個 新聞的新聞副標題搜尋的輸入用變數
        model.addAttribute("news04", news04);
//      新建一個 新聞的開始時間搜尋的輸入用變數
        model.addAttribute("news05", news05);
//      新建一個 新聞的結束時間搜尋的輸入用變數
        model.addAttribute("news06", news06);
//      新建一個 新聞的複合搜尋的輸入用變數
        model.addAttribute("news07", news07);
//    (結束)新聞的搜尋的Thymeleaf傳入設定

//    (開始)分類列表的初始化設定
//      將 新聞的分類選擇框用的List 傳到Thymeleaf
        model.addAttribute("categoryList", categoryListInitializer());
//    (結束)分類列表的初始化設定
        
//    (開始)子分類列表的初始化設定
//      將 新聞的子分類選擇框用的List 傳到Thymeleaf
        model.addAttribute("subCategoryList", subCategoryListInitializer());
//    (結束)子分類列表的初始化設定
      
//    (開始)勾選設定
//      新建一個 勾選結果儲存用的變數
        CheckedRes checkedRes = new CheckedRes();
//      將 勾選結果儲存用的變數 傳到Thymeleaf
        model.addAttribute("checkedRes", checkedRes);
//    (結束)勾選設定

//    (開始)分頁設定
        int pageNum = 0;
//      將 每頁最大顯示結果的筆數 改為10筆
//        pageSize = 10;
//      將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
        model.addAttribute("pageNum", pageNum);
//      將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
        model.addAttribute("pageSize", pageSize);
//    (結束)分頁設定

//    (開始)新聞搜尋結果的設定
//      將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
        model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//      將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
        model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//    (結束)新聞搜尋結果的設定
        
//    返回 管理端的新聞主頁
      return "home";
        
    }

//  管理端的分類主頁的頁數設定動作(每頁10筆)
    @RequestMapping(value = "/category_home_set_pageSize_10")
    public String CategoryHomeSetPageSize10(Model model) {
//      每頁筆數 設為10筆
        pageSize = 10;

        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋結果的初始化
        Page<Category> categoryPage = null;
//      搜尋所有分類
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
//      將搜尋結果 轉存為List
        CategoryListTemp = categoryPage.getContent();
//      將搜尋結果 傳到Thymeleaf
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到 分類的主頁
        return "category_home";
    }
//  管理端的分類主頁的頁數設定動作(每頁50筆)    
    @RequestMapping(value = "/category_home_set_pageSize_50")
    public String CategoryHomeSetPageSize50(Model model) {
//      每頁筆數 設為50筆
        pageSize = 50;
        
        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋結果的初始化
        Page<Category> categoryPage = null;
//      搜尋所有分類
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
//      將搜尋結果 轉存為List
        CategoryListTemp = categoryPage.getContent();
//      將搜尋結果 傳到Thymeleaf
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到 分類的主頁
        return "category_home";
        
    }
//  管理端的分類主頁的頁數設定動作(每頁100筆)
    @RequestMapping(value = "/category_home_set_pageSize_100")
    public String CategoryHomePageSize100(Model model) {
//      每頁筆數 設為100筆
        pageSize = 100;
        
        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋結果的初始化
        Page<Category> categoryPage = null;
//      搜尋所有分類
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
//      將搜尋結果 轉存為List
        CategoryListTemp = categoryPage.getContent();
//      將搜尋結果 傳到Thymeleaf
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到 分類的主頁
        return "category_home";
        
    }

//  管理端的子分類主頁的頁數設定動作(每頁10筆)
    @RequestMapping(value = "/sub_category_home_set_pageSize_10")
    public String SubCategoryHomeSetPageSize10(Model model) {
//      每頁筆數 設為10筆
        pageSize = 10;

        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      找到所有分類 再傳到Thymeleaf
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋完子分類 再傳到Thymeleaf        
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
        model.addAttribute("subCategoryPage", subCategoryPage);

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到子分類的主頁
        return "sub_category_home";
    }
//  管理端的子分類主頁的頁數設定動作(每頁50筆)    
    @RequestMapping(value = "/sub_category_home_set_pageSize_50")
    public String SubCategoryHomeSetPageSize50(Model model) {
//      每頁筆數 設為50筆
        pageSize = 50;
        
        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      找到所有分類 再傳到Thymeleaf
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋完子分類 再傳到Thymeleaf        
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
        model.addAttribute("subCategoryPage", subCategoryPage);

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到子分類的主頁
        return "sub_category_home";
        
    }
//  管理端的子分類主頁的頁數設定動作(每頁100筆)
    @RequestMapping(value = "/sub_category_home_set_pageSize_100")
    public String SubCategoryHomeSetPageSize100(Model model) {
//      每頁筆數 設為100筆
        pageSize = 100;
        
        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      找到所有分類 再傳到Thymeleaf
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋完子分類 再傳到Thymeleaf        
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
        model.addAttribute("subCategoryPage", subCategoryPage);

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到子分類的主頁
        return "sub_category_home";
    }
    
//  管理端的主頁的升降冪設定動作(升冪)
    @RequestMapping(value = "/home_set_asc")
    public String HomeSetAsc(Model model) {
//      排序方式 改成升冪
        sortDescFlag = false;

//      (開始)新聞的搜尋的初始化設定
//        新建一個 新聞的分類搜尋的輸入用變數
          News news01 = new News();
//        新建一個 新聞的子分類搜尋的輸入用變數
          News news02 = new News();
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          News news03 = new News();
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          News news04 = new News();
//        新建一個 新聞的開始時間搜尋的輸入用變數
          News news05 = new News();
//        新建一個 新聞的結束時間搜尋的輸入用變數
          News news06 = new News();
//        新建一個 新聞的複合搜尋的輸入用變數
          MultipleSearch news07 = new MultipleSearch();
//      (結束)新聞的搜尋的初始化設定

//      (開始)新聞的搜尋的初始值設定
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              news01.setCategory(lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              news02.setSubCategory(lastKeyWordStr);  
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              news03.setNewsTitle(lastKeyWordStr);  
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              news04.setNewsSubTitle(lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              news05.setReleaseTime(lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              news06.setReleaseTime(lastKeyWordStr);
//      (結束)新聞的搜尋的初始值設定
          
//      (開始)新聞的搜尋的Thymeleaf傳入設定
//        新建一個 新聞的分類搜尋的輸入用變數
          model.addAttribute("news01", news01);
//        新建一個 新聞的子分類搜尋的輸入用變數
          model.addAttribute("news02", news02);
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          model.addAttribute("news03", news03);
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          model.addAttribute("news04", news04);
//        新建一個 新聞的開始時間搜尋的輸入用變數
          model.addAttribute("news05", news05);
//        新建一個 新聞的結束時間搜尋的輸入用變數
          model.addAttribute("news06", news06);
//        新建一個 新聞的複合搜尋的輸入用變數
          model.addAttribute("news07", news07);
//      (結束)新聞的搜尋的Thymeleaf傳入設定

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定
        
//      (開始)勾選設定
//        新建一個 勾選結果儲存用的變數
          CheckedRes checkedRes = new CheckedRes();
//        將 勾選結果儲存用的變數 傳到Thymeleaf
          model.addAttribute("checkedRes", checkedRes);
//      (結束)勾選設定

//      (開始)分頁設定
          int pageNum = 0;
//        將 每頁最大顯示結果的筆數 改為10筆
//          pageSize = 10;
//        將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageNum", pageNum);
//        將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageSize", pageSize);
//      (結束)分頁設定

//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定
          
//    返回 管理端的新聞主頁
      return "home";
    }
//  管理端的主頁的升降冪設定動作(降冪)    
    @RequestMapping(value = "/home_set_desc")
    public String HomeSetDesc(Model model) {
//      排序方式 改成降冪
        sortDescFlag = true;

//      (開始)新聞的搜尋的初始化設定
//        新建一個 新聞的分類搜尋的輸入用變數
          News news01 = new News();
//        新建一個 新聞的子分類搜尋的輸入用變數
          News news02 = new News();
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          News news03 = new News();
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          News news04 = new News();
//        新建一個 新聞的開始時間搜尋的輸入用變數
          News news05 = new News();
//        新建一個 新聞的結束時間搜尋的輸入用變數
          News news06 = new News();
//        新建一個 新聞的複合搜尋的輸入用變數
          MultipleSearch news07 = new MultipleSearch();
//      (結束)新聞的搜尋的初始化設定

//      (開始)新聞的搜尋的初始值設定
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              news01.setCategory(lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              news02.setSubCategory(lastKeyWordStr);  
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              news03.setNewsTitle(lastKeyWordStr);  
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              news04.setNewsSubTitle(lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              news05.setReleaseTime(lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              news06.setReleaseTime(lastKeyWordStr);
//      (結束)新聞的搜尋的初始值設定
          
//      (開始)新聞的搜尋的Thymeleaf傳入設定
//        新建一個 新聞的分類搜尋的輸入用變數
          model.addAttribute("news01", news01);
//        新建一個 新聞的子分類搜尋的輸入用變數
          model.addAttribute("news02", news02);
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          model.addAttribute("news03", news03);
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          model.addAttribute("news04", news04);
//        新建一個 新聞的開始時間搜尋的輸入用變數
          model.addAttribute("news05", news05);
//        新建一個 新聞的結束時間搜尋的輸入用變數
          model.addAttribute("news06", news06);
//        新建一個 新聞的複合搜尋的輸入用變數
          model.addAttribute("news07", news07);
//      (結束)新聞的搜尋的Thymeleaf傳入設定

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定
        
//      (開始)勾選設定
//        新建一個 勾選結果儲存用的變數
          CheckedRes checkedRes = new CheckedRes();
//        將 勾選結果儲存用的變數 傳到Thymeleaf
          model.addAttribute("checkedRes", checkedRes);
//      (結束)勾選設定

//      (開始)分頁設定
          int pageNum = 0;
//        將 每頁最大顯示結果的筆數 改為10筆
//          pageSize = 10;
//        將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageNum", pageNum);
//        將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageSize", pageSize);
//      (結束)分頁設定

//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定
          
//    返回 管理端的新聞主頁
      return "home";
    }
    
//  管理端的分類主頁的升降冪設定動作(升冪)
    @RequestMapping(value = "/category_home_set_asc")
    public String CategoryHomeSetAsc(Model model) {
//      排序方式 改為升冪
        sortDescFlag = false;

        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋結果的初始化
        Page<Category> categoryPage = null;
//      搜尋所有分類
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
//      將搜尋結果 轉存為List
        CategoryListTemp = categoryPage.getContent();
//      將搜尋結果 傳到Thymeleaf
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到 分類的主頁
        return "category_home";
    }
//  管理端的分類主頁的升降冪設定動作(降冪)    
    @RequestMapping(value = "/category_home_set_desc")
    public String CategoryHomeSetDesc(Model model) {
//      排序方式 改為降冪
        sortDescFlag = true;

        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      搜尋結果的初始化
        Page<Category> categoryPage = null;
//      搜尋所有分類
        categoryPage=mainService.findCategoryPageByAll(sortDescFlag, pageNum, pageSize);
//      將搜尋結果 轉存為List
        CategoryListTemp = categoryPage.getContent();
//      將搜尋結果 傳到Thymeleaf
        model.addAttribute("categoryPage", categoryPage);
        model.addAttribute("categoryPageSize", categoryPage.getNumberOfElements());

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到 分類的主頁
        return "category_home";
    }
    
//  管理端的子分類主頁的升降冪設定動作(升冪)
    @RequestMapping(value = "/sub_category_home_set_asc")
    public String SubCategoryHomeSetAsc(Model model) {
//      排序方式 改為升冪
        sortDescFlag = false;

        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      找到所有分類 再傳到Thymeleaf
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋完子分類 再傳到Thymeleaf        
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
        model.addAttribute("subCategoryPage", subCategoryPage);

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到子分類的主頁
        return "sub_category_home";
    }
//  管理端的子分類主頁的升降冪設定動作(降冪)
    @RequestMapping(value = "/sub_category_home_set_desc")
    public String SubCategoryHomeSetDesc(Model model) {
//      排序方式 改為降冪
        sortDescFlag = true;

        int pageNum = 0;
//      勾選用變數的初始化
        CheckedRes checkedRes = new CheckedRes();
        model.addAttribute("checkedRes", checkedRes);
//      頁數的初始化
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("pageSize", pageSize);
//      找到所有分類 再傳到Thymeleaf
        List<Category> res02 = categoryDao.findAll();
        List<String> categoryList = new ArrayList<>();
        for (Category item : res02) {
            categoryList.add(item.getCategory());
        }
        model.addAttribute("categoryList", categoryList);
        SubCategory subCategory = new SubCategory();
        model.addAttribute("subCategory", subCategory);
//      搜尋完子分類 再傳到Thymeleaf        
        Page<SubCategory> subCategoryPage = null;
//      預設搜尋
        if(subCategoryLastSearch == 0) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
//     分類搜尋
        if(subCategoryLastSearch == 1) {
            subCategoryPage=mainService.findSubCategoryPageByAll(sortDescFlag, pageNum, pageSize);
        }
        SubCategoryListTemp = subCategoryPage.getContent();
        model.addAttribute("subCategoryPage", subCategoryPage);
        model.addAttribute("subCategoryPageSize", subCategoryPage.getNumberOfElements());
        model.addAttribute("subCategoryPage", subCategoryPage);

//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      跳轉到子分類的主頁
        return "sub_category_home";
    }
    
    
    //////////////////////////////////////////////////
    
//  分類列表的初始化設定
    private List<String> categoryListInitializer() {
//      (開始)分類列表的初始化設定
//        搜尋 所有的分類
          List<Category> res02 = categoryDao.findAll();
//        新建一個 空的分類的List
          List<String> categoryList = new ArrayList<>();
//        對找所有的分類的搜尋結果 做forEach
          for (Category item : res02) {
//            將 找所有的分類的搜尋結果 依序加進 空的List
              categoryList.add(item.getCategory());
          }
//        回傳 所有的分類的搜尋結果
          return categoryList;
//      (結束)分類列表的初始化設定
    }

//  子分類列表的初始化設定
    private List<String> subCategoryListInitializer() {
//      (開始)子分類列表的初始化設定
//        暫存 新聞的分類搜尋的選擇結果
//          newsAddCategorySelect = news01.getCategory();
//          newsAddCategorySelect = categoryInput;
//        用分類搜尋子分類
          List<SubCategory> res01 = subCateogryDao.findByCategory(newsAddCategorySelect);
//        新建一個 空的子分類的List
          List<String> subCategoryList = new ArrayList<>();
//        對用分類搜尋子分類的搜尋結果 做forEach
          for (SubCategory item : res01) {
//            將 用分類搜尋子分類的搜尋結果 依序加進 空的List
              subCategoryList.add(item.getSubCategory());
          }
//        回傳 用分類搜尋子分類的搜尋結果
          return subCategoryList;
//      (結束)子分類列表的初始化設定
    }

//  新聞的條件搜尋
    private Page<News> NewsSearch(int pageNum, int pageSize){
        
//      (開始)新聞搜尋結果的設定
//        新建一個 空的新聞搜尋結果的Page
          Page<News> newsPage = null;
//        新聞的預設搜尋
          if(lastSearch == 0)// 取得 新聞的分類的搜尋結果
              newsPage=mainService.findPageByCategory(sortDescFlag, pageNum, pageSize, "分類1");            
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              newsPage=mainService.findPageByCategory(sortDescFlag, pageNum, pageSize, lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              newsPage=mainService.findPageBySubCategory(sortDescFlag, pageNum, pageSize, lastKeyWordStr);     
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              newsPage=mainService.findPageByNewsTitle(sortDescFlag, pageNum, pageSize, lastKeyWordStr);
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              newsPage=mainService.findPageByNewsSubTitle(sortDescFlag, pageNum, pageSize, lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              newsPage=mainService.findPageByReleaseTimeGreater(sortDescFlag, pageNum, pageSize, lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              newsPage=mainService.findPageByReleaseTimeLess(sortDescFlag, pageNum, pageSize, lastKeyWordStr);
//        複合條件搜尋
          if(lastSearch == 7)// 取得 複合條件的搜尋結果
              newsPage=mainService.findPageByNewsByInput(sortDescFlag, pageNum, pageSize, lastKeyWordMultipleStr.getCategory(), lastKeyWordMultipleStr.getSubCategory(), lastKeyWordMultipleStr.getNewsTitle(), lastKeyWordMultipleStr.getNewsSubTitle(), lastKeyWordMultipleStr.getReleaseTimeStart(), lastKeyWordMultipleStr.getReleaseTimeEnd(), lastKeyWordMultipleStr.getBuildTimeStart(), lastKeyWordMultipleStr.getBuildTimeEnd());
//        按照發布日期降冪的搜尋
          if(lastSearch == 8)// 取得 複合條件的搜尋結果
              newsPage=mainService.findPageAll(sortDescFlag, pageNum, pageSize);
//        將 新聞的搜尋結果的Page 轉存到 暫存用的新聞的搜尋結果List
          NewsListTemp = newsPage.getContent();
//        回傳 新聞的搜尋結果的Page
          return newsPage;
//      (結束)新聞搜尋結果的設定
    }
    
    
//  讀取 選擇的新聞
    private News NewsListTempReader() {
//      讀取 勾選的新聞
        int counter0 = 0;
        int counter = 0;
        if((checkedResTemp.isChecked1() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked1() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked1() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 1;
        if((checkedResTemp.isChecked2() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked2() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked2() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 2;
        if((checkedResTemp.isChecked3() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked3() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked3() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 3;
        if((checkedResTemp.isChecked4() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked4() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked4() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 4;
        if((checkedResTemp.isChecked5() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked5() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked5() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 5;
        if((checkedResTemp.isChecked6() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked6() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked6() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 6;
        if((checkedResTemp.isChecked7() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked7() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked7() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 7;
        if((checkedResTemp.isChecked8() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked8() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked8() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 8;
        if((checkedResTemp.isChecked9() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked9() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked9() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 9;
        if((checkedResTemp.isChecked10() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked10() && pageSize == 10 && pageNumTemp != 0)
            ||(checkedResTemp.isChecked10() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 10;
        if((checkedResTemp.isChecked11() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked11() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 11;
        if((checkedResTemp.isChecked12() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked12() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 12;
        if((checkedResTemp.isChecked13() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked13() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 13;
        if((checkedResTemp.isChecked14() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked14() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 14;
        if((checkedResTemp.isChecked15() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked15() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 15;
        if((checkedResTemp.isChecked16() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked16() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 16;
        if((checkedResTemp.isChecked17() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked17() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 17;
        if((checkedResTemp.isChecked18() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked18() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 18;
        if((checkedResTemp.isChecked19() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked19() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 19;
        if((checkedResTemp.isChecked20() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked20() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 20;
        if((checkedResTemp.isChecked21() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked21() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 21;
        if((checkedResTemp.isChecked22() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked22() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 22;
        if((checkedResTemp.isChecked23() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked23() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 23;
        if((checkedResTemp.isChecked24() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked24() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 24;
        if((checkedResTemp.isChecked25() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked25() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 25;
        if((checkedResTemp.isChecked26() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked26() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 26;
        if((checkedResTemp.isChecked27() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked27() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 27;
        if((checkedResTemp.isChecked28() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked28() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 28;
        if((checkedResTemp.isChecked29() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked29() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 29;
        if((checkedResTemp.isChecked30() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked30() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 30;
        if((checkedResTemp.isChecked31() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked31() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 31;
        if((checkedResTemp.isChecked32() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked32() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 32;
        if((checkedResTemp.isChecked33() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked33() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 33;
        if((checkedResTemp.isChecked34() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked34() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 34;
        if((checkedResTemp.isChecked35() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked35() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 35;
        if((checkedResTemp.isChecked36() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked36() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 36;
        if((checkedResTemp.isChecked37() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked37() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 37;
        if((checkedResTemp.isChecked38() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked38() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 38;
        if((checkedResTemp.isChecked39() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked39() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 39;
        if((checkedResTemp.isChecked40() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked40() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 40;
        if((checkedResTemp.isChecked41() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked41() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 41;
        if((checkedResTemp.isChecked42() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked42() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 42;
        if((checkedResTemp.isChecked43() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked43() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 43;
        if((checkedResTemp.isChecked44() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked44() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 44;
        if((checkedResTemp.isChecked45() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked45() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 45;
        if((checkedResTemp.isChecked46() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked46() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 46;
        if((checkedResTemp.isChecked47() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked47() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 47;
        if((checkedResTemp.isChecked48() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked48() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 48;
        if((checkedResTemp.isChecked49() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked49() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 49;
        if((checkedResTemp.isChecked50() && pageNumTemp == 0)
            ||(checkedResTemp.isChecked50() && pageSize == 50 && pageNumTemp != 0))
            counter0 = 50;
        if(checkedResTemp.isChecked51() && pageNumTemp == 0)
            counter0 = 51;
        if(checkedResTemp.isChecked52() && pageNumTemp == 0)
            counter0 = 52;
        if(checkedResTemp.isChecked53() && pageNumTemp == 0)
            counter0 = 53;
        if(checkedResTemp.isChecked54() && pageNumTemp == 0)
            counter0 = 54;
        if(checkedResTemp.isChecked55() && pageNumTemp == 0)
            counter0 = 55;
        if(checkedResTemp.isChecked56() && pageNumTemp == 0)
            counter0 = 56;
        if(checkedResTemp.isChecked57() && pageNumTemp == 0)
            counter0 = 57;
        if(checkedResTemp.isChecked58() && pageNumTemp == 0)
            counter0 = 58;
        if(checkedResTemp.isChecked59() && pageNumTemp == 0)
            counter0 = 59;
        if(checkedResTemp.isChecked60() && pageNumTemp == 0)
            counter0 = 60;
        if(checkedResTemp.isChecked61() && pageNumTemp == 0)
            counter0 = 61;
        if(checkedResTemp.isChecked62() && pageNumTemp == 0)
            counter0 = 62;
        if(checkedResTemp.isChecked63() && pageNumTemp == 0)
            counter0 = 63;
        if(checkedResTemp.isChecked64() && pageNumTemp == 0)
            counter0 = 64;
        if(checkedResTemp.isChecked65() && pageNumTemp == 0)
            counter0 = 65;
        if(checkedResTemp.isChecked66() && pageNumTemp == 0)
            counter0 = 66;
        if(checkedResTemp.isChecked67() && pageNumTemp == 0)
            counter0 = 67;
        if(checkedResTemp.isChecked68() && pageNumTemp == 0)
            counter0 = 68;
        if(checkedResTemp.isChecked69() && pageNumTemp == 0)
            counter0 = 69;
        if(checkedResTemp.isChecked70() && pageNumTemp == 0)
            counter0 = 70;
        if(checkedResTemp.isChecked71() && pageNumTemp == 0)
            counter0 = 71;
        if(checkedResTemp.isChecked72() && pageNumTemp == 0)
            counter0 = 72;
        if(checkedResTemp.isChecked73() && pageNumTemp == 0)
            counter0 = 73;
        if(checkedResTemp.isChecked74() && pageNumTemp == 0)
            counter0 = 74;
        if(checkedResTemp.isChecked75() && pageNumTemp == 0)
            counter0 = 75;
        if(checkedResTemp.isChecked76() && pageNumTemp == 0)
            counter0 = 76;
        if(checkedResTemp.isChecked77() && pageNumTemp == 0)
            counter0 = 77;
        if(checkedResTemp.isChecked78() && pageNumTemp == 0)
            counter0 = 78;
        if(checkedResTemp.isChecked79() && pageNumTemp == 0)
            counter0 = 79;
        if(checkedResTemp.isChecked80() && pageNumTemp == 0)
            counter0 = 80;
        if(checkedResTemp.isChecked81() && pageNumTemp == 0)
            counter0 = 81;
        if(checkedResTemp.isChecked82() && pageNumTemp == 0)
            counter0 = 82;
        if(checkedResTemp.isChecked83() && pageNumTemp == 0)
            counter0 = 83;
        if(checkedResTemp.isChecked84() && pageNumTemp == 0)
            counter0 = 84;
        if(checkedResTemp.isChecked85() && pageNumTemp == 0)
            counter0 = 85;
        if(checkedResTemp.isChecked86() && pageNumTemp == 0)
            counter0 = 86;
        if(checkedResTemp.isChecked87() && pageNumTemp == 0)
            counter0 = 87;
        if(checkedResTemp.isChecked88() && pageNumTemp == 0)
            counter0 = 88;
        if(checkedResTemp.isChecked89() && pageNumTemp == 0)
            counter0 = 89;
        if(checkedResTemp.isChecked90() && pageNumTemp == 0)
            counter0 = 90;
        if(checkedResTemp.isChecked91() && pageNumTemp == 0)
            counter0 = 91;
        if(checkedResTemp.isChecked92() && pageNumTemp == 0)
            counter0 = 92;
        if(checkedResTemp.isChecked93() && pageNumTemp == 0)
            counter0 = 93;
        if(checkedResTemp.isChecked94() && pageNumTemp == 0)
            counter0 = 94;
        if(checkedResTemp.isChecked95() && pageNumTemp == 0)
            counter0 = 95;
        if(checkedResTemp.isChecked96() && pageNumTemp == 0)
            counter0 = 96;
        if(checkedResTemp.isChecked97() && pageNumTemp == 0)
            counter0 = 97;
        if(checkedResTemp.isChecked98() && pageNumTemp == 0)
            counter0 = 98;
        if(checkedResTemp.isChecked99() && pageNumTemp == 0)
            counter0 = 99;
        if(checkedResTemp.isChecked100() && pageNumTemp == 0)
            counter0 = 100;
        
        for (News item : NewsListTemp) {
            counter++;
            if(counter == counter0) {
                return item;
//                break;
            }
        }

        return null;
    }
        
    //////////////////////////////////////////////////
//  客戶端的新聞主頁
    @RequestMapping(value = "/client_home/{pageNum}")
    public String ClientHome(@PathVariable(value="pageNum",required=false) int pageNum, Model model) {
//      搜尋結果為零時 防止頁數為負
        if(pageNum == -1)
            pageNum = 0;
//        暫存 此頁頁數
          pageNumTemp = pageNum;
        
//        (開始)新聞的搜尋的初始值設定
//          新聞的複合搜尋
            lastSearch = 8;
            lastKeyWordStr = "";
//        (結束)新聞的搜尋的初始值設定
            
//        (開始)分類列表的初始化設定
//          將 新聞的分類選擇框用的List 傳到Thymeleaf
            model.addAttribute("categoryList", categoryListInitializer());
//        (結束)分類列表的初始化設定
            
//        (開始)子分類列表的初始化設定
//          將 新聞的子分類選擇框用的List 傳到Thymeleaf
            model.addAttribute("subCategoryList", subCategoryListInitializer());
//        (結束)子分類列表的初始化設定
          
//        (開始)勾選設定
//          新建一個 勾選結果儲存用的變數
            CheckedRes checkedRes = new CheckedRes();
//          將 勾選結果儲存用的變數 傳到Thymeleaf
            model.addAttribute("checkedRes", checkedRes);
//        (結束)勾選設定

//        (開始)分頁設定
//          將 每頁最大顯示結果的筆數 改為10筆
//            pageSize = 10;
//          將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
            model.addAttribute("pageNum", pageNum);
//          將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
            model.addAttribute("pageSize", pageSize);
//        (結束)分頁設定

//        (開始)新聞搜尋結果的設定
//          將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
            model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//          將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
            model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//        (結束)新聞搜尋結果的設定
            
//      分類&子分類的新聞數量更新(變數宣告)
        List<Category> findCategoryByAllRes = null;
        List<News> findByCategoryRes = null;
        List<News> findByCategoryAndSubCategoryRes = null;
        List<SubCategory> findSubCategoryByCategoryRes = null;
        int newsCount = 0;
//      分類&子分類的新聞數量更新
        findCategoryByAllRes = mainService.findCategoryByAll();
        for (Category item : findCategoryByAllRes) {
            findByCategoryRes = mainService.findByCategory(item.getCategory());
            newsCount = findByCategoryRes.size();
            item.setNewsCount(newsCount);
            mainService.categoryEditNewsCount(item);
            findSubCategoryByCategoryRes = mainService.findSubCategoryByCategory(item.getCategory());
            for (SubCategory item02 : findSubCategoryByCategoryRes) {
                findByCategoryAndSubCategoryRes = mainService.findByCategoryAndSubCategory(item02.getCategory(), item02.getSubCategory());
                newsCount = findByCategoryAndSubCategoryRes.size();
                item02.setSubCategoryNewsCount(newsCount);
                mainService.subCategoryEditNewsCount(item02);
            }
        }
//      返回 客戶端的新聞主頁
        return "client_home";
    }
    
//  客戶端的勾選動作
    @PostMapping("/client_home")
    public String ClientHomeChecked(@ModelAttribute("checkedRes") CheckedRes checkedRes, Model model) {
//    統計 勾選結果
      checkedResTemp = checkedRes;
      checkedResTempCount = 0;
      if(checkedRes.isChecked1())
          checkedResTempCount++;
      if(checkedRes.isChecked2())
          checkedResTempCount++;
      if(checkedRes.isChecked3())
          checkedResTempCount++;
      if(checkedRes.isChecked4())
          checkedResTempCount++;
      if(checkedRes.isChecked5())
          checkedResTempCount++;
      if(checkedRes.isChecked6())
          checkedResTempCount++;
      if(checkedRes.isChecked7())
          checkedResTempCount++;
      if(checkedRes.isChecked8())
          checkedResTempCount++;
      if(checkedRes.isChecked9())
          checkedResTempCount++;
      if(checkedRes.isChecked10())
          checkedResTempCount++;
      if(checkedRes.isChecked11())
          checkedResTempCount++;
      if(checkedRes.isChecked12())
          checkedResTempCount++;
      if(checkedRes.isChecked13())
          checkedResTempCount++;
      if(checkedRes.isChecked14())
          checkedResTempCount++;
      if(checkedRes.isChecked15())
          checkedResTempCount++;
      if(checkedRes.isChecked16())
          checkedResTempCount++;
      if(checkedRes.isChecked17())
          checkedResTempCount++;
      if(checkedRes.isChecked18())
          checkedResTempCount++;
      if(checkedRes.isChecked19())
          checkedResTempCount++;
      if(checkedRes.isChecked20())
          checkedResTempCount++;
      if(checkedRes.isChecked21())
          checkedResTempCount++;
      if(checkedRes.isChecked22())
          checkedResTempCount++;
      if(checkedRes.isChecked23())
          checkedResTempCount++;
      if(checkedRes.isChecked24())
          checkedResTempCount++;
      if(checkedRes.isChecked25())
          checkedResTempCount++;
      if(checkedRes.isChecked26())
          checkedResTempCount++;
      if(checkedRes.isChecked27())
          checkedResTempCount++;
      if(checkedRes.isChecked28())
          checkedResTempCount++;
      if(checkedRes.isChecked29())
          checkedResTempCount++;
      if(checkedRes.isChecked30())
          checkedResTempCount++;
      if(checkedRes.isChecked31())
          checkedResTempCount++;
      if(checkedRes.isChecked32())
          checkedResTempCount++;
      if(checkedRes.isChecked33())
          checkedResTempCount++;
      if(checkedRes.isChecked34())
          checkedResTempCount++;
      if(checkedRes.isChecked35())
          checkedResTempCount++;
      if(checkedRes.isChecked36())
          checkedResTempCount++;
      if(checkedRes.isChecked37())
          checkedResTempCount++;
      if(checkedRes.isChecked38())
          checkedResTempCount++;
      if(checkedRes.isChecked39())
          checkedResTempCount++;
      if(checkedRes.isChecked40())
          checkedResTempCount++;
      if(checkedRes.isChecked41())
          checkedResTempCount++;
      if(checkedRes.isChecked42())
          checkedResTempCount++;
      if(checkedRes.isChecked43())
          checkedResTempCount++;
      if(checkedRes.isChecked44())
          checkedResTempCount++;
      if(checkedRes.isChecked45())
          checkedResTempCount++;
      if(checkedRes.isChecked46())
          checkedResTempCount++;
      if(checkedRes.isChecked47())
          checkedResTempCount++;
      if(checkedRes.isChecked48())
          checkedResTempCount++;
      if(checkedRes.isChecked49())
          checkedResTempCount++;
      if(checkedRes.isChecked50())
          checkedResTempCount++;
      if(checkedRes.isChecked51())
          checkedResTempCount++;
      if(checkedRes.isChecked52())
          checkedResTempCount++;
      if(checkedRes.isChecked53())
          checkedResTempCount++;
      if(checkedRes.isChecked54())
          checkedResTempCount++;
      if(checkedRes.isChecked55())
          checkedResTempCount++;
      if(checkedRes.isChecked56())
          checkedResTempCount++;
      if(checkedRes.isChecked57())
          checkedResTempCount++;
      if(checkedRes.isChecked58())
          checkedResTempCount++;
      if(checkedRes.isChecked59())
          checkedResTempCount++;
      if(checkedRes.isChecked60())
          checkedResTempCount++;
      if(checkedRes.isChecked61())
          checkedResTempCount++;
      if(checkedRes.isChecked62())
          checkedResTempCount++;
      if(checkedRes.isChecked63())
          checkedResTempCount++;
      if(checkedRes.isChecked64())
          checkedResTempCount++;
      if(checkedRes.isChecked65())
          checkedResTempCount++;
      if(checkedRes.isChecked66())
          checkedResTempCount++;
      if(checkedRes.isChecked67())
          checkedResTempCount++;
      if(checkedRes.isChecked68())
          checkedResTempCount++;
      if(checkedRes.isChecked69())
          checkedResTempCount++;
      if(checkedRes.isChecked70())
          checkedResTempCount++;
      if(checkedRes.isChecked71())
          checkedResTempCount++;
      if(checkedRes.isChecked72())
          checkedResTempCount++;
      if(checkedRes.isChecked73())
          checkedResTempCount++;
      if(checkedRes.isChecked74())
          checkedResTempCount++;
      if(checkedRes.isChecked75())
          checkedResTempCount++;
      if(checkedRes.isChecked76())
          checkedResTempCount++;
      if(checkedRes.isChecked77())
          checkedResTempCount++;
      if(checkedRes.isChecked78())
          checkedResTempCount++;
      if(checkedRes.isChecked79())
          checkedResTempCount++;
      if(checkedRes.isChecked80())
          checkedResTempCount++;
      if(checkedRes.isChecked81())
          checkedResTempCount++;
      if(checkedRes.isChecked82())
          checkedResTempCount++;
      if(checkedRes.isChecked83())
          checkedResTempCount++;
      if(checkedRes.isChecked84())
          checkedResTempCount++;
      if(checkedRes.isChecked85())
          checkedResTempCount++;
      if(checkedRes.isChecked86())
          checkedResTempCount++;
      if(checkedRes.isChecked87())
          checkedResTempCount++;
      if(checkedRes.isChecked88())
          checkedResTempCount++;
      if(checkedRes.isChecked89())
          checkedResTempCount++;
      if(checkedRes.isChecked90())
          checkedResTempCount++;
      if(checkedRes.isChecked91())
          checkedResTempCount++;
      if(checkedRes.isChecked92())
          checkedResTempCount++;
      if(checkedRes.isChecked93())
          checkedResTempCount++;
      if(checkedRes.isChecked94())
          checkedResTempCount++;
      if(checkedRes.isChecked95())
          checkedResTempCount++;
      if(checkedRes.isChecked96())
          checkedResTempCount++;
      if(checkedRes.isChecked97())
          checkedResTempCount++;
      if(checkedRes.isChecked98())
          checkedResTempCount++;
      if(checkedRes.isChecked99())
          checkedResTempCount++;
      if(checkedRes.isChecked100())
          checkedResTempCount++;
//    (開始)新聞的搜尋的初始化設定
//    新建一個 新聞的分類搜尋的輸入用變數
      News news01 = new News();
//    新建一個 新聞的子分類搜尋的輸入用變數
      News news02 = new News();
//    新建一個 新聞的新聞標題搜尋的輸入用變數
      News news03 = new News();
//    新建一個 新聞的新聞副標題搜尋的輸入用變數
      News news04 = new News();
//    新建一個 新聞的開始時間搜尋的輸入用變數
      News news05 = new News();
//    新建一個 新聞的結束時間搜尋的輸入用變數
      News news06 = new News();
//    新建一個 新聞的複合搜尋的輸入用變數
      MultipleSearch news07 = new MultipleSearch();
//  (結束)新聞的搜尋的初始化設定

//  (開始)新聞的搜尋的初始值設定
//    新聞的分類搜尋
      if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
          news01.setCategory(lastKeyWordStr);
//    新聞的子分類搜尋
      if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
          news02.setSubCategory(lastKeyWordStr);  
//    新聞的新聞標題搜尋
      if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
          news03.setNewsTitle(lastKeyWordStr);  
//    新聞的新聞副標題搜尋
      if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
          news04.setNewsSubTitle(lastKeyWordStr);
//    新聞的開始時間搜尋
      if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
          news05.setReleaseTime(lastKeyWordStr);
//    新聞的結束時間搜尋
      if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
          news06.setReleaseTime(lastKeyWordStr);
//    新聞的複合搜尋
      if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
          news07 = lastKeyWordMultipleStr;
//  (結束)新聞的搜尋的初始值設定
      
//  (開始)新聞的搜尋的Thymeleaf傳入設定
//    新建一個 新聞的分類搜尋的輸入用變數
      model.addAttribute("news01", news01);
//    新建一個 新聞的子分類搜尋的輸入用變數
      model.addAttribute("news02", news02);
//    新建一個 新聞的新聞標題搜尋的輸入用變數
      model.addAttribute("news03", news03);
//    新建一個 新聞的新聞副標題搜尋的輸入用變數
      model.addAttribute("news04", news04);
//    新建一個 新聞的開始時間搜尋的輸入用變數
      model.addAttribute("news05", news05);
//    新建一個 新聞的結束時間搜尋的輸入用變數
      model.addAttribute("news06", news06);
//    新建一個 新聞的複合搜尋的輸入用變數
      model.addAttribute("news07", news07);
//  (結束)新聞的搜尋的Thymeleaf傳入設定

//  (開始)分類列表的初始化設定
//    將 新聞的分類選擇框用的List 傳到Thymeleaf
      model.addAttribute("categoryList", categoryListInitializer());
//  (結束)分類列表的初始化設定
      
//  (開始)子分類列表的初始化設定
//    將 新聞的子分類選擇框用的List 傳到Thymeleaf
      model.addAttribute("subCategoryList", subCategoryListInitializer());
//  (結束)子分類列表的初始化設定
    
//  (開始)勾選設定
//    勾選結果儲存用的變數 設定為初始值
      checkedRes = new CheckedRes();
//    將 勾選結果儲存用的變數 傳到Thymeleaf
      model.addAttribute("checkedRes", checkedRes);
//  (結束)勾選設定

//  (開始)分頁設定
//    將 空的目前選擇的分頁 改為目前的頁數
      int pageNum = pageNumTemp;
//    將 每頁最大顯示結果的筆數 改為10筆
//      int pageSize = 10;
//    將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
      model.addAttribute("pageNum", pageNum);
//    將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
      model.addAttribute("pageSize", pageSize);
//  (結束)分頁設定

//  (開始)新聞搜尋結果的設定
//    將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
      model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//    將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
      model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//  (結束)新聞搜尋結果的設定

//    返回 客戶端的新聞主頁
      return "client_home";
    }

//  客戶端的全選動作
    @RequestMapping("/client_all_check")
    public String ClientHomeAllChecked(Model model) {
//    勾選結果 做全選
      CheckedRes checkedRes = new CheckedRes();
      checkedRes.setChecked1(true);
      checkedRes.setChecked2(true);
      checkedRes.setChecked3(true);
      checkedRes.setChecked4(true);
      checkedRes.setChecked5(true);
      checkedRes.setChecked6(true);
      checkedRes.setChecked7(true);
      checkedRes.setChecked8(true);
      checkedRes.setChecked9(true);
      checkedRes.setChecked10(true);
      checkedRes.setChecked11(true);
      checkedRes.setChecked12(true);
      checkedRes.setChecked13(true);
      checkedRes.setChecked14(true);
      checkedRes.setChecked15(true);
      checkedRes.setChecked16(true);
      checkedRes.setChecked17(true);
      checkedRes.setChecked18(true);
      checkedRes.setChecked19(true);
      checkedRes.setChecked20(true);
      checkedRes.setChecked21(true);
      checkedRes.setChecked22(true);
      checkedRes.setChecked23(true);
      checkedRes.setChecked24(true);
      checkedRes.setChecked25(true);
      checkedRes.setChecked26(true);
      checkedRes.setChecked27(true);
      checkedRes.setChecked28(true);
      checkedRes.setChecked29(true);
      checkedRes.setChecked30(true);
      checkedRes.setChecked31(true);
      checkedRes.setChecked32(true);
      checkedRes.setChecked33(true);
      checkedRes.setChecked34(true);
      checkedRes.setChecked35(true);
      checkedRes.setChecked36(true);
      checkedRes.setChecked37(true);
      checkedRes.setChecked38(true);
      checkedRes.setChecked39(true);
      checkedRes.setChecked40(true);
      checkedRes.setChecked41(true);
      checkedRes.setChecked42(true);
      checkedRes.setChecked43(true);
      checkedRes.setChecked44(true);
      checkedRes.setChecked45(true);
      checkedRes.setChecked46(true);
      checkedRes.setChecked47(true);
      checkedRes.setChecked48(true);
      checkedRes.setChecked49(true);
      checkedRes.setChecked50(true);
      checkedRes.setChecked51(true);
      checkedRes.setChecked52(true);
      checkedRes.setChecked53(true);
      checkedRes.setChecked54(true);
      checkedRes.setChecked55(true);
      checkedRes.setChecked56(true);
      checkedRes.setChecked57(true);
      checkedRes.setChecked58(true);
      checkedRes.setChecked59(true);
      checkedRes.setChecked60(true);
      checkedRes.setChecked61(true);
      checkedRes.setChecked62(true);
      checkedRes.setChecked63(true);
      checkedRes.setChecked64(true);
      checkedRes.setChecked65(true);
      checkedRes.setChecked66(true);
      checkedRes.setChecked67(true);
      checkedRes.setChecked68(true);
      checkedRes.setChecked69(true);
      checkedRes.setChecked70(true);
      checkedRes.setChecked71(true);
      checkedRes.setChecked72(true);
      checkedRes.setChecked73(true);
      checkedRes.setChecked74(true);
      checkedRes.setChecked75(true);
      checkedRes.setChecked76(true);
      checkedRes.setChecked77(true);
      checkedRes.setChecked78(true);
      checkedRes.setChecked79(true);
      checkedRes.setChecked80(true);
      checkedRes.setChecked81(true);
      checkedRes.setChecked82(true);
      checkedRes.setChecked83(true);
      checkedRes.setChecked84(true);
      checkedRes.setChecked85(true);
      checkedRes.setChecked86(true);
      checkedRes.setChecked87(true);
      checkedRes.setChecked88(true);
      checkedRes.setChecked89(true);
      checkedRes.setChecked90(true);
      checkedRes.setChecked91(true);
      checkedRes.setChecked92(true);
      checkedRes.setChecked93(true);
      checkedRes.setChecked94(true);
      checkedRes.setChecked95(true);
      checkedRes.setChecked96(true);
      checkedRes.setChecked97(true);
      checkedRes.setChecked98(true);
      checkedRes.setChecked99(true);
      checkedRes.setChecked100(true);
      
//    (開始)新聞的搜尋的初始化設定
//    新建一個 新聞的分類搜尋的輸入用變數
      News news01 = new News();
//    新建一個 新聞的子分類搜尋的輸入用變數
      News news02 = new News();
//    新建一個 新聞的新聞標題搜尋的輸入用變數
      News news03 = new News();
//    新建一個 新聞的新聞副標題搜尋的輸入用變數
      News news04 = new News();
//    新建一個 新聞的開始時間搜尋的輸入用變數
      News news05 = new News();
//    新建一個 新聞的結束時間搜尋的輸入用變數
      News news06 = new News();
//    新建一個 新聞的複合搜尋的輸入用變數
      MultipleSearch news07 = new MultipleSearch();
//  (結束)新聞的搜尋的初始化設定

//  (開始)新聞的搜尋的初始值設定
//    新聞的分類搜尋
      if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
          news01.setCategory(lastKeyWordStr);
//    新聞的子分類搜尋
      if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
          news02.setSubCategory(lastKeyWordStr);  
//    新聞的新聞標題搜尋
      if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
          news03.setNewsTitle(lastKeyWordStr);  
//    新聞的新聞副標題搜尋
      if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
          news04.setNewsSubTitle(lastKeyWordStr);
//    新聞的開始時間搜尋
      if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
          news05.setReleaseTime(lastKeyWordStr);
//    新聞的結束時間搜尋
      if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
          news06.setReleaseTime(lastKeyWordStr);
//    新聞的複合搜尋
      if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
          news07 = lastKeyWordMultipleStr;
//  (結束)新聞的搜尋的初始值設定
      
//  (開始)新聞的搜尋的Thymeleaf傳入設定
//    新建一個 新聞的分類搜尋的輸入用變數
      model.addAttribute("news01", news01);
//    新建一個 新聞的子分類搜尋的輸入用變數
      model.addAttribute("news02", news02);
//    新建一個 新聞的新聞標題搜尋的輸入用變數
      model.addAttribute("news03", news03);
//    新建一個 新聞的新聞副標題搜尋的輸入用變數
      model.addAttribute("news04", news04);
//    新建一個 新聞的開始時間搜尋的輸入用變數
      model.addAttribute("news05", news05);
//    新建一個 新聞的結束時間搜尋的輸入用變數
      model.addAttribute("news06", news06);
//    新建一個 新聞的結束時間搜尋的輸入用變數
      model.addAttribute("news07", news07);
//  (結束)新聞的搜尋的Thymeleaf傳入設定

//  (開始)分類列表的初始化設定
//    將 新聞的分類選擇框用的List 傳到Thymeleaf
      model.addAttribute("categoryList", categoryListInitializer());
//  (結束)分類列表的初始化設定
      
//  (開始)子分類列表的初始化設定
//    將 新聞的子分類選擇框用的List 傳到Thymeleaf
      model.addAttribute("subCategoryList", subCategoryListInitializer());
//  (結束)子分類列表的初始化設定
    
//  (開始)勾選設定
//    勾選結果儲存用的變數 設定為初始值
//      checkedRes = new CheckedRes();
//    將 勾選結果儲存用的變數 傳到Thymeleaf
      model.addAttribute("checkedRes", checkedRes);
//  (結束)勾選設定

//  (開始)分頁設定
//    將 空的目前選擇的分頁 改為目前的頁數
      int pageNum = pageNumTemp;
//    將 每頁最大顯示結果的筆數 改為10筆
//      int pageSize = 10;
//    將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
      model.addAttribute("pageNum", pageNum);
//    將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
      model.addAttribute("pageSize", pageSize);
//  (結束)分頁設定

//  (開始)新聞搜尋結果的設定
//    將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
      model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//    將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
      model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//  (結束)新聞搜尋結果的設定

//    返回 客戶端的新聞主頁
      return "client_home";
    }

//  客戶端的新聞詳細頁面
    @RequestMapping("/client_news_zoom")
    public String ClientNewsZoom(Model model) {
//      (開始)新聞的搜尋的初始化設定
//        新建一個 新聞的分類搜尋的輸入用變數
          News news01 = new News();
//        新建一個 新聞的子分類搜尋的輸入用變數
          News news02 = new News();
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          News news03 = new News();
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          News news04 = new News();
//        新建一個 新聞的開始時間搜尋的輸入用變數
          News news05 = new News();
//        新建一個 新聞的結束時間搜尋的輸入用變數
          News news06 = new News();
//        新建一個 新聞的複合搜尋的輸入用變數
          MultipleSearch news07 = new MultipleSearch();
//      (結束)新聞的搜尋的初始化設定

//      (開始)新聞的搜尋的初始值設定
//        新聞的分類搜尋
          if(lastSearch == 1)// 取得 新聞的分類的搜尋結果
              news01.setCategory(lastKeyWordStr);
//        新聞的子分類搜尋
          if(lastSearch == 2)// 取得 新聞的子分類的搜尋結果
              news02.setSubCategory(lastKeyWordStr);  
//        新聞的新聞標題搜尋
          if(lastSearch == 3)// 取得 新聞的新聞標題的搜尋結果
              news03.setNewsTitle(lastKeyWordStr);  
//        新聞的新聞副標題搜尋
          if(lastSearch == 4)// 取得 新聞的新聞副標題的搜尋結果
              news04.setNewsSubTitle(lastKeyWordStr);
//        新聞的開始時間搜尋
          if(lastSearch == 5)// 取得 新聞的開始時間的搜尋結果
              news05.setReleaseTime(lastKeyWordStr);
//        新聞的結束時間搜尋
          if(lastSearch == 6)// 取得 新聞的結束時間的搜尋結果
              news06.setReleaseTime(lastKeyWordStr);
//        新聞的複合搜尋
          if(lastSearch == 7)// 取得 新聞的結束時間的搜尋結果
              news07 = lastKeyWordMultipleStr;
//      (結束)新聞的搜尋的初始值設定
          
//      (開始)新聞的搜尋的Thymeleaf傳入設定
//        新建一個 新聞的分類搜尋的輸入用變數
          model.addAttribute("news01", news01);
//        新建一個 新聞的子分類搜尋的輸入用變數
          model.addAttribute("news02", news02);
//        新建一個 新聞的新聞標題搜尋的輸入用變數
          model.addAttribute("news03", news03);
//        新建一個 新聞的新聞副標題搜尋的輸入用變數
          model.addAttribute("news04", news04);
//        新建一個 新聞的開始時間搜尋的輸入用變數
          model.addAttribute("news05", news05);
//        新建一個 新聞的結束時間搜尋的輸入用變數
          model.addAttribute("news06", news06);
//        新建一個 新聞的複合搜尋的輸入用變數
          model.addAttribute("news07", news07);
//      (結束)新聞的搜尋的Thymeleaf傳入設定

//      (開始)分類列表的初始化設定
//        將 新聞的分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("categoryList", categoryListInitializer());
//      (結束)分類列表的初始化設定
          
//      (開始)子分類列表的初始化設定
//        將 新聞的子分類選擇框用的List 傳到Thymeleaf
          model.addAttribute("subCategoryList", subCategoryListInitializer());
//      (結束)子分類列表的初始化設定
        
//      (開始)勾選設定
//        新建一個 勾選結果儲存用的變數
          CheckedRes checkedRes = new CheckedRes();
//        將 勾選結果儲存用的變數 傳到Thymeleaf
          model.addAttribute("checkedRes", checkedRes);
//      (結束)勾選設定

//      (開始)分頁設定
          int pageNum = 0;
//        將 每頁最大顯示結果的筆數 改為10筆
          int pageSize = 10;
//        將 目前所選擇的頁數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageNum", pageNum);
//        將 每頁最大顯示結果的筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("pageSize", pageSize);
//      (結束)分頁設定

//      (開始)新聞搜尋結果的設定
//        將 新聞的搜尋結果的Page(顯示用) 傳到Thymeleaf
          model.addAttribute("newsPage", NewsSearch(pageNum, pageSize));
//        將 新聞的搜尋結果的Page的總筆數(判斷用) 傳到Thymeleaf
          model.addAttribute("newsPageSize", NewsSearch(pageNum, pageSize).getNumberOfElements());
//      (結束)新聞搜尋結果的設定

//      未勾選 則返回客戶端主頁
        if(checkedResTemp == null)
            return "client_home";
//      勾選2個以上 則返回客戶端主頁
        if(checkedResTempCount >= 2)
            return "client_home";
//      找到勾選的新聞
        int counter0 = 0;
        int counter = 0;
        if(checkedResTemp.isChecked1())
            counter0 = 1;
        if(checkedResTemp.isChecked2())
            counter0 = 2;
        if(checkedResTemp.isChecked3())
            counter0 = 3;
        if(checkedResTemp.isChecked4())
            counter0 = 4;
        if(checkedResTemp.isChecked5())
            counter0 = 5;
        if(checkedResTemp.isChecked6())
            counter0 = 6;
        if(checkedResTemp.isChecked7())
            counter0 = 7;
        if(checkedResTemp.isChecked8())
            counter0 = 8;
        if(checkedResTemp.isChecked9())
            counter0 = 9;
        if(checkedResTemp.isChecked10())
            counter0 = 10;
        if(checkedResTemp.isChecked11())
            counter0 = 11;
        if(checkedResTemp.isChecked12())
            counter0 = 12;
        if(checkedResTemp.isChecked13())
            counter0 = 13;
        if(checkedResTemp.isChecked14())
            counter0 = 14;
        if(checkedResTemp.isChecked15())
            counter0 = 15;
        if(checkedResTemp.isChecked16())
            counter0 = 16;
        if(checkedResTemp.isChecked17())
            counter0 = 17;
        if(checkedResTemp.isChecked18())
            counter0 = 18;
        if(checkedResTemp.isChecked19())
            counter0 = 19;
        if(checkedResTemp.isChecked20())
            counter0 = 20;
        if(checkedResTemp.isChecked21())
            counter0 = 21;
        if(checkedResTemp.isChecked22())
            counter0 = 22;
        if(checkedResTemp.isChecked23())
            counter0 = 23;
        if(checkedResTemp.isChecked24())
            counter0 = 24;
        if(checkedResTemp.isChecked25())
            counter0 = 25;
        if(checkedResTemp.isChecked26())
            counter0 = 26;
        if(checkedResTemp.isChecked27())
            counter0 = 27;
        if(checkedResTemp.isChecked28())
            counter0 = 28;
        if(checkedResTemp.isChecked29())
            counter0 = 29;
        if(checkedResTemp.isChecked30())
            counter0 = 30;
        if(checkedResTemp.isChecked31())
            counter0 = 31;
        if(checkedResTemp.isChecked32())
            counter0 = 32;
        if(checkedResTemp.isChecked33())
            counter0 = 33;
        if(checkedResTemp.isChecked34())
            counter0 = 34;
        if(checkedResTemp.isChecked35())
            counter0 = 35;
        if(checkedResTemp.isChecked36())
            counter0 = 36;
        if(checkedResTemp.isChecked37())
            counter0 = 37;
        if(checkedResTemp.isChecked38())
            counter0 = 38;
        if(checkedResTemp.isChecked39())
            counter0 = 39;
        if(checkedResTemp.isChecked40())
            counter0 = 40;
        if(checkedResTemp.isChecked41())
            counter0 = 41;
        if(checkedResTemp.isChecked42())
            counter0 = 42;
        if(checkedResTemp.isChecked43())
            counter0 = 43;
        if(checkedResTemp.isChecked44())
            counter0 = 44;
        if(checkedResTemp.isChecked45())
            counter0 = 45;
        if(checkedResTemp.isChecked46())
            counter0 = 46;
        if(checkedResTemp.isChecked47())
            counter0 = 47;
        if(checkedResTemp.isChecked48())
            counter0 = 48;
        if(checkedResTemp.isChecked49())
            counter0 = 49;
        if(checkedResTemp.isChecked50())
            counter0 = 50;
        if(checkedResTemp.isChecked51())
            counter0 = 51;
        if(checkedResTemp.isChecked52())
            counter0 = 52;
        if(checkedResTemp.isChecked53())
            counter0 = 53;
        if(checkedResTemp.isChecked54())
            counter0 = 54;
        if(checkedResTemp.isChecked55())
            counter0 = 55;
        if(checkedResTemp.isChecked56())
            counter0 = 56;
        if(checkedResTemp.isChecked57())
            counter0 = 57;
        if(checkedResTemp.isChecked58())
            counter0 = 58;
        if(checkedResTemp.isChecked59())
            counter0 = 59;
        if(checkedResTemp.isChecked60())
            counter0 = 60;
        if(checkedResTemp.isChecked61())
            counter0 = 61;
        if(checkedResTemp.isChecked62())
            counter0 = 62;
        if(checkedResTemp.isChecked63())
            counter0 = 63;
        if(checkedResTemp.isChecked64())
            counter0 = 64;
        if(checkedResTemp.isChecked65())
            counter0 = 65;
        if(checkedResTemp.isChecked66())
            counter0 = 66;
        if(checkedResTemp.isChecked67())
            counter0 = 67;
        if(checkedResTemp.isChecked68())
            counter0 = 68;
        if(checkedResTemp.isChecked69())
            counter0 = 69;
        if(checkedResTemp.isChecked70())
            counter0 = 70;
        if(checkedResTemp.isChecked71())
            counter0 = 71;
        if(checkedResTemp.isChecked72())
            counter0 = 72;
        if(checkedResTemp.isChecked73())
            counter0 = 73;
        if(checkedResTemp.isChecked74())
            counter0 = 74;
        if(checkedResTemp.isChecked75())
            counter0 = 75;
        if(checkedResTemp.isChecked76())
            counter0 = 76;
        if(checkedResTemp.isChecked77())
            counter0 = 77;
        if(checkedResTemp.isChecked78())
            counter0 = 78;
        if(checkedResTemp.isChecked79())
            counter0 = 79;
        if(checkedResTemp.isChecked80())
            counter0 = 80;
        if(checkedResTemp.isChecked81())
            counter0 = 81;
        if(checkedResTemp.isChecked82())
            counter0 = 82;
        if(checkedResTemp.isChecked83())
            counter0 = 83;
        if(checkedResTemp.isChecked84())
            counter0 = 84;
        if(checkedResTemp.isChecked85())
            counter0 = 85;
        if(checkedResTemp.isChecked86())
            counter0 = 86;
        if(checkedResTemp.isChecked87())
            counter0 = 87;
        if(checkedResTemp.isChecked88())
            counter0 = 88;
        if(checkedResTemp.isChecked89())
            counter0 = 89;
        if(checkedResTemp.isChecked90())
            counter0 = 90;
        if(checkedResTemp.isChecked91())
            counter0 = 91;
        if(checkedResTemp.isChecked92())
            counter0 = 92;
        if(checkedResTemp.isChecked93())
            counter0 = 93;
        if(checkedResTemp.isChecked94())
            counter0 = 94;
        if(checkedResTemp.isChecked95())
            counter0 = 95;
        if(checkedResTemp.isChecked96())
            counter0 = 96;
        if(checkedResTemp.isChecked97())
            counter0 = 97;
        if(checkedResTemp.isChecked98())
            counter0 = 98;
        if(checkedResTemp.isChecked99())
            counter0 = 99;
        if(checkedResTemp.isChecked100())
            counter0 = 100;
        News news = new News();
        for (News item : NewsListTemp) {
            counter++;
            if(counter == counter0) {
                news = item;
                break;
            }
        }
        model.addAttribute("news", news);
//      勾選結果的初始化
        checkedResTemp = null;
//      跳轉到客戶端的新聞詳細頁面
        return "client_news_zoom";
    }

}

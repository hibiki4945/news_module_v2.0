package com.example.news_module.service.impl;

import com.example.news_module.constants.RtnCode;
import com.example.news_module.entity.Category;
import com.example.news_module.entity.News;
import com.example.news_module.repository.CategoryDao;
import com.example.news_module.repository.NewsDao;
import com.example.news_module.repository.SubCategoryDao;
import com.example.news_module.service.ifs.MainService;
import com.example.news_module.vo.CategoryAddResponse;
import com.example.news_module.vo.NewsAddResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MainServiceImpl implements MainService{

    @Autowired
    private NewsDao newsDao;
    
    @Autowired
    private CategoryDao categoryDao;
    
    @Autowired
    private SubCategoryDao subCategoryDao;
    
    @Override
    public NewsAddResponse newsAddCheck(News news) {
        
//      判斷'資料'是否為空
        if(news == null) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.DATA_EMPTY_ERROR.getCode(), RtnCode.DATA_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'分類'是否為空
        if(news.getCategory() == null || news.getCategory().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.CATEGORY_EMPTY_ERROR.getCode(), RtnCode.CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否為空
        if(news.getSubCategory() == null || news.getSubCategory().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.SUB_CATEGORY_EMPTY_ERROR.getCode(), RtnCode.SUB_CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'新聞標題'是否為空
        if(news.getNewsTitle() == null || news.getNewsTitle().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_TITLE_EMPTY_ERROR.getCode(), RtnCode.NEWS_TITLE_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'新聞副標題'是否為空
        if(news.getNewsSubTitle() == null || news.getNewsSubTitle().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_SUB_TITLE_EMPTY_ERROR.getCode(), RtnCode.NEWS_SUB_TITLE_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'發布時間'是否格式錯誤
        if(!news.getReleaseTime().matches("[\\d]{4}-[\\d]{2}-[\\d]{2}")) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.RELEASE_TIME_FORMAT_ERROR.getCode(), RtnCode.RELEASE_TIME_FORMAT_ERROR.getMessage(), null);
        }
//      判斷'內文'是否為空
        if(news.getContent() == null || news.getContent().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.CONTENT_EMPTY_ERROR.getCode(), RtnCode.CONTENT_EMPTY_ERROR.getMessage(), null);
        }
        
//      取得 當前時間
        Date date = new Date();
//      將 date 設定到 news的BuildTime
        news.setBuildTime(date);
        
//      判斷'內文'是否已存在(以內文 作為 判斷新聞是否重複的條件)
        if(newsDao.existsByContent(news.getContent())) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_EXISTS_ERROR.getCode(), RtnCode.NEWS_EXISTS_ERROR.getMessage(), null);
        }
        return new NewsAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), news);
        
    }

    @Override
    public NewsAddResponse newsAdd(NewsAddResponse newsAddResponse) {


        if(!newsAddResponse.getCode().matches("200")) {
            return new NewsAddResponse(RtnCode.DATA_ERROR.getCode(), RtnCode.DATA_ERROR.getMessage(), null);
        }
        try {
//          將 新聞資料(news) 存到資料庫
            News res = newsDao.save(newsAddResponse.getNews());
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), newsAddResponse.getNews());
        } catch (Exception e) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.DAO_ERROR.getCode(), RtnCode.DAO_ERROR.getMessage(), null);
        }
    }

    @Override
    public CategoryAddResponse categoryAdd(Category category) {
        
//      判斷'資料'是否為空
        if(category == null) {
//          返回(CategoryAddResponse型別)訊息
            return new CategoryAddResponse(RtnCode.DATA_EMPTY_ERROR.getCode(), RtnCode.DATA_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'分類'是否為空
        if(category.getCategory() == null || category.getCategory().isBlank()) {
//          返回(CategoryAddResponse型別)訊息
            return new CategoryAddResponse(RtnCode.CATEGORY_EMPTY_ERROR.getCode(), RtnCode.CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'分類'是否已存在
        if(categoryDao.existsById(category.getCategory())) {
//          返回(CategoryAddResponse型別)訊息
            return new CategoryAddResponse(RtnCode.CATEGORY_EXISTS_ERROR.getCode(), RtnCode.CATEGORY_EXISTS_ERROR.getMessage(), null);
        }
        
//      取得 當前時間
        Date date = new Date();
//      將 date 設定到 news的BuildTime
        category.setBuildTime(date);
        
        category.setNewsCount(0);

        Category res = categoryDao.save(category);
        
        return new CategoryAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), res);
        
    }
    
}

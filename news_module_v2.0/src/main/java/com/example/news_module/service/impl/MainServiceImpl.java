package com.example.news_module.service.impl;

import com.example.news_module.constants.RtnCode;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MainServiceImpl implements MainService{

    @Autowired
    private NewsDao newsDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private SubCategoryDao subCategoryDao;
//  新聞預新增
    @Override
    public NewsAddResponse newsAddCheck(News news) {
        
//      判斷'資料'是否為空
        if(news == null) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.DATA_EMPTY_ERROR.getCode(), RtnCode.DATA_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'分類'是否為空
        if(news.getCategory() == null || news.getCategory().isBlank()) {
//          if(news.getCategory().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.CATEGORY_EMPTY_ERROR.getCode(), RtnCode.CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否為空
        if(news.getSubCategory() == null || news.getSubCategory().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.SUB_CATEGORY_EMPTY_ERROR.getCode(), RtnCode.SUB_CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'新聞標題'是否為空
//      * 不需加上null的判斷
//      * 可改成StringUtils.isBlank()
//        if(news.getNewsTitle() == null || news.getNewsTitle().isBlank()) {
        if(news.getNewsTitle().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_TITLE_EMPTY_ERROR.getCode(), RtnCode.NEWS_TITLE_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'新聞標題'是否超過長度上限
        if(news.getNewsTitle().length() > 100) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_TITLE_OVER_LENGTH_ERROR.getCode(), RtnCode.NEWS_TITLE_OVER_LENGTH_ERROR.getMessage(), null);
        }
//      判斷'新聞副標題'是否為空
        if(news.getNewsSubTitle() == null || news.getNewsSubTitle().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_SUB_TITLE_EMPTY_ERROR.getCode(), RtnCode.NEWS_SUB_TITLE_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'新聞副標題'是否超過長度上限
        if(news.getNewsSubTitle().length() > 20) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_SUB_TITLE_OVER_LENGTH_ERROR.getCode(), RtnCode.NEWS_SUB_TITLE_OVER_LENGTH_ERROR.getMessage(), null);
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
//      判斷'內文'是否超過長度上限
        if(news.getContent().length() > 1000) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.CONTENT_OVER_LENGTH_ERROR.getCode(), RtnCode.CONTENT_OVER_LENGTH_ERROR.getMessage(), null);
        }
        
        
        
//      判斷'內文'是否已存在(以內文 作為 判斷新聞是否重複的條件)
        if(newsDao.existsByContent(news.getContent())) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_EXISTS_ERROR.getCode(), RtnCode.NEWS_EXISTS_ERROR.getMessage(), null);
        }

//      取得 當前時間
        Date date = new Date();
//      將 date 設定到 news的BuildTime
        news.setBuildTime(date);
        
        return new NewsAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), news);
        
    }
//  新聞新增
    @Override
    public NewsAddResponse newsAdd(NewsAddResponse newsAddResponse) {

////      判斷預新增動作 是否正常
//        if(!newsAddResponse.getCode().matches("200")) {
//            return new NewsAddResponse(RtnCode.DATA_ERROR.getCode(), RtnCode.DATA_ERROR.getMessage(), null);
//        }
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
//  分類新增
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
//      判斷'分類'是否超過長度上限
        if(category.getCategory().length() > 20) {
//          返回(NewsAddResponse型別)訊息
            return new CategoryAddResponse(RtnCode.CATEGORY_OVER_LENGTH_ERROR.getCode(), RtnCode.CATEGORY_OVER_LENGTH_ERROR.getMessage(), null);
        }
//      判斷'分類'是否已存在
        if(categoryDao.existsByCategory(category.getCategory())) {
//          返回(CategoryAddResponse型別)訊息
            return new CategoryAddResponse(RtnCode.CATEGORY_EXISTS_ERROR.getCode(), RtnCode.CATEGORY_EXISTS_ERROR.getMessage(), null);
        }
        
//      取得 當前時間
        Date date = new Date();
//      將 date 設定到 news的BuildTime
        category.setBuildTime(date);
//      新聞數量的初始化
        category.setNewsCount(0);
//      新增分類
        Category res = categoryDao.save(category);
        if(res == null) {
            return new CategoryAddResponse(RtnCode.DAO_ERROR.getCode(), RtnCode.DAO_ERROR.getMessage(), null);
        }
//      回傳 成功訊息
        return new CategoryAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), res);
        
    }
//  子分類新增
    @Override
    public SubCategoryAddResponse subCategoryAdd(SubCategory subCategory) {

//      判斷'資料'是否為空
        if(subCategory == null) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.DATA_EMPTY_ERROR.getCode(), RtnCode.DATA_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'分類'是否為空
        if(subCategory.getCategory() == null || subCategory.getCategory().isBlank()) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.CATEGORY_EMPTY_ERROR.getCode(), RtnCode.CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否為空
        if(subCategory.getSubCategory() == null || subCategory.getSubCategory().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.SUB_CATEGORY_EMPTY_ERROR.getCode(), RtnCode.SUB_CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否超過長度上限(20)
        if(subCategory.getSubCategory().length() > 20) {
//          返回(NewsAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.SUB_CATEGORY_OVER_LENGTH_ERROR.getCode(), RtnCode.SUB_CATEGORY_OVER_LENGTH_ERROR.getMessage(), null);
        }
//      判斷'分類'是否不存在
        if(!categoryDao.existsByCategory(subCategory.getCategory())) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.CATEGORY_NOT_EXISTS_ERROR.getCode(), RtnCode.CATEGORY_NOT_EXISTS_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否存在
        if(subCategoryDao.existsByCategoryAndSubCategory(subCategory.getCategory(), subCategory.getSubCategory())) {
//        if(subCategoryDao.existsBySubCategory(subCategory.getSubCategory())) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.SUB_CATEGORY_EXISTS_ERROR.getCode(), RtnCode.SUB_CATEGORY_EXISTS_ERROR.getMessage(), null);
        }
        
//      取得 當前時間
        Date date = new Date();
//      將 date 設定到 news的BuildTime
        subCategory.setBuildTime(date);
//      新聞數量的初始化
        subCategory.setSubCategoryNewsCount(0);
//      新增子分類
        SubCategory res = subCategoryDao.save(subCategory);
//      回傳 成功訊息
        return new SubCategoryAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), res);
        
    }
//  用分類找新聞
    @Override
    public Page<News> findPageByCategory(boolean sortDescFlag, int pageNum, int pageSize, String category) {
//             宣告 排序用變數 
               Sort sort = null;
//             當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//             當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
               if(sortDescFlag)
                   sort = Sort.by(Sort.Direction.DESC , "id");
               else
                   sort = Sort.by(Sort.Direction.ASC , "id");
               Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//             用分類找新聞
               Page<News> newsList = newsDao.findByCategory(pageable, category);
//             回傳 搜尋結果
               return newsList;
           }
//  用子分類找新聞
    @Override
    public Page<News> findPageBySubCategory(boolean sortDescFlag, int pageNum, int pageSize, String subCategory) {
//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      用子分類找新聞
        Page<News> newsList = newsDao.findBySubCategory(pageable, subCategory);
//      回傳 搜尋結果
        return newsList;
    }
//  用新聞標題的關鍵字找新聞
    @Override
    public Page<News> findPageByNewsTitle(boolean sortDescFlag, int pageNum, int pageSize, String newsTitle) {
//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      用新聞標題找新聞
        Page<News> newsList = newsDao.findByNewsTitle(pageable, newsTitle);
//      回傳 搜尋結果
        return newsList;
    }
//  用新聞副標題的關鍵字找新聞
    @Override
    public Page<News> findPageByNewsSubTitle(boolean sortDescFlag, int pageNum, int pageSize, String newsSubTitle) {
//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      用新聞副標題找新聞
        Page<News> newsList = newsDao.findByNewsSubTitle(pageable, newsSubTitle);
//      回傳 搜尋結果
        return newsList;
    }
//  用大於發布時間找新聞
    @Override
    public Page<News> findPageByReleaseTimeGreater(boolean sortDescFlag, int pageNum, int pageSize, String date) {
//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      用大於發布時間找新聞
        Page<News> newsList = newsDao.findByReleaseTimeGreaterThanEqual(pageable, date);
//      回傳 搜尋結果
        return newsList;
    }
//  用小於發布時間找新聞
    @Override
    public Page<News> findPageByReleaseTimeLess(boolean sortDescFlag, int pageNum, int pageSize, String date) {
//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      用小於發布時間找新聞
        Page<News> newsList = newsDao.findByReleaseTimeLessThanEqual(pageable, date);
//      回傳 搜尋結果
        return newsList;
    }
//  用複合條件找新聞
    @Override
    public Page<News> findPageByNewsByInput(boolean sortDescFlag, int pageNum, int pageSize, String Category, String SubCategory, String NewsTitle, String NewsSubTitle, String ReleaseTimeStart, String ReleaseTimeEnd, String BuildTimeStart, String BuildTimeEnd) {

//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      用複合條件找新聞
//        Page<News> newsList = newsDao.searchNewsByInput(pageable, Category, SubCategory, NewsTitle, NewsSubTitle, ReleaseTime);
        Page<News> newsList = newsDao.searchNewsByInput(pageable, Category, SubCategory, NewsTitle, NewsSubTitle, ReleaseTimeStart, ReleaseTimeEnd, BuildTimeStart, BuildTimeEnd);
//      回傳 搜尋結果
        return newsList;
    }
//  找所有新聞(依照發布日 降冪排序)
    @Override
    public Page<News> findPageAll(boolean sortDescFlag, int pageNum, int pageSize) {

//      宣告 排序用變數 
        Sort sort = null;
//      依照發布日 降冪排序
        sort = Sort.by(Sort.Direction.DESC , "releaseTime");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      用複合條件找新聞
//        Page<News> newsList = newsDao.searchNewsByInput(pageable, Category, SubCategory, NewsTitle, NewsSubTitle, ReleaseTime);
        Page<News> newsList = newsDao.findAll(pageable);
//      回傳 搜尋結果
        return newsList;
    }
//  新聞預編輯
    @Override
    public NewsAddResponse newsEditCheck(News news) {
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
//      判斷'新聞標題'是否超過長度上限
        if(news.getNewsTitle().length() > 100) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_TITLE_OVER_LENGTH_ERROR.getCode(), RtnCode.NEWS_TITLE_OVER_LENGTH_ERROR.getMessage(), null);
        }
//      判斷'新聞副標題'是否為空
        if(news.getNewsSubTitle() == null || news.getNewsSubTitle().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_SUB_TITLE_EMPTY_ERROR.getCode(), RtnCode.NEWS_SUB_TITLE_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'新聞副標題'是否超過長度上限
        if(news.getNewsSubTitle().length() > 20) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.NEWS_SUB_TITLE_OVER_LENGTH_ERROR.getCode(), RtnCode.NEWS_SUB_TITLE_OVER_LENGTH_ERROR.getMessage(), null);
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
//      判斷'內文'是否超過長度上限
        if(news.getContent().length() > 1000) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.CONTENT_OVER_LENGTH_ERROR.getCode(), RtnCode.CONTENT_OVER_LENGTH_ERROR.getMessage(), null);
        }
        
//      取得 當前時間
        Date date = new Date();
//      將 date 設定到 news的BuildTime
        news.setBuildTime(date);
        
////      判斷'內文'是否已存在(以內文 作為 判斷新聞是否重複的條件)
//        if(newsDao.existsByContent(news.getContent())) {
////          返回(NewsAddResponse型別)訊息
//            return new NewsAddResponse(RtnCode.NEWS_EXISTS_ERROR.getCode(), RtnCode.NEWS_EXISTS_ERROR.getMessage(), null);
//        }
//      回傳 成功訊息
        return new NewsAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), news);
        
    }
//  新聞編輯
    @Override
    public NewsAddResponse newsEdit(NewsAddResponse newsAddResponse) {
//      確認 新聞的預編輯動作 是否正常
        if(!newsAddResponse.getCode().matches("200")) {
            return new NewsAddResponse(RtnCode.DATA_ERROR.getCode(), RtnCode.DATA_ERROR.getMessage(), null);
        }
        try {
//          將 新聞資料(news) 存到資料庫
//            News res = newsDao.save(newsAddResponse.getNews());
             int res = newsDao.updateNewsById(newsAddResponse.getNews().getId(), newsAddResponse.getNews().getCategory(), newsAddResponse.getNews().getSubCategory(), newsAddResponse.getNews().getNewsTitle(), newsAddResponse.getNews().getNewsSubTitle(), newsAddResponse.getNews().getReleaseTime(), newsAddResponse.getNews().getContent());
//          確認 回傳結果(res)是否正常
            if(!(res > 0)) {
                return new NewsAddResponse(RtnCode.DATA_ERROR.getCode(), RtnCode.DATA_ERROR.getMessage(), null);
            }
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), newsAddResponse.getNews());
        } catch (Exception e) {
//          返回(NewsAddResponse型別)訊息
            return new NewsAddResponse(RtnCode.DAO_ERROR.getCode(), RtnCode.DAO_ERROR.getMessage(), null);
        }
        
    }
//  刪除新聞
    @Override
    public void newsDelete(int id) {
        try {
//          將 新聞資料(news) 存到資料庫
//            News res = newsDao.save(newsAddResponse.getNews());
             newsDao.deleteById(id);
//          返回(NewsAddResponse型別)訊息
//            return new NewsAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), newsAddResponse.getNews());
        } catch (Exception e) {
//          返回(NewsAddResponse型別)訊息
//            return new NewsAddResponse(RtnCode.DAO_ERROR.getCode(), RtnCode.DAO_ERROR.getMessage(), null);
        }
    }
//  搜尋所有分類
    @Override
    public Page<Category> findCategoryPageByAll(boolean sortDescFlag, int pageNum, int pageSize) {
//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      搜尋所有分類
        Page<Category> categoryList = categoryDao.findAll(pageable);
//      回傳 搜尋結果
        return categoryList;
    }
//  分類編輯
    @Override
    public CategoryAddResponse categoryEdit(Category category) {
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
//      判斷'分類'是否超過長度上限
        if(category.getCategory().length() > 20) {
//          返回(NewsAddResponse型別)訊息
            return new CategoryAddResponse(RtnCode.CATEGORY_OVER_LENGTH_ERROR.getCode(), RtnCode.CATEGORY_OVER_LENGTH_ERROR.getMessage(), null);
        }
//      判斷'分類'是否已存在
        if(categoryDao.existsByCategory(category.getCategory())) {
//          返回(CategoryAddResponse型別)訊息
            return new CategoryAddResponse(RtnCode.CATEGORY_EXISTS_ERROR.getCode(), RtnCode.CATEGORY_EXISTS_ERROR.getMessage(), null);
        }
//      更新分類
        int res = categoryDao.updateCategoryById(category.getId(), category.getCategory(), category.getNewsCount());
//      確認 更新分類是否成功
        if(!(res > 0)) {
            return new CategoryAddResponse(RtnCode.DATA_ERROR.getCode(), RtnCode.DATA_ERROR.getMessage(), null);
        }
//      回傳 成功訊息
        return new CategoryAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), null);
        
    }
//  更新分類的新聞數量
    @Override
    public CategoryAddResponse categoryEditNewsCount(Category category) {

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
//      判斷'分類'是否超過長度上限
        if(category.getCategory().length() > 20) {
//          返回(NewsAddResponse型別)訊息
            return new CategoryAddResponse(RtnCode.CATEGORY_OVER_LENGTH_ERROR.getCode(), RtnCode.CATEGORY_OVER_LENGTH_ERROR.getMessage(), null);
        }

//      分類的數量更新
        int res = categoryDao.updateCategoryById(category.getId(), category.getCategory(), category.getNewsCount());
//      確認 分類的數量更新是否成功
        if(!(res > 0)) {
            return new CategoryAddResponse(RtnCode.DATA_ERROR.getCode(), RtnCode.DATA_ERROR.getMessage(), null);
        }
//      回傳 成功訊息
        return new CategoryAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), null);
        
    }
//  刪除分類
    @Override
    public void categoryDelete(int id) {
        try {
//          將 新聞資料(news) 存到資料庫
//            News res = newsDao.save(newsAddResponse.getNews());
             categoryDao.deleteById(id);
//          返回(NewsAddResponse型別)訊息
//            return new NewsAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), newsAddResponse.getNews());
        } catch (Exception e) {
//          返回(NewsAddResponse型別)訊息
//            return new NewsAddResponse(RtnCode.DAO_ERROR.getCode(), RtnCode.DAO_ERROR.getMessage(), null);
        }
        
    }
//  搜尋所有子分類
    @Override
    public Page<SubCategory> findSubCategoryPageByAll(boolean sortDescFlag, int pageNum, int pageSize) {
//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      搜尋所有子分類
        Page<SubCategory> subCategoryList = subCategoryDao.findAll(pageable);
//      回傳 搜尋結果
        return subCategoryList;
    }
//  搜尋子分類(用分類)
    @Override
    public Page<SubCategory> findSubCategoryPageByCategory(boolean sortDescFlag, int pageNum, int pageSize, String category) {
//      宣告 排序用變數 
        Sort sort = null;
//      當降冪排序的Flag為true 則設為降冪排序(根據新聞的id 做排序)
//      當降冪排序的Flag為false 則設為升冪排序(根據新聞的id 做排序)
        if(sortDescFlag)
            sort = Sort.by(Sort.Direction.DESC , "id");
        else
            sort = Sort.by(Sort.Direction.ASC , "id");
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
//      搜尋子分類(用分類)
        Page<SubCategory> subCategoryList = subCategoryDao.findByCategory(pageable, category);
//      回傳 搜尋結果
        return subCategoryList;
    }
//  編輯子分類
    @Override
    public SubCategoryAddResponse subCategoryEdit(SubCategory subCategory) {

//      判斷'資料'是否為空
        if(subCategory == null) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.DATA_EMPTY_ERROR.getCode(), RtnCode.DATA_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'分類'是否為空
        if(subCategory.getCategory() == null || subCategory.getCategory().isBlank()) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.CATEGORY_EMPTY_ERROR.getCode(), RtnCode.CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否為空
        if(subCategory.getSubCategory() == null || subCategory.getSubCategory().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.SUB_CATEGORY_EMPTY_ERROR.getCode(), RtnCode.SUB_CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否超過長度上限(20)
        if(subCategory.getSubCategory().length() > 20) {
//          返回(NewsAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.SUB_CATEGORY_OVER_LENGTH_ERROR.getCode(), RtnCode.SUB_CATEGORY_OVER_LENGTH_ERROR.getMessage(), null);
        }
//      判斷'分類'是否不存在
        if(!categoryDao.existsByCategory(subCategory.getCategory())) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.CATEGORY_NOT_EXISTS_ERROR.getCode(), RtnCode.CATEGORY_NOT_EXISTS_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否存在(記得改回(subCategoryDao.existsById))
        if(subCategoryDao.existsByCategoryAndSubCategory(subCategory.getCategory(), subCategory.getSubCategory())) {
//        if(subCategoryDao.existsBySubCategory(subCategory.getSubCategory())) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.NO_CHANGE_ERROR.getCode(), RtnCode.NO_CHANGE_ERROR.getMessage(), null);
        }
        
//      更新子分類
        int res = subCategoryDao.updateSubCategoryById(subCategory.getId(), subCategory.getCategory(), subCategory.getSubCategory());
//      確認 更新是否正常
        if(!(res > 0)) {
            return new SubCategoryAddResponse(RtnCode.DATA_ERROR.getCode(), RtnCode.DATA_ERROR.getMessage(), null);
        }
//      回傳 成功訊息
        return new SubCategoryAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), null);
        
    }
//  刪除子分類
    @Override
    public void subCategoryDelete(int id) {
        try {
             subCategoryDao.deleteById(id);
//          返回(NewsAddResponse型別)訊息
//            return new NewsAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), newsAddResponse.getNews());
        } catch (Exception e) {
//          返回(NewsAddResponse型別)訊息
//            return new NewsAddResponse(RtnCode.DAO_ERROR.getCode(), RtnCode.DAO_ERROR.getMessage(), null);
        }
        
    }
//  搜尋新聞(用分類)
    @Override
    public List<News> findByCategory(String category) {
        List<News> newsList = newsDao.findByCategory(category);
        return newsList;
    }
//  搜尋新聞(用子分類)
    @Override
    public List<News> findBySubCategory(String subCategory) {
        List<News> newsList = newsDao.findBySubCategory(subCategory);
        return newsList;
    }
//  搜尋所有分類
    @Override
    public List<Category> findCategoryByAll() {
        List<Category> categoryList = categoryDao.findAll();
        return categoryList;
    }
//  搜尋子分類(用分類)
    @Override
    public List<SubCategory> findSubCategoryByCategory(String category) {
        List<SubCategory> subCategoryList = subCategoryDao.findByCategory(category);
        return subCategoryList;
    }
//  搜尋新聞(用分類+子分類)
    @Override
    public List<News> findByCategoryAndSubCategory(String category, String subCategory) {
        List<News> newsList = newsDao.findByCategoryAndSubCategory(category, subCategory);
        return newsList;
    }
//  更新子分類的新聞數量
    @Override
    public SubCategoryAddResponse subCategoryEditNewsCount(SubCategory subCategory) {

//      判斷'資料'是否為空
        if(subCategory == null) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.DATA_EMPTY_ERROR.getCode(), RtnCode.DATA_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'分類'是否為空
        if(subCategory.getCategory() == null || subCategory.getCategory().isBlank()) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.CATEGORY_EMPTY_ERROR.getCode(), RtnCode.CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否為空
        if(subCategory.getSubCategory() == null || subCategory.getSubCategory().isBlank()) {
//          返回(NewsAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.SUB_CATEGORY_EMPTY_ERROR.getCode(), RtnCode.SUB_CATEGORY_EMPTY_ERROR.getMessage(), null);
        }
//      判斷'子分類'是否超過長度上限(20)
        if(subCategory.getSubCategory().length() > 20) {
//          返回(NewsAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.SUB_CATEGORY_OVER_LENGTH_ERROR.getCode(), RtnCode.SUB_CATEGORY_OVER_LENGTH_ERROR.getMessage(), null);
        }
//      判斷'分類'是否不存在
        if(!categoryDao.existsByCategory(subCategory.getCategory())) {
//          返回(CategoryAddResponse型別)訊息
            return new SubCategoryAddResponse(RtnCode.CATEGORY_NOT_EXISTS_ERROR.getCode(), RtnCode.CATEGORY_NOT_EXISTS_ERROR.getMessage(), null);
        }
        
//      更新 子分類的新聞數量
        int res = subCategoryDao.updateSubCategoryNewsCountById(subCategory.getId(), subCategory.getCategory(), subCategory.getSubCategory(), subCategory.getSubCategoryNewsCount());
//      判斷 子分類的新聞數量的更新 是否成功
        if(!(res > 0)) {
            return new SubCategoryAddResponse(RtnCode.DATA_ERROR.getCode(), RtnCode.DATA_ERROR.getMessage(), null);
        }
//      回傳 成功訊息
        return new SubCategoryAddResponse(RtnCode.SUCCESSFUL.getCode(), RtnCode.SUCCESSFUL.getMessage(), null);
        
    }

}

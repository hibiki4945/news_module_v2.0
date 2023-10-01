package com.example.news_module.service.ifs;

import com.example.news_module.entity.Category;
import com.example.news_module.entity.News;
import com.example.news_module.entity.SubCategory;
import com.example.news_module.vo.CategoryAddResponse;
import com.example.news_module.vo.NewsAddResponse;
import com.example.news_module.vo.SubCategoryAddResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MainService {
//  新聞預新增
    public NewsAddResponse newsAddCheck(News news);
//  新聞新增
    public NewsAddResponse newsAdd(NewsAddResponse newsAddResponse);
//  分類新增
    public CategoryAddResponse categoryAdd(Category category);
//  子分類新增
    public SubCategoryAddResponse subCategoryAdd(SubCategory subCategory);
//  用分類找新聞
    public Page<News> findPageByCategory(boolean sortDescFlag, int pageNum, int pageSize, String category);
//  用分類找新聞
    public List<News> findByCategory(String category);
//  用子分類找新聞
    public Page<News> findPageBySubCategory(boolean sortDescFlag, int pageNum, int pageSize, String subCategory);
//  用子分類找新聞
    public List<News> findBySubCategory(String subCategory);
//  搜尋新聞(用分類+子分類)
    public List<News> findByCategoryAndSubCategory(String category, String subCategory);
//  用新聞標題的關鍵字找新聞
    public Page<News> findPageByNewsTitle(boolean sortDescFlag, int pageNum, int pageSize, String newsTitle);
//  用新聞副標題的關鍵字找新聞
    public Page<News> findPageByNewsSubTitle(boolean sortDescFlag, int pageNum, int pageSize, String newsSubTitle);
//  用大於發布時間找新聞
    public Page<News> findPageByReleaseTimeGreater(boolean sortDescFlag, int pageNum, int pageSize, String date);
//  用小於發布時間找新聞
    public Page<News> findPageByReleaseTimeLess(boolean sortDescFlag, int pageNum, int pageSize, String date);
//  用複合條件找新聞
    public Page<News> findPageByNewsByInput(boolean sortDescFlag, int pageNum, int pageSize, String Category, String SubCategory, String NewsTitle, String NewsSubTitle, String ReleaseTime);
//  新聞預編輯
    public NewsAddResponse newsEditCheck(News news);
//  新聞編輯
    public NewsAddResponse newsEdit(NewsAddResponse newsAddResponse);
//  刪除新聞
    public void newsDelete(int id);
    ///////////////////////////////////////////////
//  搜尋所有分類
    public Page<Category> findCategoryPageByAll(boolean sortDescFlag, int pageNum, int pageSize);
//  搜尋所有分類
    public List<Category> findCategoryByAll();
//  分類編輯
    public CategoryAddResponse categoryEdit(Category category);
//  分類編輯其新聞數量
    public CategoryAddResponse categoryEditNewsCount(Category category);
//  刪除分類
    public void categoryDelete(int id);
    
    ///////////////////////////////////////////////
//  搜尋所有子分類
    public Page<SubCategory> findSubCategoryPageByAll(boolean sortDescFlag, int pageNum, int pageSize);
//  搜尋所有子分類(用分類)
    public Page<SubCategory> findSubCategoryPageByCategory(boolean sortDescFlag, int pageNum, int pageSize, String category);
//  搜尋所有子分類(用分類)
    public List<SubCategory> findSubCategoryByCategory(String category);
//  子分類編輯
    public SubCategoryAddResponse subCategoryEdit(SubCategory subCategory);
//  更新子分類的新聞數量
    public SubCategoryAddResponse subCategoryEditNewsCount(SubCategory subCategory);
//  刪除子分類
    public void subCategoryDelete(int id);
    
    
}

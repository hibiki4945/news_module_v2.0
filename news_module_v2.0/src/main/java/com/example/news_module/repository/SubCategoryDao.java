package com.example.news_module.repository;

import com.example.news_module.entity.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubCategoryDao extends JpaRepository<SubCategory, Integer>{
//  判斷 分類是否存在
    public boolean existsByCategory(String category);
//  用分類搜尋子分類
    public List<SubCategory> findByCategory(String category);
//  用分類搜尋子分類
    public Page<SubCategory> findByCategory(Pageable pageable, String category);
//  判斷子分類是否已存在(用子分類)
    public boolean existsBySubCategory(String subCategory);
//  判斷子分類是否已存在(用分類+子分類)    
    public boolean existsByCategoryAndSubCategory(String category, String subCategory);
//  更新 子分類
    @Modifying
    @Transactional
    @Query(value = "update sub_category s"
            + " set s.category = :inputCategory, "
            + " s.sub_category = :inputSubCategory "
            + " where s.id = :inputId ", nativeQuery = true)
    public int updateSubCategoryById(@Param("inputId") int id,
                              @Param("inputCategory") String category,
                              @Param("inputSubCategory") String subCategory
                              );
//  更新 子分類(包括 子分類的新聞數量)
    @Modifying
    @Transactional
    @Query(value = "update sub_category s"
            + " set s.category = :inputCategory, "
            + " s.sub_category = :inputSubCategory, "
            + " s.sub_category_news_count = case when :inputSubCategoryNewsCount is null then 0 else :inputSubCategoryNewsCount end "
            + " where s.id = :inputId ", nativeQuery = true)
    public int updateSubCategoryNewsCountById(@Param("inputId") int id,
                              @Param("inputCategory") String category,
                              @Param("inputSubCategory") String subCategory,
                              @Param("inputSubCategoryNewsCount") int NewsCount
                              );
//  分類更新時 更新以前新增的該分類下的子分類的分類
    @Modifying
    @Transactional
    @Query(value = "update sub_category s"
            + " set s.category = :inputCategory "
            + " where s.category = :inputOldCategory ", nativeQuery = true)
    public int updateSubCategoryCategoryByOldCategory(@Param("inputCategory") String category, @Param("inputOldCategory") String oldCategory);
//  用id 刪除子分類
//    public int deleteById(int id);
    
}

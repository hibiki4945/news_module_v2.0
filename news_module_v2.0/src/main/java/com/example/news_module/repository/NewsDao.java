package com.example.news_module.repository;

import com.example.news_module.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDao extends JpaRepository<News, Integer>{
    
    public boolean existsByContent(String content);

    public int removeByContent(String content);
    
    public List<News> findByCategory(String category);
    
    public List<News> findBySubCategory(String subCategory);

    @Query(value = "select * from news where news_title like concat ('%', :keyword, '%')", nativeQuery = true)
    public List<News> findByNewsTitle(@Param("keyword")String str);

    @Query(value = "select * from news where news_sub_title like concat ('%', :keyword, '%')", nativeQuery = true)
    public List<News> findByNewsSubTitle(@Param("keyword")String str);

    public List<News> findByReleaseTimeGreaterThanEqual(String date);
    
    public List<News> findByReleaseTimeLessThanEqual(String date);
    
}

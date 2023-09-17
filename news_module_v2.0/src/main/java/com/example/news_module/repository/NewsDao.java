package com.example.news_module.repository;

import com.example.news_module.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDao extends JpaRepository<News, Integer>{
    
    public boolean existsByContent(String content);

    public int removeByContent(String content);
    
    public List<News> findByCategory(String category);
    
//    public List<News> findBySubCategory(String subCategory);
//    
//    public List<News> findByNewsTitle(String newsTitle);
//    
//    public List<News> findBySubNewsTitle(String subNewsTitle);
    
}

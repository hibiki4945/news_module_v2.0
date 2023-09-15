package com.example.news_module.repository;

import com.example.news_module.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsDao extends JpaRepository<News, Integer>{
    
    public boolean existsByContent(String content);

    public int removeByContent(String content);
    
}

package com.example.news_module.repository;

import com.example.news_module.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsDao extends JpaRepository<News, Integer>{
    
}

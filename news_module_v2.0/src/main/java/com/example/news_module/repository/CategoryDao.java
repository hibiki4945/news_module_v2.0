package com.example.news_module.repository;

import com.example.news_module.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category, String>{
    
}

package com.example.news_module.repository;

import com.example.news_module.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryDao extends JpaRepository<SubCategory, String>{

    public boolean existsByCategory(String category);
    
    public List<SubCategory> findByCategory(String category);
    
}

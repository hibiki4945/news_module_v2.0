package com.example.news_module.repository;

import com.example.news_module.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryDao extends JpaRepository<SubCategory, String>{
    
}

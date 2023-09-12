package com.example.news_module.repository;

import com.example.news_module.entity.OnlyTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlyTestDao extends JpaRepository<OnlyTest, String>{
    
}

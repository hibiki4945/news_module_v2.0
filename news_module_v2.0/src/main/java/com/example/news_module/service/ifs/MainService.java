package com.example.news_module.service.ifs;

import com.example.news_module.entity.Category;
import com.example.news_module.entity.News;
import com.example.news_module.vo.CategoryAddResponse;
import com.example.news_module.vo.NewsAddResponse;

public interface MainService {

    public NewsAddResponse newsAddCheck(News news);
    
    public NewsAddResponse newsAdd(NewsAddResponse newsAddResponse);
    
    public CategoryAddResponse categoryAdd(Category category);
}

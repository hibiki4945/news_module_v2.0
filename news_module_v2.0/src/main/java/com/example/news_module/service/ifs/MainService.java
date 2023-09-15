package com.example.news_module.service.ifs;

import com.example.news_module.entity.News;
import com.example.news_module.vo.NewsAddResponse;

public interface MainService {

    public NewsAddResponse newsAdd(News news);
    
}

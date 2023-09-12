package com.example.news_module.service.impl;

import com.example.news_module.entity.OnlyTest;
import com.example.news_module.repository.OnlyTestDao;
import com.example.news_module.service.ifs.OnlyTestService;
import org.springframework.beans.factory.annotation.Autowired;

public class OnlyTestServiceImpl implements OnlyTestService{

    @Autowired
    private OnlyTestDao onlyTestDao;
    
    @Override
    public void add() {
        // TODO Auto-generated method stub
        
        onlyTestDao.save(new OnlyTest("A01"));
        
    }

}

package com.example.news_module.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "only_test")
@Entity
public class OnlyTest {

    @Id
    @Column(name = "id_str")
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public OnlyTest() {
        super();
        // TODO Auto-generated constructor stub
    }

    public OnlyTest(String str) {
        super();
        this.str = str;
    }
    
}

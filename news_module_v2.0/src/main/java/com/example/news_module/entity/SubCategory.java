package com.example.news_module.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sub_category")
public class SubCategory {
    
    @Id
    @Column(name = "sub_category")
    private String subCategory;
    
    @Column(name = "sub_category_news_count")
    private int subCategoryNewsCount;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "build_time")
    private String buildTime;

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getSubCategoryNewsCount() {
        return subCategoryNewsCount;
    }

    public void setSubCategoryNewsCount(int subCategoryNewsCount) {
        this.subCategoryNewsCount = subCategoryNewsCount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(String buildTime) {
        this.buildTime = buildTime;
    }

    public SubCategory(String subCategory, int subCategoryNewsCount, String category, String buildTime) {
        super();
        this.subCategory = subCategory;
        this.subCategoryNewsCount = subCategoryNewsCount;
        this.category = category;
        this.buildTime = buildTime;
    }

    public SubCategory() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "SubCategory [subCategory=" + subCategory + ", subCategoryNewsCount=" + subCategoryNewsCount + ", category=" + category + ", buildTime=" + buildTime + "]";
    }
    
}

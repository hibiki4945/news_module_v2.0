package com.example.news_module.constants;

public enum RtnCode {

    SUCCESSFUL("200", "successful!"),
    DATA_EMPTY_ERROR("400", "data empty error!"),
    CATEGORY_EMPTY_ERROR("400", "category empty error!"),
    SUB_CATEGORY_EMPTY_ERROR("400", "sub category empty error!"),
    NEWS_TITLE_EMPTY_ERROR("400", "news title empty error!"),
    NEWS_SUB_TITLE_EMPTY_ERROR("400", "news sub title empty error!"),
    RELEASE_TIME_FORMAT_ERROR("400", "release time format error!"),
    CONTENT_EMPTY_ERROR("400", "content empty error!"),
    NEWS_EXISTS_ERROR("400", "news exists error!"),
    DAO_ERROR("400", "dao error!"),
    DATA_ERROR("400", "data error!"),
    CATEGORY_EXISTS_ERROR("400", "category exists error!"),
    SUB_CATEGORY_EXISTS_ERROR("400", "sub category exists error!"),
    CATEGORY_NOT_EXISTS_ERROR("400", "category not exists error!"),
    NEWS_TITLE_OVER_LENGTH_ERROR("400", "news title over length error!"),
    NEWS_SUB_TITLE_OVER_LENGTH_ERROR("400", "news sub title over length error!"),
    CONTENT_OVER_LENGTH_ERROR("400", "content over length error!"),
    CATEGORY_OVER_LENGTH_ERROR("400", "category over length error!"),
    SUB_CATEGORY_OVER_LENGTH_ERROR("400", "sub category over length error!");
    
    private String code;
    
    private String message;

    private RtnCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}

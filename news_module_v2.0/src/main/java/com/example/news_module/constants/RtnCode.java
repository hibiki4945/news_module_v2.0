package com.example.news_module.constants;

public enum RtnCode {

    SUCCESSFUL("200", "successful!"),
    DATA_ERROR("400", "data error");
    
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

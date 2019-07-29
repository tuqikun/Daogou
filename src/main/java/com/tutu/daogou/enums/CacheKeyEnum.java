package com.tutu.daogou.enums;

public enum CacheKeyEnum {

    GOODSINFO("GOODSINFO_","商品详细信息"),
    TBGOODSINFO("TBGOODSINFO_","商品详细信息");
    private String code;
    private String message;

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

    private CacheKeyEnum(String code, String message){
        this.code=code;
        this.message = message;
    }
}

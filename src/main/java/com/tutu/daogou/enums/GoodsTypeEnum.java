package com.tutu.daogou.enums;

public enum GoodsTypeEnum {

    JDGOODS("10","京东商品"),
    TAOBAOGOODS("20","淘宝商品");

    public String code;
    public String note;

    public void setCode( String code ) {
        this.code = code;
    }

    public void setNote( String note ) {

        this.note = note;
    }

    GoodsTypeEnum(String code, String note ) {
        this.note = note;
        this.code = code;
    }

}

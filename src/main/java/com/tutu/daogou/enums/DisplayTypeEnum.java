package com.tutu.daogou.enums;

public enum  DisplayTypeEnum {

    index("10","发现"),
    find("20","潮流"),
    goodSale("30","好价");

    public String code;
    public String note;

    public void setCode( String code ) {
        this.code = code;
    }

    public void setNote( String note ) {

        this.note = note;
    }

    DisplayTypeEnum(String code, String note ) {
        this.note = note;
        this.code = code;
    }
}

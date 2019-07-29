package com.tutu.daogou.util;

/**
 * name: zhangyan
 * date:$[DATE]
 */
public class MessageCode {

    //生成6位随机数
    public static String getRandom(){
        double a = Math.random();
        int i=(int)(a*100000+100000);
        System.out.println(i);
        String messageCode = String.valueOf(i);
        return messageCode;
    }

}

package com.tutu.daogou.model;

import lombok.Data;

@Data
public class JdCouponModel {

    private String name;

    private Double quota;//限额

    private Double discount;//折扣金额

    private String mUrl;//领券链接

    private String timeDesc;//有效时间描述
}

package com.tutu.daogou.model;

import lombok.Data;

import java.util.List;

@Data
public class GoodsInfoModel {

    private String skuId;//商品id

    private String name;

    private String secondName;

    private Double price;

    private String url;//转链后的url

    private String mainImg;//主图

    private List<String> minImgs;//小图

    private List<JdCouponModel> jdCouponModels;

    private List<String> labels;

    private String shopName;

    private String goodsType;//10 京东 20 淘宝

    private String introduce;//商品介绍

    private int sort;//商品排序

    private boolean collect;//是否收藏
}

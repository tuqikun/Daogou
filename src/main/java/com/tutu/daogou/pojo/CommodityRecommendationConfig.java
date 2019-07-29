package com.tutu.daogou.pojo;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "commodity_recommendation_config")
public class CommodityRecommendationConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;//商品介绍

    private String skuId;

    private String state;

    private String img;

    private Date createTime;

    private String labels;

    private int sort;

    private String goodsType;

    private String pushUrl;

    private String displayType;//10 首页 20 发现

    private String title;

}

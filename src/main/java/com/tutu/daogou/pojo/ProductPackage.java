package com.tutu.daogou.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "cl_product_package")
@Entity
@Data
public class ProductPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String packageIdJd;

    private String packageIdTb;

    private String firstName;//首页主标题

    private String secondName;//首页副标题

    private String firstDetailName;//详情页主标题

    private String secondDetailName;//详情副主标题

    private String state;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone ="GMT+8")
    private Date createTime;

    private String img;

    private String detailImg;//详情页图片

    private String detailIntroduce;//详情页描述

    private String labels;

    private int sort;

    private String displayType;

}

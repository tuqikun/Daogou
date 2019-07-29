package com.tutu.daogou.model;/*
 *Created by tuqikun on 2019/3/28
 */

import com.tutu.daogou.util.Detect;
import lombok.Data;

import java.io.Serializable;

@Data
public class TaoBaoKeProductInfoModel implements Serializable{

    private String numIid;//商品ID
    private String title;//商品标题
    private String pictUrl;//商品主图
    private String smallImages;//商品小图
    private String reservePrice;//商品一口价格
    private String zkFinalPrice;//商品折扣价格
    private String clickUrl;//商品淘客链接
    private String nick;//店家名称
    private Long   productTaokeId;
    private String itemUrl;//商品链接

    public String getClickUrl(){
        if(!Detect.notEmpty(this.clickUrl)){
            this.clickUrl=this.itemUrl;
        }
        return this.clickUrl;
    }
}

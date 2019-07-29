package com.tutu.daogou.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserBrowseLogModel extends GoodsInfoModel {
    private Date createTime;
}

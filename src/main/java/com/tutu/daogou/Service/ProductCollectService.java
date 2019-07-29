package com.tutu.daogou.Service;


import com.tutu.daogou.model.UserBrowseLogModel;

import java.util.List;

public interface ProductCollectService {

    boolean collect(String userId, String goodsType, String skuId);

    void addCollection(String userId, String goodsType, String skuId);

    void deleteCollection(String userId, String goodsType, String skuId);

    List<UserBrowseLogModel> getCollectList(String userId, int current, int pageSize);
}

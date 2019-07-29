package com.tutu.daogou.Service;


import com.tutu.daogou.model.GoodsInfoModel;

import java.util.List;

public interface JdProductService {
    List<GoodsInfoModel> getGoodsBykeyword(String keywords, int order, int orderField, int pageSize, int pageNum, String userId);

    List<GoodsInfoModel> getGoodsByPackageId(String packageIdJd, String packageIdTb, int pageSize, int pageNum, String label, String userId);

    GoodsInfoModel getGoodsBySkuId(String skuId);

    List<GoodsInfoModel> getGoodsSimilar(String skuId, String goodsType, String userId);

    void evitCache(String type);
}

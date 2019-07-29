package com.tutu.daogou.Service;

import com.tutu.daogou.model.GoodsInfoModel;
import com.tutu.daogou.model.JdCouponModel;

import java.util.List;

public interface JdAuthService {

    String getPushUrl(String skuId);

    void getGoodsInfo(String skuId, GoodsInfoModel goodsInfoModel);

    List<Object> getGoodsSkuIdBypackageId(String packageId, int pageNo, int pageSize);

    List<JdCouponModel> getGoodsCoupons(String skuId);

    List<GoodsInfoModel> getGoodsBykeyWord(String keywords, int order, int orderField, int pageSize, int pageNum);

    List<Object> getSimilarGoods(String skuId);
}

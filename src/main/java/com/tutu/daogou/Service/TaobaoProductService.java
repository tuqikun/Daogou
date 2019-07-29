package com.tutu.daogou.Service;


import com.tutu.daogou.model.GoodsInfoModel;

import java.util.List;

public interface TaobaoProductService {

    GoodsInfoModel getTBproduct(String numIid);

    void getTaobaoPackageGoods(int pageSize, int pageNo, String packageId, String label, List<GoodsInfoModel> rtnList, String userId);

    void getSimilarGoods(String skuId, List<GoodsInfoModel> rtnList, String userId);
}

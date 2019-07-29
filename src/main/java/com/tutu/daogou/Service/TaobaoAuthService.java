package com.tutu.daogou.Service;


import com.tutu.daogou.model.TaoBaoKeProductInfoModel;

import java.util.List;

public interface TaobaoAuthService {
    TaoBaoKeProductInfoModel setTaoBaoKeModel(String numIid);

    List<TaoBaoKeProductInfoModel> getTaoBaoPackageGoods(int pageSize, int pageNo, String packageId);

    List<TaoBaoKeProductInfoModel> selectSimilar(Long numIid);
}

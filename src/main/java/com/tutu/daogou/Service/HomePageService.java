package com.tutu.daogou.Service;


import com.tutu.daogou.model.GoodsInfoModel;
import com.tutu.daogou.model.SearchModel;

import java.util.List;

public interface HomePageService {

    List<GoodsInfoModel> getHomePageGoods(String displayType, String userId);

    SearchModel getSearchModel();
}

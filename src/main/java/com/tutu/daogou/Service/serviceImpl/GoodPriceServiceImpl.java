package com.tutu.daogou.Service.serviceImpl;

import com.tutu.daogou.Service.GoodPriceService;
import com.tutu.daogou.Service.HomePageService;
import com.tutu.daogou.enums.DisplayTypeEnum;
import com.tutu.daogou.model.SearchModel;
import com.tutu.daogou.pojo.ProductPackage;
import com.tutu.daogou.repository.ArcSysConfigRepository;
import com.tutu.daogou.repository.ProductPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("goodPriceService")
public class GoodPriceServiceImpl implements GoodPriceService {


    @Autowired
    private ProductPackageRepository productPackageRepository;
    @Autowired
    private ArcSysConfigRepository arcSysConfigRepository;
    @Autowired
    private HomePageService homePageService;

    public Map<String,Object> getGoodPricePage(){
        Map<String,Object> data=new HashMap<>();
        List<ProductPackage> productPackageList=getPckage();
        SearchModel searchModel=homePageService.getSearchModel();
        data.put("productPackageList",productPackageList);
        data.put("searchModel",searchModel);
        return data;
    }


    private List<ProductPackage> getPckage(){
        List<ProductPackage> packageList = productPackageRepository.findByStateAndDisplayTypeOrderBySort("10", DisplayTypeEnum.goodSale.code);
        return packageList;
    }



}

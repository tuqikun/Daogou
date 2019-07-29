package com.tutu.daogou.Service.serviceImpl;

import com.tutu.daogou.Service.HomePageService;
import com.tutu.daogou.Service.JdProductService;
import com.tutu.daogou.Service.ProductCollectService;
import com.tutu.daogou.Service.TaobaoProductService;
import com.tutu.daogou.enums.GoodsTypeEnum;
import com.tutu.daogou.model.GoodsInfoModel;
import com.tutu.daogou.model.SearchModel;
import com.tutu.daogou.pojo.ArcSysConfig;
import com.tutu.daogou.pojo.CommodityRecommendationConfig;
import com.tutu.daogou.repository.ArcSysConfigRepository;
import com.tutu.daogou.repository.CommodityRecommendationConfigRepository;
import com.tutu.daogou.util.Detect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

@Service("homePageService")
@Slf4j
public class HomePageServiceImpl implements HomePageService {

    @Autowired
    private JdProductService jdProductService;
    @Autowired
    private CommodityRecommendationConfigRepository commodityRecommendationConfigRepository;
    @Autowired
    private ArcSysConfigRepository arcSysConfigRepository;
    @Autowired
    private ThreadPoolTaskExecutor asyncPromiseExecutor;
    @Autowired
    private TaobaoProductService taobaoProductService;
    @Autowired
    private ProductCollectService productCollectService;

    public List<GoodsInfoModel> getHomePageGoods(String displaytype, String userId){
        List<GoodsInfoModel> goodsInfoModels = new CopyOnWriteArrayList<>();
        List<CommodityRecommendationConfig> list = commodityRecommendationConfigRepository.findAllByStateAndDisplayTypeOrderBySort("10", displaytype);
        List<Future> futureList = new ArrayList<>();
        int index=0;
        int dealSize=3;
        Future future = null;
        for(int i=0;i<(list.size()/dealSize)+1;i++,index+=dealSize) {
            int startIndex = index;
            if (startIndex >= list.size()) break;
            int endIndex = startIndex + dealSize;
            endIndex = endIndex > list.size() ? list.size() : endIndex;
            List<CommodityRecommendationConfig> newList = list.subList(startIndex, endIndex);
            future = asyncPromiseExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    for (CommodityRecommendationConfig commodityRecommendationConfig : newList) {
                        if(commodityRecommendationConfig.getGoodsType().equals(GoodsTypeEnum.JDGOODS.code)){
                            log.info("首页获取京东产品 id:"+commodityRecommendationConfig.getSkuId());
                            GoodsInfoModel goodsInfoModel = jdProductService.getGoodsBySkuId(commodityRecommendationConfig.getSkuId());
                            if (goodsInfoModel != null) {
                                goodsInfoModel.setGoodsType(commodityRecommendationConfig.getGoodsType());
                                goodsInfoModel.setMainImg(commodityRecommendationConfig.getImg());
                                goodsInfoModel.setIntroduce(commodityRecommendationConfig.getName());
                                goodsInfoModel.setSort(commodityRecommendationConfig.getSort());
                                if (commodityRecommendationConfig.getLabels() != null) {
                                    goodsInfoModel.setLabels(Arrays.asList(commodityRecommendationConfig.getLabels().split(",")));
                                }
                                if (Detect.notEmpty(commodityRecommendationConfig.getTitle())&&!"null".equals(commodityRecommendationConfig.getTitle())) {
                                    goodsInfoModel.setName(commodityRecommendationConfig.getTitle());
                                }
                                goodsInfoModel.setGoodsType(GoodsTypeEnum.JDGOODS.code);
                                goodsInfoModel.setCollect(productCollectService.collect(userId,GoodsTypeEnum.JDGOODS.code,commodityRecommendationConfig.getSkuId()));
                                goodsInfoModels.add(goodsInfoModel);
                            }else {
                                commodityRecommendationConfig.setState("20");
                                commodityRecommendationConfigRepository.save(commodityRecommendationConfig);
                            }
                         }else if(commodityRecommendationConfig.getGoodsType().equals(GoodsTypeEnum.TAOBAOGOODS.code)){
                            log.info("首页获取淘宝产品 id:"+commodityRecommendationConfig.getSkuId());
                            GoodsInfoModel goodsInfoModel = taobaoProductService.getTBproduct(commodityRecommendationConfig.getSkuId());
                            if (goodsInfoModel != null) {
                                goodsInfoModel.setGoodsType(commodityRecommendationConfig.getGoodsType());
                                goodsInfoModel.setMainImg(commodityRecommendationConfig.getImg());
                                goodsInfoModel.setIntroduce(commodityRecommendationConfig.getName());
                                goodsInfoModel.setSort(commodityRecommendationConfig.getSort());
                                if (commodityRecommendationConfig.getLabels() != null) {
                                    goodsInfoModel.setLabels(Arrays.asList(commodityRecommendationConfig.getLabels().split(",")));
                                }
                                if (Detect.notEmpty(commodityRecommendationConfig.getTitle())&&!"null".equals(commodityRecommendationConfig.getTitle())) {
                                    goodsInfoModel.setName(commodityRecommendationConfig.getTitle());
                                }
                                goodsInfoModel.setGoodsType(GoodsTypeEnum.TAOBAOGOODS.code);
                                goodsInfoModel.setCollect(productCollectService.collect(userId,GoodsTypeEnum.TAOBAOGOODS.code,commodityRecommendationConfig.getSkuId()));
                                goodsInfoModels.add(goodsInfoModel);
                            }else {
                                commodityRecommendationConfig.setState("20");
                                commodityRecommendationConfigRepository.save(commodityRecommendationConfig);
                            }
                        }
                    }
                }
            });
            futureList.add(future);
        }
        Iterator<Future> iter = futureList.iterator();//为了等待上面线程完成返回结果
        while(iter.hasNext()){
            future = iter.next();
            try {
                future.get();
            } catch (Exception e) {
                log.error("", e);
            }
        }
        if(Detect.notEmpty(goodsInfoModels)){
            goodsInfoModels.sort((s1,s2)->s1.getSort()-s2.getSort());
        }
        return goodsInfoModels;
    }

    public SearchModel getSearchModel(){
        SearchModel searchModel=new SearchModel();
        ArcSysConfig searchKey=  arcSysConfigRepository.findByCode("search_key");
        if(searchKey!=null&& searchKey.getValue()!=null){
            searchModel.setSearcKey(Arrays.asList(searchKey.getValue().split(",")));
            }
        ArcSysConfig inputSearchKey=  arcSysConfigRepository.findByCode("input_search_key");
        if(inputSearchKey!=null){
            searchModel.setInputSearchKey(inputSearchKey.getValue());
        }
        return searchModel;
    }
}

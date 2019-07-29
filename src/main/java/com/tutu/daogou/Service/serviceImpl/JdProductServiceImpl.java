package com.tutu.daogou.Service.serviceImpl;

import com.tutu.daogou.Service.JdAuthService;
import com.tutu.daogou.Service.JdProductService;
import com.tutu.daogou.Service.ProductCollectService;
import com.tutu.daogou.Service.TaobaoProductService;
import com.tutu.daogou.enums.CacheKeyEnum;
import com.tutu.daogou.enums.GoodsTypeEnum;
import com.tutu.daogou.model.GoodsInfoModel;
import com.tutu.daogou.model.JdCouponModel;
import com.tutu.daogou.repository.CommodityRecommendationConfigRepository;
import com.tutu.daogou.util.Detect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import tool.util.StringUtil;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;

@Service("jdProductService")
@Slf4j
public class JdProductServiceImpl implements JdProductService {
    @Autowired
    private JdAuthService jDAuthService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private ThreadPoolTaskExecutor asyncPromiseExecutor;
    @Autowired
    private CommodityRecommendationConfigRepository commodityRecommendationConfigRepository;
    @Autowired
    private TaobaoProductService taobaoProductService;
    @Autowired
    private ProductCollectService productCollectService;


    /**
     * 商品搜索
     *
     * @param keywords
     * @param order
     * @param orderField
     * @param pageSize
     * @param pageNum
     * @return
     */
    public List<GoodsInfoModel> getGoodsBykeyword(String keywords, int order, int orderField, int pageSize, int pageNum, String userId) {
        List<GoodsInfoModel> goodsInfoModelList = jDAuthService.getGoodsBykeyWord(keywords, order, orderField, pageSize, pageNum);
        List<GoodsInfoModel> rtnList = new CopyOnWriteArrayList<>();
        List<Future> futureList = new ArrayList<>();
        log.info("关键字搜索京东产品");
        int index=0;
        int dealSize=2;
        Future future = null;
        for(int i=0;i<10;i++,index+=dealSize){
            int startIndex = index;
            if(startIndex>=goodsInfoModelList.size()) break;
            int endIndex = startIndex + dealSize;
            endIndex = endIndex>goodsInfoModelList.size() ? goodsInfoModelList.size() : endIndex;
            List<GoodsInfoModel> newGoodsInfoModelList=goodsInfoModelList.subList(startIndex,endIndex);
            future = asyncPromiseExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    if (Detect.notEmpty(newGoodsInfoModelList)) {
                        for (int i = 0; i < newGoodsInfoModelList.size(); i++) {
                            GoodsInfoModel goodsInfoModel = newGoodsInfoModelList.get(i);
                            GoodsInfoModel cacheModel = cacheManager.getCache("JDGOODS").get(CacheKeyEnum.GOODSINFO.getCode() + goodsInfoModel.getSkuId(), GoodsInfoModel.class);
                            if (cacheModel != null) {//若缓存中有直接从缓存中获取
                                BeanUtils.copyProperties(cacheModel, goodsInfoModel);
                                rtnList.add(goodsInfoModel);
                            } else {
                                jDAuthService.getGoodsInfo(goodsInfoModel.getSkuId(), goodsInfoModel);
//                                String url = jDAuthService.getPushUrl(goodsInfoModel.getSkuId());
//                                log.info("获取推广链接是：" + url);
//                                if (Detect.notEmpty(url)) {
//                                    goodsInfoModel.setUrl(url);
                                    goodsInfoModel.setGoodsType(GoodsTypeEnum.JDGOODS.code);
                                    goodsInfoModel.setCollect(productCollectService.collect(userId, GoodsTypeEnum.JDGOODS.code,goodsInfoModel.getSkuId()));
                                    rtnList.add(goodsInfoModel);
//                                }
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
        if(orderField==4){
            if(order==0){
                rtnList.sort(Comparator.comparing(GoodsInfoModel::getPrice).reversed());
            }else if(order==1){
                rtnList.sort((s1,s2)->s1.getPrice().compareTo(s2.getPrice()));
            }
        }
        return rtnList;
    }


    public List<GoodsInfoModel> getGoodsByPackageId(String packageIdJd,String packageIdTb, int pageSize, int pageNum, String label,String userId) {
        List<GoodsInfoModel> rtnList = new CopyOnWriteArrayList<>();
        Future future=null;
        if(Detect.notEmpty(packageIdTb)){
            future= asyncPromiseExecutor.submit(new Runnable() {
                @Override
                public void run() {

                    taobaoProductService.getTaobaoPackageGoods(pageSize/2,pageNum,packageIdTb,label,rtnList,userId);
                }
            });
        }

        getJdGoodsBySkuIds(packageIdJd, pageSize/2, pageNum, label, rtnList,userId);
        try {
            if(future!=null){
                future.get();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return rtnList;
    }

    private void getJdGoodsBySkuIds(String packageIdJd, int pageSize, int pageNum, String label,List<GoodsInfoModel> rtnList,String userId) {
        log.info("调用京东商品包");
            if(Detect.notEmpty(packageIdJd)){
                List<Object> goodsSkuId = jDAuthService.getGoodsSkuIdBypackageId(packageIdJd, pageNum, pageSize);
                getJdGoodsInfoBySkuIds(goodsSkuId,rtnList,label,userId);
            }
    }

    /**
     * 查找相似商品
     * @param skuId
     * @return
     */
    public List<GoodsInfoModel> getGoodsSimilar(String skuId,String goodsType,String userId){
        List<GoodsInfoModel> rtnList = new CopyOnWriteArrayList<>();
        if(GoodsTypeEnum.JDGOODS.code.equals(goodsType)){
            List<Object> goodsSkuId=jDAuthService.getSimilarGoods(skuId);
            getJdGoodsInfoBySkuIds(goodsSkuId,rtnList,null,userId);
        }else if(GoodsTypeEnum.TAOBAOGOODS.code.equals(goodsType)){
            taobaoProductService.getSimilarGoods(skuId,rtnList,userId);
        }
        return rtnList;
    }

    private void getJdGoodsInfoBySkuIds(List<Object> goodsSkuId,List<GoodsInfoModel> rtnList,String label,String userId){
        List<Future> futureList = new ArrayList<>();
        int index=0;
        int dealSize=2;
        Future future = null;
        if(Detect.notEmpty(goodsSkuId)){
            for(int i=0;i<10;i++,index+=dealSize){
                int startIndex = index;
                if(startIndex>=goodsSkuId.size()) break;
                int endIndex = startIndex + dealSize;
                endIndex = endIndex>goodsSkuId.size() ? goodsSkuId.size() : endIndex;
                List<Object> newGoodsInfoModelList=goodsSkuId.subList(startIndex,endIndex);
                future = asyncPromiseExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (Object skuId : newGoodsInfoModelList) {
                            GoodsInfoModel goodsInfoModel = getGoodsBySkuId(skuId.toString());
                            if (goodsInfoModel != null) {
                                if (label != null) {
                                    goodsInfoModel.setLabels(Arrays.asList(label.split(",")));
                                }
                                goodsInfoModel.setGoodsType(GoodsTypeEnum.JDGOODS.code);
                                goodsInfoModel.setCollect(productCollectService.collect(userId,GoodsTypeEnum.JDGOODS.code,goodsInfoModel.getSkuId()));
                                rtnList.add(goodsInfoModel);
                            }
                        }
                    }
                });
                futureList.add(future);
            }
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
    }

    public GoodsInfoModel getGoodsBySkuId(String skuId) {
        GoodsInfoModel goodsInfoModel = cacheManager.getCache("JDGOODS").get(CacheKeyEnum.GOODSINFO.getCode() + skuId, GoodsInfoModel.class);
        if (goodsInfoModel == null) {
            String url = jDAuthService.getPushUrl(skuId.toString());
            if (Detect.notEmpty(url)) {
                log.info("获取到推广链接skuId:" + skuId);
                goodsInfoModel = new GoodsInfoModel();
                goodsInfoModel.setUrl(url);
                jDAuthService.getGoodsInfo(skuId.toString(), goodsInfoModel);
                if (StringUtil.isBlank(goodsInfoModel.getName()) || StringUtil.isBlank(goodsInfoModel.getPrice()) || goodsInfoModel.getPrice() <= 0) {
                    return null;
                }
                List<JdCouponModel> couponModels = jDAuthService.getGoodsCoupons(skuId.toString());
                goodsInfoModel.setJdCouponModels(couponModels);
                cacheManager.getCache("JDGOODS").put(CacheKeyEnum.GOODSINFO.getCode() + skuId, goodsInfoModel);
            }
        }
        return goodsInfoModel;
    }

    public void evitCache(String type){
        cacheManager.getCache(type).clear();
        if(type.equals("JDGOODS")){
            cacheManager.getCache("TBGOODS").clear();
        }
}
}
package com.tutu.daogou.Service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tutu.daogou.Service.ProductCollectService;
import com.tutu.daogou.Service.TaobaoAuthService;
import com.tutu.daogou.Service.TaobaoProductService;
import com.tutu.daogou.enums.CacheKeyEnum;
import com.tutu.daogou.enums.GoodsTypeEnum;
import com.tutu.daogou.model.GoodsInfoModel;
import com.tutu.daogou.model.TaoBaoKeProductInfoModel;
import com.tutu.daogou.util.Detect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import tool.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("taobaoProductService")
@Slf4j
public class TaobaoProductServiceImpl implements TaobaoProductService {

    @Autowired
    private TaobaoAuthService taobaoAuthService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private ProductCollectService productCollectService;


    public GoodsInfoModel getTBproduct(String numIid){
        GoodsInfoModel goodsInfoModel=cacheManager.getCache("TBGOODS").get(CacheKeyEnum.TBGOODSINFO.getCode()+numIid,GoodsInfoModel.class);
        if(goodsInfoModel==null){
            TaoBaoKeProductInfoModel taoBaoKeProductInfoModel=taobaoAuthService.setTaoBaoKeModel(numIid);
            if(StringUtil.isNotBlank(taoBaoKeProductInfoModel)){
                goodsInfoModel=new GoodsInfoModel();
                transform(taoBaoKeProductInfoModel,goodsInfoModel);
                cacheManager.getCache("TBGOODS").put(CacheKeyEnum.TBGOODSINFO.getCode()+numIid,goodsInfoModel);
            }
        }
        return goodsInfoModel;
    }

    public void getTaobaoPackageGoods(int pageSize, int pageNo, String packageId,String label,List<GoodsInfoModel> rtnList,String userId){
        log.info("调用淘宝包");
        List<TaoBaoKeProductInfoModel> productInfoModels=taobaoAuthService.getTaoBaoPackageGoods(pageSize,pageNo,packageId);
        setTaobaoToGoods(productInfoModels,rtnList,label,userId);
    }

    private void setTaobaoToGoods(List<TaoBaoKeProductInfoModel> productInfoModels,List<GoodsInfoModel> rtnList,String label,String userId){
        if(Detect.notEmpty(productInfoModels)){
            for(TaoBaoKeProductInfoModel taoBaoKeProductInfoModel:productInfoModels){
                GoodsInfoModel goodsInfoModel=new GoodsInfoModel();
                if(Detect.notEmpty(taoBaoKeProductInfoModel.getClickUrl())) {
                    transform(taoBaoKeProductInfoModel, goodsInfoModel);
                    if (label != null) {
                        goodsInfoModel.setLabels(Arrays.asList(label.split(",")));
                    }
                    goodsInfoModel.setUrl(taoBaoKeProductInfoModel.getClickUrl());
                    goodsInfoModel.setCollect(productCollectService.collect(userId, GoodsTypeEnum.TAOBAOGOODS.code,goodsInfoModel.getSkuId()));
                    rtnList.add(goodsInfoModel);
                }
            }
        }
    }

    /**
     * 获取淘宝类似商品
     * @param skuId
     * @param rtnList
     */
    public void getSimilarGoods(String skuId,List<GoodsInfoModel> rtnList,String userId){
        List<TaoBaoKeProductInfoModel> productInfoModels=taobaoAuthService.selectSimilar(Long.valueOf(skuId));
        setTaobaoToGoods(productInfoModels,rtnList,null,userId);
    }

    /**
     * @param taoBaoKeProductInfoModel
     * @param goodsInfoModel
     * 属性转换
     */
    private void transform(TaoBaoKeProductInfoModel taoBaoKeProductInfoModel,GoodsInfoModel goodsInfoModel){
        goodsInfoModel.setName(taoBaoKeProductInfoModel.getTitle());
        goodsInfoModel.setMainImg(taoBaoKeProductInfoModel.getPictUrl());
        JSONObject jsonObject=JSON.parseObject(taoBaoKeProductInfoModel.getSmallImages());
        if(jsonObject!=null){
            goodsInfoModel.setMinImgs(JSON.parseArray(jsonObject.getString("string"),String.class));
        }else {
            List<String> minImgs=new ArrayList<>();
            minImgs.add(taoBaoKeProductInfoModel.getPictUrl());
            goodsInfoModel.setMinImgs(minImgs);
        }
        goodsInfoModel.setShopName(taoBaoKeProductInfoModel.getNick());
        goodsInfoModel.setPrice(Double.valueOf(taoBaoKeProductInfoModel.getZkFinalPrice()));
        goodsInfoModel.setGoodsType(GoodsTypeEnum.TAOBAOGOODS.code);
        goodsInfoModel.setSkuId(taoBaoKeProductInfoModel.getNumIid());
    }
}

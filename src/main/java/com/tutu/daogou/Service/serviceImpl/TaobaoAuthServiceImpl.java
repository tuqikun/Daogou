package com.tutu.daogou.Service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.request.TbkItemRecommendGetRequest;
import com.taobao.api.request.TbkUatmFavoritesItemGetRequest;
import com.taobao.api.response.TbkItemInfoGetResponse;
import com.taobao.api.response.TbkItemRecommendGetResponse;
import com.taobao.api.response.TbkUatmFavoritesItemGetResponse;
import com.tutu.daogou.Service.TaobaoAuthService;
import com.tutu.daogou.model.TaoBaoKeProductInfoModel;
import com.tutu.daogou.util.Detect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("taobaoAuthService")
@Slf4j
public class TaobaoAuthServiceImpl implements TaobaoAuthService {

    private static final String taobaoAppkey="27570866";
        private static final String taobaoAppsercet="7f62968bf088332470e0e13f0826aefb";


    /**
     * 获取商品详情
     * 淘宝客接口名称taobao.tbk.item.info.get
     * @param numIid
     * @return
     */
    public TaoBaoKeProductInfoModel setTaoBaoKeModel(String numIid){
        try{
            TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", taobaoAppkey, taobaoAppsercet);
            TbkItemInfoGetRequest req = new TbkItemInfoGetRequest();
            req.setNumIids(numIid);
            TbkItemInfoGetResponse rsp = client.execute(req);
            log.info("调用淘宝商品详情："+rsp.getBody());
            if(Detect.notEmpty(rsp.getBody())&&
                    JSONPath.read(rsp.getBody(),"$.tbk_item_info_get_response.results.n_tbk_item")!=null
                    &&Detect.notEmpty(JSONPath.read(rsp.getBody(),"$.tbk_item_info_get_response.results.n_tbk_item").toString())){
                List<TaoBaoKeProductInfoModel> productInfos= JSON.parseArray(JSONPath.read(rsp.getBody(),"$.tbk_item_info_get_response.results.n_tbk_item").toString(),TaoBaoKeProductInfoModel.class);
                TaoBaoKeProductInfoModel taoBaoKeProductInfoModel=productInfos.get(0);
                return taoBaoKeProductInfoModel;
            }
        }catch (Exception e){
            log.error("请求支付宝商品详情出错",e);
            return null;
        }
        return null;
    }


    /**
     * 查询相似商品
     * @param numIid
     * @return
     * 接口taobao.tbk.item.recommend.get
     */
    public  List<TaoBaoKeProductInfoModel> selectSimilar(Long numIid){
        try{
            TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", taobaoAppkey, taobaoAppsercet);
            TbkItemRecommendGetRequest req = new TbkItemRecommendGetRequest();
            req.setFields("num_iid,title,pict_url,small_images,reserve_price,zk_final_price,item_url,nick");
            req.setNumIid(numIid);
            req.setCount(20L);
            req.setPlatform(1L);
            TbkItemRecommendGetResponse rsp = client.execute(req);
            log.info("调用淘宝商品详情："+rsp.getBody());
            if(Detect.notEmpty(rsp.getBody())&&
                    JSONPath.read(rsp.getBody(),"$.tbk_item_recommend_get_response.results.n_tbk_item")!=null
                    &&Detect.notEmpty(JSONPath.read(rsp.getBody(),"$.tbk_item_recommend_get_response.results.n_tbk_item").toString())){
                List<TaoBaoKeProductInfoModel> productInfos= JSON.parseArray(JSONPath.read(rsp.getBody(),"$.tbk_item_recommend_get_response.results.n_tbk_item").toString(),TaoBaoKeProductInfoModel.class);
                return productInfos;
            }
        }catch (Exception e){
            log.error("请求支付宝商品详情出错",e);
            return null;
        }
        return null;
    }
    /**
     * @param pageSize
     * @param pageNo
     * @param packageId
     *获取商品包产品
     * 淘宝客接口名称taobao.tbk.uatm.favorites.item.get
     * @return
     */
    public  List<TaoBaoKeProductInfoModel> getTaoBaoPackageGoods(int pageSize,int pageNo,String packageId){
        List<TaoBaoKeProductInfoModel> productInfos=new ArrayList<>();
        try{
            TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", taobaoAppkey, taobaoAppsercet);
            TbkUatmFavoritesItemGetRequest req = new TbkUatmFavoritesItemGetRequest();
            req.setPageSize((long)pageSize);
            req.setAdzoneId(102412800085L);
            req.setFavoritesId(Long.valueOf(packageId));
            req.setPageNo((long)pageNo);
            req.setFields("num_iid,title,pict_url,small_images,reserve_price,zk_final_price,click_url,nick");
            TbkUatmFavoritesItemGetResponse response = client.execute(req);
            if(Detect.notEmpty(response.getBody())&&
                    JSONPath.read(response.getBody(),"$.tbk_uatm_favorites_item_get_response.results.uatm_tbk_item")!=null
                    &&Detect.notEmpty(JSONPath.read(response.getBody(),"$.tbk_uatm_favorites_item_get_response.results.uatm_tbk_item").toString())){
                productInfos= JSON.parseArray(JSONPath.read(response.getBody(),"$.tbk_uatm_favorites_item_get_response.results.uatm_tbk_item").toString(),TaoBaoKeProductInfoModel.class);
            }
        }catch (Exception e){
            log.error("请求淘宝商品库出错",e);
            return null;
        }
        return productInfos;
    }
    public static void main(String[] args){
        TaobaoAuthService taobaoAuthService=new TaobaoAuthServiceImpl();
        System.out.println(taobaoAuthService.selectSimilar(555813492238l).toString());
    }
}

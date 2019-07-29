package com.tutu.daogou.Service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.tutu.daogou.Service.JdAuthService;
import com.tutu.daogou.model.GoodsInfoModel;
import com.tutu.daogou.model.JdCouponModel;
import com.tutu.daogou.pojo.JDToken;
import com.tutu.daogou.repository.JDTokenRepository;
import com.tutu.daogou.util.DateUtil;
import com.tutu.daogou.util.Detect;
import com.tutu.daogou.util.JDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tool.util.HttpsUtil;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("jDAuthService")
@Slf4j
public class JdAuthServiceImpl implements JdAuthService {

    private final String appkey="e81a8e5d818690a75e3babcff11f72d9";
    private final String appSecret="fffb37c81b8a48a5b343170fa4d0a131";
    private final String imgUrl="http://img12.360buyimg.com/n0/";
    private final String mainImgUrl="http://img12.360buyimg.com/n1/";
    @Autowired
    private JDTokenRepository jdTokenRepository;


    /**
     * 刷新accessToken，若refreshToken(有效期一年)失效需到京东开普勒重新获取
     * 网址是https://kepler.jd.com/console/docCenterCatalog/docContent?channelId=17
     * @param refreshToken
     */
    private String refreshAccessToken(String refreshToken){
        String str=   HttpsUtil.getClient("https://kploauth.jd.com/oauth/token?grant_type=oauth_refresh_token&app_key="+appkey+"&app_secret="+appSecret+"&refresh_token="+refreshToken);
        JSONObject json=JSON.parseObject(str);
        String result="0";
        if(Detect.notEmpty(json)){
            if((int)json.get("code")==0){
                JDToken jdToken=new JDToken();
                jdToken.setAccessToken(json.get("access_token").toString());
                jdToken.setRefershToken(json.get("refresh_token").toString());
                jdToken.setCreateTime(DateUtil.getNow());
                jdToken.setState("10");
                jdTokenRepository.save(jdToken);
                result =json.get("access_token").toString();
            }else {
                result="-1";
            }
        }
        return result;
    }


    /**
     * @param accessToken
     * @param appKey
     * @param method
     * @param verison
     * @param paramJson
     * 拼接请求参数
     * @return
     * @throws Exception
     */
    private Map<String,String> getParams(String accessToken,String appKey,String method,String verison,Map<String,Object> paramJson) throws Exception {
        Map<String,String> params=new HashMap<>();
        String timestamp=DateUtil.dateStr4(DateUtil.getNow());
        params.put("timestamp", URLEncoder.encode(timestamp,"utf-8"));
        params.put("access_token",URLEncoder.encode(accessToken,"utf-8"));
        params.put("app_key",URLEncoder.encode(appKey,"utf-8"));
        params.put("method",URLEncoder.encode(method,"utf-8"));
        params.put("v",verison);
        params.put("param_json",JSON.toJSONString(paramJson));
        params.put("sign", JDUtil.buildSign(timestamp,verison,"md5","json",method,JSON.toJSONString(paramJson),accessToken,appKey,appSecret));
        params.put("sign_method","md5");
        params.put("format","json");
        return params;
    }

    /**
     * 发送请求
     * @param paramJson
     * @param method
     * @param verison
     * @return
     * @throws Exception
     */
    public String send(Map<String,Object> paramJson,String method,String verison) throws Exception {
        String string=null;
//        JDToken jdToken=jdTokenRepository.findByState("10");
//        if(jdToken!=null){
//            Map<String,String> params=getParams(jdToken.getAccessToken(),appkey,method,verison,paramJson);
        Map<String,String> params=getParams("3d7cf83b20f64d5f9ee6af06a3dc38e7y4m2",appkey,method,verison,paramJson);
            string=HttpsUtil.postClient("https://router.jd.com/api",params);
            JSONObject jsonObject=JSON.parseObject(string);
//            if(Detect.notEmpty(jsonObject)
//                    && JSONPath.read(jsonObject.toJSONString(),"$.error_response.code")!= null
//                    && JSONPath.read(jsonObject.toJSONString(),"$.error_response.code").equals("19")){
//                String refreshResult=refreshAccessToken(jdToken.getRefershToken());
//                jdToken.setState("20");
//                jdTokenRepository.saveAndFlush(jdToken);
//                if(refreshResult.equals("0")){
//                    string=refreshResult;
//                }else if (refreshResult.equals("-1")){
//                    string=refreshResult;
//                }else {
//                    return send(paramJson,method,verison);
//                }
//            }
//        }
        return string;
    }


    /**
     * 获取推广链接
     * @param skuId
     * @return
     */
    public String getPushUrl(String skuId){
        String url=null;
        String response=null;
        Map<String,Object> params=new HashMap<>();
        Map<String,Object> paramJson=new HashMap<>();
        params.put("unionId","1001361654");//联盟id
        params.put("webId","1733740178");//appid
        params.put("skuList",skuId);
        params.put("appKey",appkey);
        paramJson.put("request",params);
        try {
            response = send(paramJson,"jd.kpl.open.cps.convert.keplerurl","1.0");
            if(Detect.notEmpty(response)&&!response.equals("0")&&!response.equals("-1")){
                JSONObject json = JSON.parseObject(response);
                if(JSONPath.read(json.toString(),"$.jd_kpl_open_cps_convert_keplerurl_response.code")!=null
                        &&JSONPath.read(json.toString(),"$.jd_kpl_open_cps_convert_keplerurl_response.code").equals("0")){
                    if(JSONPath.read(json.toString(),"$.jd_kpl_open_cps_convert_keplerurl_response.data")!=null){
                        List<String> urls=new ArrayList<>();
                        if(JSONPath.read(json.toString(),"$.jd_kpl_open_cps_convert_keplerurl_response.data.keplerUrls[0]")!=null){
                            url=JSONPath.read(json.toString(),"$.jd_kpl_open_cps_convert_keplerurl_response.data.keplerUrls[0]").toString();
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("调用获取推广链接接口出错",e);
        }
        return url;

    }


    /**
     * 获取商品信息
     * @param skuId
     * @return
     */
    public void getGoodsInfo(String skuId,GoodsInfoModel goodsInfoModel){
        String url=null;
        String response=null;
        Map<String,Object> paramJson=new HashMap<>();
        paramJson.put("sku",skuId);
        goodsInfoModel.setSkuId(skuId);
        try {
            response = send(paramJson,"public.product.base.query","1.0");
            if(Detect.notEmpty(response)&&!response.equals("0")&&!response.equals("-1")){
                JSONObject json = JSON.parseObject(response);
                if(JSONPath.read(json.toString(),"$.public_product_base_query_response.code")!=null
                        &&JSONPath.read(json.toString(),"$.public_product_base_query_response.code").equals("0")){
                    if(JSONPath.read(json.toString(),"$.public_product_base_query_response.result")!=null){
                        if(JSONPath.read(json.toString(),"$.public_product_base_query_response.result.name")!=null){
                            goodsInfoModel.setName(JSONPath.read(json.toString(),"$.public_product_base_query_response.result.name").toString());
                        }
                        if(JSONPath.read(json.toString(),"$.public_product_base_query_response.result.price")!=null){
                            goodsInfoModel.setPrice(Double.valueOf(JSONPath.read(json.toString(),"$.public_product_base_query_response.result.price").toString()));
                        }
                        if(JSONPath.read(json.toString(),"$.public_product_base_query_response.result.img")!=null){
                            goodsInfoModel.setMainImg(mainImgUrl+JSONPath.read(json.toString(),"$.public_product_base_query_response.result.img").toString());
                        }
                        if(JSONPath.read(json.toString(),"$.public_product_base_query_response.result.shopName")!=null){
                            goodsInfoModel.setShopName(JSONPath.read(json.toString(),"$.public_product_base_query_response.result.shopName").toString());
                        }
                        if(JSONPath.read(json.toString(),"$.public_product_base_query_response.result.images")!=null){
                            List<String > minImgs=(List<String >)JSONPath.read(json.toString(),"$.public_product_base_query_response.result.images");
                            List<String > minImgs2=new ArrayList<>();
                            for(String str:minImgs){
                                str=imgUrl+str;
                                minImgs2.add(str);
                            }
                            goodsInfoModel.setMinImgs(minImgs2);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("调用获取商品详情接口出错",e);
        }
    }


    /**
     * 获取选品包商品id
     * @param packageId
     * @return
     */
    public List<Object> getGoodsSkuIdBypackageId(String packageId,int pageNo,int pageSize){
        List<Object> skuId=new ArrayList<Object> ();
        String response=null;
        Map<String,Object> paramJson=new HashMap<>();
        paramJson.put("pageNo",pageNo);
        paramJson.put("pageSize",pageSize);
        paramJson.put("pkgId",packageId);
        try {
            response = send(paramJson,"jd.kepler.xuanpin.getskuidlist","1.0");
            if(Detect.notEmpty(response)&&!response.equals("0")&&!response.equals("-1")){
                JSONObject json = JSON.parseObject(response);
                if(JSONPath.read(json.toString(),"$.jd_kepler_xuanpin_getskuidlist_response.code")!=null
                        &&JSONPath.read(json.toString(),"$.jd_kepler_xuanpin_getskuidlist_response.code").equals("0")){
                    if(JSONPath.read(json.toString(),"$.jd_kepler_xuanpin_getskuidlist_response.list")!=null){
                        skuId=(List<Object>)JSONPath.eval(json,"$.jd_kepler_xuanpin_getskuidlist_response.list");
                    }
                }
            }

        } catch (Exception e) {
            log.error("调用获取推广链接接口出错",e);
        }
        return skuId;
    }


    /**
     * 获取商品优惠券
     * @param skuId
     * @return
     */
    public List<JdCouponModel> getGoodsCoupons(String skuId){
        List<JdCouponModel> JdCouponModels=new ArrayList<JdCouponModel> ();
        String response=null;
        Map<String,Object> paramJson=new HashMap<>();
        paramJson.put("sku",skuId);
        try {
            response = send(paramJson,"jd.kpl.open.item.findjoinactives","2.0");
            if(Detect.notEmpty(response)&&!response.equals("0")&&!response.equals("-1")){
                JSONObject json = JSON.parseObject(response);
                if(JSONPath.read(json.toString(),"$.jd_kpl_open_item_findjoinactives_response.code")!=null
                        &&JSONPath.read(json.toString(),"$.jd_kpl_open_item_findjoinactives_response.code").equals("0")){
                    if(JSONPath.read(json.toString(),"$.jd_kpl_open_item_findjoinactives_response.coupons")!=null){
                        List<JSONObject> stringList=(List<JSONObject>)JSONPath.eval(json,"$.jd_kpl_open_item_findjoinactives_response.coupons");
                        for(JSONObject jsonObject : stringList){
                            JdCouponModel jdCouponModel=JSON.parseObject(jsonObject.toString(),JdCouponModel.class);
                            jdCouponModel.setMUrl("https:"+jdCouponModel.getMUrl().split(",")[0]);//获取h5链接
                            JdCouponModels.add(jdCouponModel);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("调用获取商品优惠券信息接口出错",e);
        }
        return JdCouponModels;
    }



    /**
     * 获取相似商品
     * @param skuId
     * @return
     */
    public List<Object> getSimilarGoods(String skuId){
        List<Object> skuIds=new ArrayList<Object> ();
        String response=null;
        Map<String,Object> paramJson=new HashMap<>();
        skuIds.add(skuId);
        paramJson.put("set",skuIds);
        try {
            response = send(paramJson,"jd.kpl.open.shopinfo.sku","1.0");
            skuIds.clear();
            if(Detect.notEmpty(response)&&!response.equals("0")&&!response.equals("-1")){
                JSONObject json = JSON.parseObject(response);
                if(JSONPath.read(json.toString(),"$.jd_kpl_open_shopinfo_sku_response.code")!=null
                        &&JSONPath.read(json.toString(),"$.jd_kpl_open_shopinfo_sku_response.code").equals("0")){
                    if(JSONPath.read(json.toString(),"$.jd_kpl_open_shopinfo_sku_response.result")!=null){
                        JSONObject result=(JSONObject) JSONPath.eval(json,"$.jd_kpl_open_shopinfo_sku_response.result");
                        if(Detect.notEmpty(result)){
                            List<JSONObject> stringList=(List<JSONObject>)result.get(skuId);
                            for(JSONObject jsonObject : stringList){
                                skuIds.add(jsonObject.get("skuId").toString());
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("调用获取类似商品信息接口出错",e);
        }
        return skuIds;
    }
    /**
     * 关键字搜索
     * @param
     * @return
     */
    public List<GoodsInfoModel> getGoodsBykeyWord(String keywords, int order, int orderField, int pageSize, int pageNum){
        List<GoodsInfoModel> goodsInfoModelList =new ArrayList<GoodsInfoModel> ();
        String response=null;
        Map<String,Object> paramJson=new HashMap<>();
        Map<String,Object> pageParam=new HashMap<>();
        pageParam.put("pageSize",pageSize);
        pageParam.put("pageNum",pageNum);
        Map<String,Object> queryParam=new HashMap<>();
        queryParam.put("keywords",keywords);
        queryParam.put("sellerType",6);
        paramJson.put("pageParam",pageParam);
        paramJson.put("queryParam",queryParam);
        paramJson.put("orderField",orderField);//排序类型 排序属性，0:综合排序;5:销量排序;6:新品排序;4:价格
        if(orderField==4){
            paramJson.put("order",order);//排序方式 	升降序，1:升序，0:降序，只有按价格排序支持且必填
        }
        try {
            response = send(paramJson,"jd.kpl.open.xuanpin.search.sku","1.0");
            if(Detect.notEmpty(response)&&!response.equals("0")&&!response.equals("-1")){
                JSONObject json = JSON.parseObject(response);
                if(JSONPath.read(json.toString(),"$.jd_kpl_open_xuanpin_search_sku_response.code")!=null
                        &&JSONPath.read(json.toString(),"$.jd_kpl_open_xuanpin_search_sku_response.code").equals("0")){
                    if(JSONPath.read(json.toString(),"$.jd_kpl_open_xuanpin_search_sku_response.skuList")!=null){
                        List<JSONObject> stringList=(List<JSONObject>)JSONPath.eval(json,"$.jd_kpl_open_xuanpin_search_sku_response.skuList.list");
                        for(JSONObject jsonObject : stringList){
                            GoodsInfoModel goodsInfoModel =new GoodsInfoModel();
                            goodsInfoModel.setSkuId(jsonObject.getString("skuId"));
                            JSONArray jsonArray=jsonObject.getJSONArray("coupons");
                            List<JdCouponModel> jdCouponModelList=new ArrayList<> ();
                            if(Detect.notEmpty(jsonArray)){
                                for(Object object:jsonArray){
                                    JdCouponModel jdCouponModel=JSON.parseObject(object.toString(),JdCouponModel.class);
                                    jdCouponModel.setMUrl("https:"+JSON.parseObject(object.toString()).getString("url"));
                                    jdCouponModelList.add(jdCouponModel);
                                }
                            }
                            goodsInfoModel.setJdCouponModels(jdCouponModelList);
                            goodsInfoModelList.add(goodsInfoModel);
                        }
                    }
                }
            }

        } catch (Exception e) {
            log.error("调用获取商品关键字搜索信息接口出错",e);
        }
        return goodsInfoModelList;
    }


    public static void main(String[] args) throws Exception {
//        String str=   HttpsUtil.getClient("https://kploauth.jd.com/oauth/token?grant_type=oauth_refresh_token&app_key=e81a8e5d818690a75e3babcff11f72d9&app_secret=fffb37c81b8a48a5b343170fa4d0a131&refresh_token=d85d3e37ab784b3caf78acdeb556f7429");
        JdAuthServiceImpl jdAuthService=new JdAuthServiceImpl();

        System.out.println(jdAuthService.getSimilarGoods("100003765665").toString());
    }
}

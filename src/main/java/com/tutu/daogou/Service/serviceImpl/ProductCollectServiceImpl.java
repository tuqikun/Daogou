package com.tutu.daogou.Service.serviceImpl;

import com.tutu.daogou.Service.JdProductService;
import com.tutu.daogou.Service.ProductCollectService;
import com.tutu.daogou.Service.TaobaoProductService;
import com.tutu.daogou.enums.GoodsTypeEnum;
import com.tutu.daogou.model.GoodsInfoModel;
import com.tutu.daogou.model.UserBrowseLogModel;
import com.tutu.daogou.pojo.UserProductCollect;
import com.tutu.daogou.repository.UserProductCollectRepository;
import com.tutu.daogou.util.DateUtil;
import com.tutu.daogou.util.Detect;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("productCollectServiceImpl")
@Transactional
public class ProductCollectServiceImpl implements ProductCollectService {

    @Autowired
    private UserProductCollectRepository userProductCollectRepository;
    @Autowired
    private JdProductService jdProductService;
    @Autowired
    private TaobaoProductService taobaoProductService;

    public boolean collect(String userId,String goodsType,String skuId){
        boolean isCollect=false;
        if(Detect.notEmpty(userId)){
            UserProductCollect userProductCollect=userProductCollectRepository.findByUserIdAndGoodsTypeAndSkuId(userId,goodsType,skuId);
            if(userProductCollect!=null){
                isCollect=true;
            }
        }
        return isCollect;
    }

    public void addCollection(String userId,String goodsType,String skuId) {
        UserProductCollect userProductCollect=userProductCollectRepository.findByUserIdAndGoodsTypeAndSkuId(userId,goodsType,skuId);
        if(userProductCollect==null){
            userProductCollect=new UserProductCollect();
            userProductCollect.setCreateTime(DateUtil.getNow());
            userProductCollect.setUserId(userId);
            userProductCollect.setGoodsType(goodsType);
            userProductCollect.setSkuId(skuId);
            userProductCollectRepository.save(userProductCollect);
        }

    }

    public void deleteCollection(String userId, String  goodsType,String skuId){
        userProductCollectRepository.deleteByUserIdAndGoodsTypeAndSkuId(userId,goodsType,skuId);
    }

    public List<UserBrowseLogModel> getCollectList(String userId, int current, int pageSize){
        List<UserBrowseLogModel> list=new ArrayList<>();
        Pageable pageable = new PageRequest((current - 1), pageSize);
        Page<UserProductCollect> userProductCollects=userProductCollectRepository.findAll(new Specification<UserProductCollect>() {
            @Override
            public Predicate toPredicate(Root<UserProductCollect> root, CriteriaQuery<?> query,
                                         CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>(); //所有的断言
                if(Detect.notEmpty(userId)){
                    predicates.add(cb.equal(root.get("userId").as(String.class),userId));
                }
                Predicate[] pre = new Predicate[predicates.size()];
                Predicate predicate= query.where(predicates.toArray(pre)).orderBy(cb.desc(root.get("createTime").as(Date.class))).getRestriction();
                return predicate;
            }
        }, pageable);
        for(UserProductCollect userProductCollect:userProductCollects){
            UserBrowseLogModel userBrowseLogModel=null;
            GoodsInfoModel goodsInfoModel=null;
            if(userProductCollect.getGoodsType().equals(GoodsTypeEnum.JDGOODS.code)){
                goodsInfoModel =jdProductService.getGoodsBySkuId(userProductCollect.getSkuId());
                if(goodsInfoModel !=null){
                    userBrowseLogModel=new UserBrowseLogModel();
                    userBrowseLogModel.setCreateTime(userProductCollect.getCreateTime());
                    goodsInfoModel.setGoodsType(GoodsTypeEnum.JDGOODS.code);
                    goodsInfoModel.setCollect(true);
                    BeanUtils.copyProperties(goodsInfoModel,userBrowseLogModel);
                }else {//删除失效商品浏览记录
                    userProductCollectRepository.deleteBySkuId(userProductCollect.getSkuId());
                }
            }else if(userProductCollect.getGoodsType().equals(GoodsTypeEnum.TAOBAOGOODS.code)){
                goodsInfoModel=taobaoProductService.getTBproduct(userProductCollect.getSkuId());
                if(goodsInfoModel!=null){
                    userBrowseLogModel=new UserBrowseLogModel();
                    userBrowseLogModel.setCreateTime(userProductCollect.getCreateTime());
                    goodsInfoModel.setGoodsType(GoodsTypeEnum.TAOBAOGOODS.code);
                    goodsInfoModel.setCollect(true);
                    BeanUtils.copyProperties(goodsInfoModel,userBrowseLogModel);
                }else {
                    userProductCollectRepository.deleteBySkuId(userProductCollect.getSkuId());
                }
            }
            if(userBrowseLogModel!=null){
                list.add(userBrowseLogModel);}
        }
        return list;
    }
}

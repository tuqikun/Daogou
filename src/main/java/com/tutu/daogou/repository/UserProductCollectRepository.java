package com.tutu.daogou.repository;

import com.tutu.daogou.pojo.UserProductCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserProductCollectRepository extends JpaRepository<UserProductCollect,Long>, JpaSpecificationExecutor {

    UserProductCollect findByUserIdAndGoodsTypeAndSkuId(String userId, String goodsType, String skuId);

    void deleteByUserIdAndGoodsTypeAndSkuId(String userId, String goodsType, String skuId);

    void deleteBySkuId(String skuId);
}

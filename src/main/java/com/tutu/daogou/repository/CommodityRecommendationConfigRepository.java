package com.tutu.daogou.repository;

import com.tutu.daogou.pojo.CommodityRecommendationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityRecommendationConfigRepository  extends JpaRepository<CommodityRecommendationConfig,String> {

       List<CommodityRecommendationConfig> findAllByStateAndDisplayTypeOrderBySort(String state, String displayType);
}

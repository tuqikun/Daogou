package com.tutu.daogou.repository;

import com.tutu.daogou.pojo.ArcSysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArcSysConfigRepository extends JpaRepository<ArcSysConfig,String> {

    List<ArcSysConfig> findByType(String type);

    ArcSysConfig findByCode(String code);
}

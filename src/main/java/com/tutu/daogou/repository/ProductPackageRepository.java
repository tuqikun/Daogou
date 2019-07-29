package com.tutu.daogou.repository;

import com.tutu.daogou.pojo.ProductPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPackageRepository extends JpaRepository<ProductPackage,Long> {

    List<ProductPackage> findByStateAndDisplayTypeOrderBySort(String state, String displayType);

}

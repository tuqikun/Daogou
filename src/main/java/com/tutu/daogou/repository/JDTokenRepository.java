package com.tutu.daogou.repository;

import com.tutu.daogou.pojo.JDToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JDTokenRepository extends JpaRepository<JDToken,Long> {

    JDToken findByState(String state);
}

package com.tutu.daogou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cl_user_browse_log")
public class UserBorwseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String skuId;

    private Date createTime;

    private String goodsType;

}

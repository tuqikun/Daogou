package com.tutu.daogou.pojo;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "arc_sys_config")
public class ArcSysConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;


    /** 参数编号 唯一 **/
    private String code;

    /** 名称 **/
    private String name;

    /** 值 **/
    private String value;

    /** 类型，1：基础参数，...... **/
    private String type;

    /** 状态，0：禁用，1：启用 **/
    private Boolean status;


    /**备注*/
    private String remark;

    private Date updateTime;
}

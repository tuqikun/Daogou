package com.tutu.daogou.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "JD_token")
@Data
public class JDToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition ="VARCHAR(100) DEFAULT NULL COMMENT '授权token'")
    private String accessToken;

    @Column(columnDefinition ="VARCHAR(100) DEFAULT NULL COMMENT '刷新授权token'")
    private String refershToken;

    @Column(columnDefinition =" datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'")
    private Date createTime;

    @Column(columnDefinition ="VARCHAR(2) DEFAULT NULL COMMENT '10 有效 20 失效'")
    private String state;
}

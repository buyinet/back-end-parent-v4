package com.kantboot.business.ovo.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 用户vip表
 * @author 方某方
 */
@Table(name="bus_ovo_vip")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoVip {

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 天数
     */
    @Column(name = "days")
    private Integer days;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 价格
     */
    @Column(name = "price")
    private Double price;

    /**
     * 赠送O币数量
     */
    @Column(name = "num_of_o_money")
    private Long numOfOMoney;

    /**
     * 编码，用于区分不同的vip，驼峰式
     * 7天会员：sevenDays
     * 月度会员：month
     * 季度会员：quarter
     * 年度会员：year
     */
    @Column(name = "code")
    private String code;

    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;
}

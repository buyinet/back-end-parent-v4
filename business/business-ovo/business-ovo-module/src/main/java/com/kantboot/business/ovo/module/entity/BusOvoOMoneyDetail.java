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
 * O币获取明细的实体类
 * 用来记录已经支付的订单
 * @author 方某方
 */
@Table(name="bus_ovo_o_money_detail")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoOMoneyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 类型编码，驼峰式
     * 充值：recharge
     * 兑换礼物：exchangeGift
     */
    @Column(name="type_code")
    private String typeCode;

    /**
     * O币id
     */
    @Column(name="o_money_id")
    private Long oMoneyId;

    /**
     * 订单号
     */
    @Column(name="order_id")
    private Long orderId;

    /**
     * 花费金额
     */
    @Column(name="amount")
    private Double amount;

    /**
     * 得到的O币数量
     */
    @Column(name="num")
    private Long num;

    /**
     * 用户id
     */
    @Column(name="user_id")
    private Long userId;


    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;


}

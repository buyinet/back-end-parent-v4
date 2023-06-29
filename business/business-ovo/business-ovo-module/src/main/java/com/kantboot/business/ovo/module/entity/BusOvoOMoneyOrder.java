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
 * O币订单的实体类
 * @author 方奕丰
 */
@Table(name="bus_ovo_o_money_order")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoOMoneyOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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
     * 支付状态
     * 订单状态，驼峰式
     * 未支付 unpaid
     * 已支付 paid
     * 已退款 refunded
     * 已取消 canceled
     */
    @Column(name="status_code")
    private String statusCode;


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

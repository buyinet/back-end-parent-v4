package com.kantboot.pay.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 订单的实体类
 * @author 方某方
 */
@Table(name="pay_order")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class PayOrder {

    /**
     * 主键
     * 使用雪花算法生成
     * 同时也是订单号
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 订单金额
     */
    @Column(name="amount")
    private Double amount;

    /**
     * 订单状态，驼峰式
     * 未支付 unpaid
     * 已支付 paid
     * 已退款 refunded
     * 已取消 canceled
     */
    @Column(name="status_code")
    private String statusCode;

    /**
     * 商品编码
     * 用于区分不同的商品
     * 例如：oMoney
     * 例如：oVip
     */
    @Column(name="product_code")
    private String productCode;

    /**
     * 描述
     */
    @Column(name="description")
    private String description;

    /**
     * 货币
     * 例如：CNY
     * 例如：USD
     */
    @Column(name="currency")
    private String currency;

    /**
     * 支付方式编码
     * 例如：wechatPay
     * 例如：alipay
     */
    @Column(name="pay_method_code")
    private String payMethodCode;

    /**
     * 订单发起人的用户id
     */
    @Column(name="user_id")
    private Long userId;

    @CreatedDate
    @Column(name="gmt_create")
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;



}

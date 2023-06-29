package com.kantboot.business.ovo.module.entity;

import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * O币的实体类
 * @author 方奕丰
 */
@Table(name="bus_ovo_o_money")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoOMoney {

    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 金额（人民币）
     */
    @Column(name="amount")
    private Double amount;

    /**
     * O币数量
     */
    @Column(name="num")
    private Long num;

    /**
     * 状态编码
     * 不使用 noUse
     * 使用 use
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

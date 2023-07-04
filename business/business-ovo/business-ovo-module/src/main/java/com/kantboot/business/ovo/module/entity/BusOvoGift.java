package com.kantboot.business.ovo.module.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 礼物表
 * @author 方某方
 */
@Table(name="bus_ovo_gift")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoGift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 礼物编码
     */
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 礼物名称
     */
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 礼物描述
     */
    @Column(name = "description", length = 64)
    private String description;

    /**
     * 礼物展示图的文件id
     * 礼物展示图，用于在礼物列表中展示
     * 还有一个礼物效果图，用于在礼物动画中展示
     */
    @Column(name = "file_id_of_show")
    private Long fileIdOfShow;

    /**
     * 礼物效果图的文件id
     * 礼物展示图，用于在礼物列表中展示
     * 还有一个礼物效果图，用于在礼物动画中展示
     */
    @Column(name = "file_id_of_effect")
    private Long fileIdOfEffect;

    /**
     * 礼物需要的o币数量
     */
    @JSONField(name="costOfMoney")
    @Column(name = "o_money_cost")
    private Long costOfMoney;

    /**
     * 魅力值
     */
    @Column(name = "charm_value")
    private Long charmValue;

    /**
     * 礼物优先级
     * 优先级越高，越靠前
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 礼物状态编码
     * noUse 禁用
     * use 启用
     */
    @Column(name = "status_code", length = 64)
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


















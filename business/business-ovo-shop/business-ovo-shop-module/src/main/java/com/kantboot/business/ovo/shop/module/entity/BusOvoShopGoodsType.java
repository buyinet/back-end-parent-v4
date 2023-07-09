package com.kantboot.business.ovo.shop.module.entity;


import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Ovo商城商品分类表
 */
@Table(name="bus_ovo_shop_goods_type")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "is_delete = false")
public class BusOvoShopGoodsType {
    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 分类名称
     */
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 分类描述
     */
    @Column(name = "description", length = 64)
    private String description;

    /**
     * 分类编码
     */
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 是否删除
     */
    @Column(name = "is_delete")
    private Boolean delete;

    /**
     * 优先级
     */
    @Column(name = "priority")
    private Integer priority;


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

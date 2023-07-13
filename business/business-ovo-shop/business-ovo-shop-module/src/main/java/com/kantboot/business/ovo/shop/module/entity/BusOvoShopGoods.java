package com.kantboot.business.ovo.shop.module.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.List;

/**
 * Ovo商城商品表
 */
@Table(name="bus_ovo_shop_goods")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "is_delete = false")
public class BusOvoShopGoods {
    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 商品标题
     */
    @Column(name = "title", length = 64)
    private String title;

    /**
     * 商品描述
     */
    @Column(name = "description", length = 64)
    private String description;

    /**
     * 商品所需要的魅力积分（情趣碎片）
     * 0 表示不需要
     */
    @Column(name = "charm_point")
    private Long charmPoint;

    /**
     * 商品分类
     */
    @Column(name = "type_code", length = 64)
    private String typeCode;

    /**
     * 商品展示的文件
     * 一对多，并且按照优先级倒序
     */
    @Column(name = "body_file_list_str", length = 500)
    private String bodyFileListStr;

    /**
     * 货源地址
     */
    @JsonIgnore
    @Column(name = "source_url", length = 500)
    private String sourceUrl;


    /**
     * 是否删除
     */
    @Column(name = "is_delete")
    private Boolean delete;


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

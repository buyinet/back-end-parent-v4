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
 * Ovo商城商品文件表
 *
 * @author 方某方
 */
@Table(name = "bus_ovo_shop_goods_body_file")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "is_delete = false")
public class BusOvoShopGoodsBodyFile {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId", strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 商品id
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 优先级
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 文件类型
     * image 图片
     * video 视频
     */
    @Column(name = "file_type")
    private String fileType;

    /**
     * 文件id
     */
    @Column(name = "file_id")
    private Long fileId;

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

package com.kantboot.business.ovo.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * Ovo用户绑定位置表
 * @author 方某某
 */
@Table(name="bus_ovo_user_bind_location")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoUserBindLocation {
    /**
     * 主键
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 经度
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * 纬度
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * 国家
     */
    @Column(name = "country")
    private String country;

    /**
     * 省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 区县
     */
    @Column(name = "district")
    private String district;

    /**
     * 详细位置-标题
     */
    @Column(name = "address_title")
    private String addressTitle;

    /**
     * 详细位置
     */
    @Column(name = "address")
    private String address;

    /**
     * 国家code
     */
    @Column(name = "country_code")
    private String countryCode;

    /**
     * 区域code
     */
    @Column(name = "ad_code")
    private String adCode;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @CreatedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 腾讯地图api调用时间
     */
    @Column(name = "gmt_tencent_map_api_time")
    private Date gmtTencentMapApiTime;

}

package com.kantboot.business.ovo.module.entity;

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
import java.util.List;

/**
 * 帖子
 * @author 方某方
 */
@Table(name="bus_ovo_post")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoPost {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 帖子标题
     * 暂时用不到
     */
    @Column(name = "title", length = 64)
    private String title;

    /**
     * 帖子内容
     */
    @Column(name = "content", length = 64)
    private String content;

    /**
     * 可见
     * all 所有人可见
     * friend 仅好友可见
     * self 仅自己可见
     * follower 仅关注者可见
     */
    @Column(name = "visibleCode", length = 64)
    private String visibleCode;

    /**
     * 图片列表
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private List<BusOvoPostImage> imageList;

    /**
     * 发帖人id
     */
    @Column(name = "userId")
    private Long userId;

    /**
     * 发帖时纬度
     */
    @Column(name = "latitude")
    private Double latitude;

    /**
     * 发帖时经度
     */
    @Column(name = "longitude")
    private Double longitude;

    /**
     * 发帖时省份
     */
    @Column(name = "province")
    private String province;

    /**
     * 发帖时城市
     */
    @Column(name = "city")
    private String city;

    /**
     * 发帖时区域
     */
    @Column(name = "district")
    private String district;

    /**
     * 发帖时区域编码
     */
    @Column(name = "ad_code")
    private String adCode;

    /**
     * 选择显示的纬度
     */
    @Column(name = "latitude_of_select")
    private Double latitudeOfSelect;

    /**
     * 选择显示的经度
     */
    @Column(name = "longitude_of_select")
    private Double longitudeOfSelect;

    /**
     * 选择显示的区域编码
     */
    @Column(name = "adCode_of_select")
    private String adCodeOfSelect;

    /**
     * 选择显示的地址
     */
    @Column(name = "address_title_of_select")
    private String addressTitleOfSelect;

    /**
     * 选择的地址级别
     * 省 province
     * 市 city
     * 区县 district
     * 具体地址 specific
     */
    @Column(name = "address_level_of_select")
    private String addressLevelOfSelect;

    /**
     * 审核状态编码，驼峰式
     * 待审核 wait
     * 审核通过 pass
     * 审核不通过 reject
     * 审核中 ing
     * 审核异常 error
     */
    @Column(name = "audit_status_code")
    private String auditStatusCode;

    /**
     * 审核不通过原因
     */
    @Column(name = "audit_reject_reason")
    private String auditRejectReason;


    /**
     * 选择显示的地址
     */
    @Column(name = "address_of_select")
    private String addressOfSelect;

    /**
     * 选择显示的省份
     */
    @Column(name = "province_of_select")
    private String provinceOfSelect;

    /**
     * 选择显示的城市
     */
    @Column(name = "city_of_select")
    private String cityOfSelect;

    /**
     * 选择显示的区域
     */
    @Column(name = "district_of_select")
    private String districtOfSelect;

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

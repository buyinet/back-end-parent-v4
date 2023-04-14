package com.kantboot.third.party.module.entity;

import com.kantboot.system.module.entity.SysUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 微信用户表
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "tp_wechat_user")
@EntityListeners(AuditingEntityListener.class)
public class TpWechatUser {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    /**
     * 平台编码
     */
    @Column(name="platform_code")
    private String platformCode;

    /**
     * 微信公众平台提供用来识别的一种用户ID
     */
    @Column(name="openid")
    private String openid;

    /**
     * 微信开放平台提供用来识别的一种用户ID
     */
    @Column(name="unionid")
    private String unionid;

    /**
     * 昵称
     */
    @Column(name="nickname")
    private String nickname;

    /**
     * 性别(0 女，1 男)
     */
    @Column(name="gender")
    private Integer gender;

    /**
     * 城市
     */
    @Column(name="city")
    private String city;

    /**
     * 省份
     */
    @Column(name="province")
    private String province;

    /**
     * 国家
     */
    @Column(name="country")
    private String country;

    /**
     * 头像
     */
    @Column(name="avatar_url")
    private String avatarUrl;

    /**
     * 创建时间
     */
    @Column(name="gmt_create",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date gmtCreate;

    /**
     * 最后一次修改时间
     */
    @Column(name="gmt_modified",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date gmtModified;

    /**
     * 查看关联的用户实体
     */
    @OneToOne(targetEntity = SysUser.class,
            cascade = CascadeType.DETACH,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",referencedColumnName = "id",insertable = false,updatable = false)
    private SysUser sysUser;
}

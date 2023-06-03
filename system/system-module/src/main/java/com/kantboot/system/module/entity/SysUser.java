package com.kantboot.system.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysUser implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 用户名
     */
    @Column(name = "username", length = 64)
    private String username;

    /**
     * 密码
     * 此处非明文密码，而是加密后的密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 64)
    private String nickname;

    /**
     * 用户自我介绍
     */
    @Column(name = "introduction", length = 1024)
    private String introduction;

    /**
     * 头像文件的id
     */
    @Column(name = "file_id_of_avatar")
    private Long fileIdOfAvatar;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 64)
    private String email;

    /**
     * 手机号的国际区号
     * 如:中国(+86)、美国(+1)、英国(+44)、日本(+81)、韩国(+82)、台湾(+886)、香港(+852)、澳门(+853)
     */
    @Column(name = "phone_area_code", length = 64)
    private String phoneAreaCode;


    /**
     * 手机号
     */
    @Column(name = "phone", length = 64)
    private String phone;

    /**
     * 性别
     * -1：保密
     * 0：女
     * 1：男
     */
    @Column(name="gender",length = 1)
    private Integer gender;

    /**
     * 是否激活
     */
    @Column(name = "is_active")
    private Boolean active;

    /**
     * 是否锁定
     */
    @Column(name = "is_locked")
    private Boolean locked;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Boolean deleted;

    /**
     * 是否适用于所有平台
     * 由于一个系统可能有多个平台，所以一个用户可能适用于所有平台，也可能只适用于某个平台
     * 为true时，platformCode字段无效
     */
    @Column(name = "is_all_platform")
    private Boolean allPlatform;

    /**
     * 平台编码
     * 由于一个系统可能有多个平台
     */
    @Column(name = "platform_code", length = 64)
    private String platformCode;

    /**
     * 创建时的UserAgent
     */
    @Column(name = "create_user_agent",length = 1024)
    private String createUserAgent;

    /**
     * 创建时的ip
     */
    @Column(name = "create_ip")
    private String createIp;

    /**
     * 创建时的设备
     */
    @Column(name = "create_device",length = 1024)
    private String createDevice;

    /**
     * 生日
     */
    @Column(name = "birthday")
    private Date birthday;

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

    /**
     * 关联的角色
     * 一个用户可以有多个角色
     */
    @ManyToMany(targetEntity = SysRole.class,fetch = FetchType.EAGER)
    @JoinTable(name = "rel_sys_user_and_sys_role",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_code",referencedColumnName = "code")}
    )
    private List<SysRole> roles;


}
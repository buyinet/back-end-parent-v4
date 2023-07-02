package com.kantboot.system.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统token实体类
 * 为什么要把token放到数据库中一遍，而不是只放到redis中？
 *      为了方便管理，可以更方便的在后台管理系统中增删查改token
 * 当然也会放进redis中，这样可以更快的校验token
 * @author 方某方
 */
@Table(name="sys_token")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysToken implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * token
     */
    @Column(name = "token")
    private String token;

    /**
     * 语言编码
     */
    @Column(name = "language_code")
    private String languageCode;

    /**
     * 场景编码
     * 校验token时使用
     */
    @Column(name = "scene_code")
    private String sceneCode;

    /**
     * 创建时的User-Agent
     */
    @Column(name = "create_user_agent")
    private String createUserAgent;

    /**
     * 创建时的ip
     */
    @Column(name = "create_ip")
    private String createIp;

    /**
     * 最后一次更新时的ip
     */
    @Column(name = "last_ip")
    private String lastIp;

    /**
     * 创建时的设备
     */
    @Column(name = "create_device")
    private String createDevice;

    /**
     * 最后一次进入时间
     */
    @Column(name = "gmt_last")
    private Date gmtLast;

    /**
     * 创建时间
     */
    @CreatedBy
    @Column(name = "gmt_create")
    private Date gmtCreate;


    /**
     * 过期时间
     */
    @Column(name = "gmt_expire")
    private Date gmtExpire;

    /**
     * 刷新时间
     */
    @Column(name = "gmt_refresh")
    private Date gmtRefresh;

    /**
     * 最后一次更新时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    @Transient
    private SysUser user;

}
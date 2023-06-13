package com.kantboot.system.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
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
@Data
@Accessors(chain = true)
public class SysToken implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * token
     */
    private String token;

    /**
     * 语言编码
     */
    private String languageCode;

    /**
     * 场景编码
     * 校验token时使用
     */
    private String sceneCode;

    /**
     * 创建时的User-Agent
     */
    private String createUserAgent;

    /**
     * 创建时的ip
     */
    private String createIp;

    /**
     * 最后一次更新时的ip
     */
    private String lastIp;

    /**
     * 创建时的设备
     */
    private String createDevice;

    /**
     * 最后一次进入时间
     */
    private Date gmtLast;

    /**
     * 创建时间
     */
    private Date gmtCreate;


    /**
     * 过期时间
     */
    private Date gmtExpire;

    /**
     * 刷新时间
     */
    private Date gmtRefresh;

    private Date gmtModified;

    private SysUser user;

}
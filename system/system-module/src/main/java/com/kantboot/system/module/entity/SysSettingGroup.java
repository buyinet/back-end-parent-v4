package com.kantboot.system.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 系统设置分组实体类
 * 用于区分不同的设置分组
 * 例如：系统设置、用户设置、角色设置、邮箱设置等
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "sys_setting_group")
@EntityListeners(AuditingEntityListener.class)
public class SysSettingGroup {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 设置编码
     */
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 设置名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 设置描述
     */
    @Column(name = "description")
    private String description;


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
     * 最后一次修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

}

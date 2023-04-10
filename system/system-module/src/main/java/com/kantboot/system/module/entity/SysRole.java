package com.kantboot.system.module.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "sys_role")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysRole implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 角色编码
     */
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 字典编码
     */
    @Column(name = "dict_code", length = 64)
    private String dictCode;

    /**
     * 角色名称
     */
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 角色描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 角色优先级
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

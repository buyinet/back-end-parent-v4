package com.kantboot.system.module.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @JSONField(serialize = false)
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
    @Column(name = "name")
    private String name;

    /**
     * 角色描述
     */
    @JSONField(serialize = false)
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


    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

package com.kantboot.business.ovo.module.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 虐恋属性
 * 用于描述用户的虐恋属性
 * @author 方某方
 */
@Table(name="bus_ovo_sadomasochism_attr")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoSadomasochismAttr {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 虐恋属性编码
     */
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 虐恋属性名称
     */
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 虐恋属性描述
     */
    @Column(name = "description", length = 64)
    private String description;

    /**
     * 虐恋属性排序
     */
    @Column(name = "sort")
    private Integer sort;

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

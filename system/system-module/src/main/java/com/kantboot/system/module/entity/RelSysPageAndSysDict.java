package com.kantboot.system.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 页面和字典关联实体类
 * 用于记录页面和字典的关联关系
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "rel_sys_page_and_sys_dict")
@EntityListeners(AuditingEntityListener.class)
public class RelSysPageAndSysDict {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 页面编码
     */
    @Column(name = "page_code",length = 64)
    private String pageCode;

    /**
     * 字典编码
     */
    @Column(name = "dict_code",length = 64)
    private String dictCode;


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

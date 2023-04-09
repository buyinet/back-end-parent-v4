package com.kantboot.system.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 字典国际化实体类
 * @author 方某方
 */
@Table(name = "sys_dict_i18n")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysDictI18n {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 字典编码
     */
    @Column(name = "dict_code", length = 64)
    private String dictCode;

    /**
     * 字典分组编码
     */
    @Column(name = "dict_group_code", length = 64)
    private String dictGroupCode;

    /**
     * 语言
     */
    @Column(name = "language_code", length = 64)
    private String languageCode;

    /**
     * 内容
     */
    @Column(name = "t_value")
    private String value;

    /**
     * 描述
     */
    @CreatedDate
    @Column(name = "description")
    private String description;

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

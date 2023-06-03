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
 * 情感取向
 * @author 方某方
 */
@Table(name="bus_ovo_emotional_orientation")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoEmotionalOrientation {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 情感取向编码
     */
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 情感取向名称
     */
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 情感取向描述
     */
    @Column(name = "description", length = 64)
    private String description;

    /**
     * 情感取向排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 性别限制
     * -1 不限
     * 0 女
     * 1 男
     */
    @Column(name="gender_limit")
    private Integer genderLimit;

    /**
     * 是否有性取向限制
     */
    @Column(name="is_sexual_orientation_limit", columnDefinition = "bit(1) default 0")
    private Boolean sexualOrientationLimit;


    /**
     * 情感取向状态
     */
    @Column(name = "status")
    private Integer status;

    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;
}

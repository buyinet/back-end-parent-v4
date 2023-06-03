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
 * 性取向
 * @author 方某方
 */
@Table(name="bus_ovo_sexual_orientation")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoSexualOrientation {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 性取向编码
     */
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 性取向名称
     */
    @Column(name = "name", length = 64)
    private String name;

    /**
     * 性取向描述
     */
    @Column(name = "description", length = 64)
    private String description;

    /**
     * 性取向排序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 性取向状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 性取向创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 性取向修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;
}

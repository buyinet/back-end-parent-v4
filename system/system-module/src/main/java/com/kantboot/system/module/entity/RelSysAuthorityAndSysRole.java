package com.kantboot.system.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 权限和角色关联实体类
 * 用于记录权限和角色的关联关系
 * 一个权限可以被多个角色拥有
 * 一个角色可以拥有多个权限
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "rel_sys_authority_and_sys_role")
@EntityListeners(AuditingEntityListener.class)
public class RelSysAuthorityAndSysRole {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 权限编码
     */
    @Column(name = "authority_code")
    private String authorityCode;

    /**
     * 角色编码
     */
    @Column(name = "role_code")
    private String roleCode;

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

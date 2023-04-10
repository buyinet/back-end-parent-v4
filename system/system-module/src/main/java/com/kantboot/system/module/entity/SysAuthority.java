package com.kantboot.system.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Set;

/**
 * 权限实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "sys_authority")
@EntityListeners(AuditingEntityListener.class)
public class SysAuthority {

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
    @Column(name = "code", length = 64)
    private String code;

    /**
     * 权限描述
     */
    @Column(name = "description")
    private String description;

    /**
     * uri
     */
    @Column(name = "uri")
    private String uri;

    /**
     * 是否需要登录才能访问
     */
    @Column(name = "is_need_login")
    private Boolean needLogin;

    /**
     * 是否对应角色才能访问
     */
    @Column(name = "is_need_role")
    private Boolean needRole;


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


    /**
     * 关联的角色
     */
    @ManyToMany(targetEntity = SysRole.class,fetch = FetchType.EAGER)
    @JoinTable(name = "rel_sys_authority_and_sys_role",
            joinColumns = {@JoinColumn(name = "authority_code",referencedColumnName = "code")},
            inverseJoinColumns = {@JoinColumn(name = "role_code",referencedColumnName = "code")}
    )
    private Set<SysRole> roles;

}

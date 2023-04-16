package com.kantboot.admin.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

/**
 * 菜单实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "admin_menu")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class AdminMenu {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 菜单编码
     * 用来标识菜单的唯一性
     * 也用于国际化，即菜单名称的国际化
     */
    @Column(name = "code",length = 64)
    private String code;

    /**
     * 菜单名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 菜单路径
     */
    @Column(name = "path")
    private String path;

    /**
     * 组件
     */
    @Column(name = "component")
    private String component;

    /**
     * 菜单图标
     */
    @Column(name = "file_id_of_icon")
    private Long fileIdOfIcon;

    /**
     * 菜单优先级
     */
    @Column(name = "priority")
    private Integer priority;

    /**
     * 是否是目录
     */
    @Column(name = "is_directory")
    private Boolean directory;

    /**
     * 目录的菜单code
     */
    @Column(name = "menu_code_of_directory")
    private String menuCodeOfDirectory;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    @OneToMany
    @JoinColumn(name = "menu_code_of_directory",referencedColumnName = "code")
    private List<AdminMenu> children;

}

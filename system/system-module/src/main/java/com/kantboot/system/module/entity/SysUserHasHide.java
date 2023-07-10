package com.kantboot.system.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 * @author 方某方
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user")
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysUserHasHide implements Serializable {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 64)
    private String nickname;

    /**
     * 用户自我介绍
     */
    @Column(name = "introduction", length = 1024)
    private String introduction;

    /**
     * 头像文件的id
     */
    @Column(name = "file_id_of_avatar")
    private Long fileIdOfAvatar;

    /**
     * 性别
     * -1：保密
     * 0：女
     * 1：男
     */
    @Column(name="gender",length = 1)
    private Integer gender;

    /**
     * 关联的角色
     * 一个用户可以有多个角色
     */
    @ManyToMany(targetEntity = SysRole.class,fetch = FetchType.EAGER)
    @JoinTable(name = "rel_sys_user_and_sys_role",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_code",referencedColumnName = "code")}
    )
    private List<SysRole> roles;

    /**
     * 生日
     */
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 关联用户在线表
     */
    @OneToOne(targetEntity = SysUserOnline.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "id",referencedColumnName = "user_id")
    private SysUserOnline userOnline;

    @Transient
    private Boolean online;

    @Transient
    public SysUserHasHide setOnline(Boolean online){
        return this;
    }

    @Transient
    public Boolean getOnline() {
        if (this.userOnline == null) {
            return false;
        }
        if (this.userOnline.getGmtOnline() == null) {
            return false;
        }
        // 如果在线时间在3分钟内，则认为在线
        return System.currentTimeMillis() - this.userOnline.getGmtOnline().getTime() < 3 * 60 * 1000;
    }


}
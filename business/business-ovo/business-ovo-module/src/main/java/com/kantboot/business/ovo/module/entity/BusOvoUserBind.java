package com.kantboot.business.ovo.module.entity;

import com.kantboot.system.module.entity.SysUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

/**
 * Ovo用户绑定表
 * @author 方某方
 */
@Table(name="bus_ovo_user_bind")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoUserBind {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 性取向code
     */
    @Column(name = "sexual_orientation_code")
    private String sexualOrientationCode;

    /**
     * 性虐属性code
     */
    @Column(name = "sadomasochism_attr_code")
    private String sadomasochismAttrCode;

    /**
     * 自我介绍
     */
    @Column(name = "introduction")
    private String introduction;

    @ManyToMany
    @JoinTable(name = "rel_bus_ovo_user_bind_and_bus_ovo_emotional_orientation",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "emotional_orientation_code", referencedColumnName = "code"))
    private List<BusOvoEmotionalOrientation> emotionalOrientationList;

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
     * 关联的用户
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUser user;

    /**
     * 关联的用户绑定位置
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private BusOvoUserBindLocation location;

}

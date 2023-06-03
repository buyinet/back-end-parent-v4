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
 * Ovo用户绑定表
 * @author 方某方
 */
@Table(name="rel_bus_ovo_user_bind_and_bus_ovo_emotional_orientation")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class RelBusOvoUserBindAndBusOvoEmotionalOrientation {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 用户绑定id
     */
    @Column(name = "user_bind_id")
    private Long userBindId;

    /**
     * 情感取向id
     */
    @Column(name = "emotional_orientation_code")
    private String emotionalOrientationCode;

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

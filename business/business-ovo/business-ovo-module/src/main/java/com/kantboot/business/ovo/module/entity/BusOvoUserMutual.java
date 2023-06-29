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
 * Ovo用户互相关注表
 * @author 方某方
 */
@Table(name="bus_ovo_user_mutual")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoUserMutual {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 非数据库字段，但是需要用到的字段，可以被映射到数据库中
     */
    @Column(name = "user_id")
    private Long userId;

    @Transient
    private BusOvoUser ovoUser;

    /**
     * 用户id
     */
    @Column(name = "user1_id")
    private Long user1Id;

    /**
     * 互相关注的用户id
     */
    @Column(name = "user2_id")
    private Long user2Id;

    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

}

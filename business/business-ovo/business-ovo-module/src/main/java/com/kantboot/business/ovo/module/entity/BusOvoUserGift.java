package com.kantboot.business.ovo.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 用户礼物表
 * 用于记录用户的礼物
 * @author 方某方
 */
@Table(name="bus_ovo_user_gift")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoUserGift {
    /**
     * 主键
     * 用户id
     */
    @Id
    @Column(name = "user_id")
    private Long userId;

    /**
     * 礼物id
     */
    @Column(name = "gift_id")
    private Long giftId;

    /**
     * 礼物数量
     */
    @Column(name = "gift_num")
    private Long giftNum;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;
}

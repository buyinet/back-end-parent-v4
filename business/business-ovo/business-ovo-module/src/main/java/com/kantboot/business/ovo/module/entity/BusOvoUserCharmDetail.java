package com.kantboot.business.ovo.module.entity;

import jakarta.annotation.Resource;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 用户魅力值明细表
 * 用于记录用户的魅力值明细
 * @author 方某方
 */
@Table(name="bus_ovo_user_charm_detail")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoUserCharmDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 类型编码，驼峰式
     * 礼物: give
     * 点赞: like
     */
    @Column(name="type_code")
    private String typeCode;

    /**
     * 礼物编码
     */
    @Column(name="gift_code")
    private String giftCode;

    /**
     * 礼物数量
     */
    @Column(name="gift_num")
    private Long giftNum;

    /**
     * fromUserId
     * 谁送出了礼物
     */
    @Column(name="from_user_id")
    private Long fromUserId;

    /**
     * toUserId
     * 谁收到了礼物
     */
    @Column(name="to_user_id")
    private Long toUserId;

    /**
     * toPostId
     * 收到礼物的帖子id
     */
    @Column(name="to_post_id")
    private Long toPostId;

    /**
     * 得到的魅力值
     */
    @Column(name="charm_value")
    private Long charmValue;

    @OneToOne
    @JoinColumn(name = "from_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private BusOvoUser fromUser;

    @OneToOne
    @JoinColumn(name = "to_user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private BusOvoUser toUser;

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

package com.kantboot.business.ovo.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 聊天室和用户绑定关系表
 * 用于记录用户和聊天室的绑定关系
 * @author 方某方
 */
@Table(name="rel_bus_ovo_chat_room_and_bus_ovo_user_bind")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class RelBusOvoChatRoomAndBusOvoUserBind {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**
     * 聊天室id
     */
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 多对一关系
     * 一个用户可以绑定多个聊天室
     * 一个聊天室可以绑定多个用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id",insertable = false,updatable = false)
    private BusOvoChatRoom busOvoChatRoom;

    /**
     * 多对一关系
     * 一个用户可以绑定多个聊天室
     * 一个聊天室可以绑定多个用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    private BusOvoUserBind busOvoUser;


    /**
     * 创建时间
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gmt_modified")
    private Date gmtModified;

}

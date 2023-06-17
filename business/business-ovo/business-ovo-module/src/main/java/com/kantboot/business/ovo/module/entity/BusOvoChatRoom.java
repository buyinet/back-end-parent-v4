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
import java.util.List;

/**
 * Ovo用户聊天室表
 * 一个用户可以加入多个聊天室
 * 一个聊天室可以有多个用户
 * @author 方某方
 */
@Table(name="bus_ovo_chat_room")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoChatRoom {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 聊天室名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 聊天室类型编码
     * privateChat 私聊
     * groupChat 群聊
     */
    @Column(name = "type_code")
    private String typeCode;

    /**
     * 不要建立外键关系和索引关系
     */
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_bus_ovo_chat_room_and_bus_ovo_user_bind",
            joinColumns = @JoinColumn(name = "chat_room_id",referencedColumnName = "id",insertable = false,updatable = false),
            inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id",insertable = false,updatable = false))
    private List<BusOvoUserBind> ovoUserList;


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

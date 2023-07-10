package com.kantboot.system.module.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

/**
 * 用户在线信息
 * 用于记录用户的在线信息
 * @author 方某方
 */
@Table(name="sys_user_online")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysUserOnline {

    @Id
    private Long userId;

    /**
     * 在线时间
     */
    @Column(name = "gmt_online")
    private Date gmtOnline;

    /**
     * 最后一次访问的路径
     */
    @Column(name = "last_url")
    private String lastUrl;


}

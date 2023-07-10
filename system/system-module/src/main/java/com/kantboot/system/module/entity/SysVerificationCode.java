package com.kantboot.system.module.entity;


import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Table(name="sys_verification_code")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class SysVerificationCode {

    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 验证码
     */
    @Column(name = "verification_code")
    private String verificationCode;

    /**
     * 如果类型为phone，则为手机号，如果类型为mail，则为邮箱
     * 用于发送验证码
     */
    @Column(name = "send_to")
    private String sendTo;

    /**
     * 验证码类型
     * email 邮箱验证码
     * phone 手机验证码
     */
    @Column(name = "type_code")
    private String typeCode;

    /**
     * 验证码行为编码
     * bindEmail 绑定邮箱
     * bindPhone 绑定手机
     */
    @Column(name = "action_code")
    private String actionCode;

    @CreatedBy
    @Column(name = "gmt_create")
    private Date gmtCreate;

    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 过期时间
     * 用于判断验证码是否过期
     */
    @Column(name = "gmt_expire")
    private Date gmtExpire;


}

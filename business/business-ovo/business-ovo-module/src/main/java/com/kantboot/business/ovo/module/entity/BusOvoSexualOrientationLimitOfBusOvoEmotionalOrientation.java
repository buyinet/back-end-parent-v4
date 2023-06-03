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
 * 情感取向的性取向限制
 * @author 方奕丰
 */
@Table(name = "bus_ovo_sexual_orientation_limit_of_emotional_orientation")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoSexualOrientationLimitOfBusOvoEmotionalOrientation {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 性取向code
     */
    @Column(name = "sexual_orientation_code",length = 64)
    private String sexualOrientationCode;

    /**
     * 情感取向code
     */
    @Column(name = "emotional_orientation_code",length = 64)
    private String emotionalOrientationCode;

    /**
     * 性取向创建时间
     */
    @CreatedDate
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 性取向修改时间
     */
    @LastModifiedDate
    @Column(name = "gmt_modified")
    private Date gmtModified;
}

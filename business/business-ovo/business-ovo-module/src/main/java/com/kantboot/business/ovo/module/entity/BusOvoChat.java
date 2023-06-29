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
 * 用户聊天室关系表
 * @author 方某方
 */
@Table(name="bus_ovo_chat")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoChat {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 聊天室id
     */
    @Column(name = "room_id")
    private Long roomId;

    /**
     * 发消息的用户
     */
    @Column(name = "user_id_of_send")
    private Long userIdOfSend;

    /**
     * 时长
     */
    @Column(name = "duration")
    private Long duration;

    /**
     * 封面，仅有视频有
     */
    @Column(name = "file_id_of_cover")
    private Long fileIdOfCover;

    /**
     * 类型
     * text 文本
     * image 图片
     * video 视频
     * audio 音频
     * voice 语音
     */
    @Column(name = "type_code")
    private String typeCode;

    /**
     * 内容
     */
    @Column(name = "content",length = 3000)
    private String content;

    /**
     * 关联的用户
     */
    @OneToOne
    @JoinColumn(name = "user_id_of_send",referencedColumnName = "user_id",insertable = false,updatable = false)
    private BusOvoUser ovoUserOfSend;

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

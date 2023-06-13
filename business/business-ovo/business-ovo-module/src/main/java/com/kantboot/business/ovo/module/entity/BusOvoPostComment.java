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
 * 帖子评论表
 * 用于存储帖子的评论
 * 评论的内容可以是文字、图片
 * @author 方某方
 */
@Table(name="bus_ovo_post_comment")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoPostComment {

    /**
     * 主键
     */
    @Id
    @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
    @GeneratedValue(generator = "snowflakeId")
    @Column(name = "id")
    private Long id;

    /**
     * 帖子id
     */
    @Column(name = "post_id")
    private Long postId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 评论内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 绑定用户
     * 用于存储评论绑定的用户
     */
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",insertable=false, updatable=false)
    private BusOvoUserBind ovoUser;

    /**
     * @的用户
     * 用于存储评论@的用户
     * 一个评论可以@多个用户
     */
    @OneToMany
    @JoinColumn(name = "post_comment_id",referencedColumnName = "id")
    private List<BusOvoPostCommentAtUser> atUsers;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name="gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name="gmt_modified")
    private Date gmtModified;

}








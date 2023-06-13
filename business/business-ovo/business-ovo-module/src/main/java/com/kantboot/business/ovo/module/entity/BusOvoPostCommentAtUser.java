package com.kantboot.business.ovo.module.entity;

import com.kantboot.util.core.jpa.KantbootGenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 帖子评论@的用户表
 * 用于存储帖子评论@的用户
 * @author 方某方
 */
@Table(name="bus_ovo_post_comment_at_user")
@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class BusOvoPostCommentAtUser {

        /**
        * 主键
        */
        @Id
        @GenericGenerator(name = "snowflakeId",strategy = KantbootGenerationType.SNOWFLAKE)
        @GeneratedValue(generator = "snowflakeId")
        @Column(name = "id")
        private Long id;

        /**
        * 帖子评论id
        */
        @Column(name = "post_comment_id")
        private Long postCommentId;

        /**
        * @的用户id
        */
        @Column(name = "at_user_id")
        private Long atUserId;
}

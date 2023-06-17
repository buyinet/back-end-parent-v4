package com.kantboot.business.ovo.module.vo;

import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.entity.BusOvoPostComment;
import lombok.Data;

/**
 * 帖子VO
 * @author 方某方
 */
@Data
public class BusOvoPostCommentVO extends BusOvoPostComment {

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 被@数
     */
    private Long atCount;

    /**
     * 我是否点赞
     */
    private Boolean like;

}

package com.kantboot.business.ovo.module.vo;

import com.kantboot.business.ovo.module.entity.BusOvoPost;
import lombok.Data;

/**
 * 帖子VO
 * @author 方某方
 */
@Data
public class BusOvoPostVO extends BusOvoPost {

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 评论数
     */
    private Long commentCount;

    /**
     * 我是否点赞
     */
    private Boolean like;

}

package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoPostComment;
import org.springframework.data.domain.Page;

import java.util.HashMap;

/**
 * 帖子评论的service
 * 用于处理帖子评论的业务逻辑
 * @author 方某方
 */
public interface IBusOvoPostCommentService {

    /**
     * 根据postId获取评论列表(分页)
     * @param postId 帖子id
     * @param pageNumber 页码
     * @return 评论列表
     */
    HashMap<String, Object> getByPostId(Long postId, Integer pageNumber);


}

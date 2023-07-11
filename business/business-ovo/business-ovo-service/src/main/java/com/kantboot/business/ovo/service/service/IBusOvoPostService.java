package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusOvoPost;

/**
 * 帖子的service
 * 用于处理帖子的业务逻辑
 * @author 方某方
 */
public interface IBusOvoPostService {


    /**
     * 审核帖子
     * @param busOvoPost 帖子
     * @return 帖子
     */
    BusOvoPost audit(BusOvoPost busOvoPost);

    /**
     * 查询默认推荐的帖子
     * @return 帖子列表
     */
    Object getDefaultRecommend();

    /**
     * 获取大于某个id的帖子
     * @param id id
     * @return 帖子列表
     */
    Object getGreaterOfRecommend(Long id);

    /**
     * 获取小于某个id的帖子
     * @param id id
     * @return 帖子列表
     */
    Object getLessOfRecommend(Long id);

    Object getHot();

}

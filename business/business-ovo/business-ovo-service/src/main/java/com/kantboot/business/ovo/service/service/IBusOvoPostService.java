package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
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


    /**
     * 发布帖子
     * @param dto 发布帖子的参数
     * @return 发布的帖子
     */
    BusOvoPost publish(BusOvoPostDTO dto);

    /**
     * 自己的帖子
     * @param id 帖子id,用来查询前后的帖子
     * @return 帖子列表
     */
    Object getSelf(Long id);

    /**
     * 根据id获取帖子
     * @param id id
     * @return 帖子
     */
    BusOvoPost getById(Long id);

    /**
     * 给帖子点赞
     * @param id 帖子id
     */
    BusOvoPost like(Long id);

    /**
     * 给帖子取消点赞
     * @param id 帖子id
     * @return 帖子
     */
    BusOvoPost unLike(Long id);


    /**
     * 获取附近的帖子
     * @param pageNumber 页码
     * @param range 范围
     * @return 附近的帖子
     */
    Object getNear(Long pageNumber,Double range);


    /**
     * 获取关注的帖子
     * @return 帖子列表
     */
    Object getFollowDefault();

    /**
     * 获取小于某个id的帖子
     * @param id id
     * @return 帖子列表
     */
    Object getFollowLess(Long id);

    /**
     * 获取大于某个id的帖子
     * @param id id
     * @return 帖子列表
     */
    Object getFollowGreater(Long id);

}

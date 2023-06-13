package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.vo.BusOvoPostVO;

import java.util.HashMap;

/**
 * 帖子的service
 * 用于处理帖子的业务逻辑
 * @author 方某方
 */
public interface IBusOvoPostService {

    /**
     * 发布帖子
     * @param dto 发布帖子的参数
     * @return 发布的帖子
     */
    BusOvoPost publish(BusOvoPostDTO dto);

    /**
     * 获取自己的帖子
     * @param pageNumber 页码
     * @param sortField 排序字段
     * @param sortOrderBy 排序方式
     * @return 自己的帖子
     */
    HashMap<String,Object> getSelf(Integer pageNumber, String sortField, String sortOrderBy);

    /**
     * 获取推荐的帖子
     * @param pageNumber 页码
     * @param sortField 排序字段
     * @param sortOrderBy 排序方式
     * @return 推荐的帖子
     */
    HashMap<String,Object> getRecommend(Integer pageNumber, String sortField, String sortOrderBy);

    /**
     * 获取附近的帖子
     * @param pageNumber 页码
     * @param range 范围
     * @return 附近的帖子
     */
    HashMap<String,Object> getNear(Integer pageNumber,Double range);

    /**
     * 点赞
     * @param postId 帖子id
     * @return 点赞数
     */
    BusOvoPostVO like(Long postId);

    /**
     * 根据id获取帖子
     * @param id 帖子id
     * @return 帖子
     */
    BusOvoPostVO getById(Long id);

}

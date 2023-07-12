package com.kantboot.business.ovo.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kantboot.business.ovo.module.entity.BusOvoPostLike;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 帖子点赞的mapper
 * @author 方某方
 */
public interface BusOvoPostLikeMapper
extends BaseMapper<BusOvoPostLike>
{

    /**
     * 根据帖子id获取点赞数
     * @param postId 帖子id
     * @return 点赞数
     */
    @Select("select * from bus_ovo_post_like where user_id = #{userId} AND post_id = #{postId}")
    List<BusOvoPostLike> selectByUserIdAndPostId(
            @Param("userId")
            Long userId,
            @Param("postId")
            Long postId);

}

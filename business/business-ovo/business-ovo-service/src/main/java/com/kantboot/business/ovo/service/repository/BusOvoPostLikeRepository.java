package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 帖子点赞的repositor
 * @author 方某方
 */
public interface BusOvoPostLikeRepository extends JpaRepository<BusOvoPostLike,Long> {

    /**
     * 根据帖子id获取点赞数
     * @param postId 帖子id
     * @return 点赞数
     */
    Long countByPostId(Long postId);

    /**
     * 根据用户id和帖子id获取点赞数
     * @param userId 用户id
     * @param postId 帖子id
     * @return 点赞数
     */
    Long countByUserIdAndPostId(Long userId,Long postId);

    /**
     * 根据用户id和帖子id判断是否存在点赞
     * @param userId 用户id
     * @param postId 帖子id
     * @return 是否存在
     */
    Boolean existsBusOvoPostLikeByUserIdAndPostId(Long userId,Long postId);

    /**
     * 根据用户id和帖子id删除点赞
     * @param userId 用户id
     * @param postId 帖子id
     */
    void deleteByUserIdAndPostId(Long userId,Long postId);


    /**
     * 根据用户id和帖子id获取点赞
     * @param userId 用户id
     * @param postId 帖子id
     * @return 点赞
     */
    BusOvoPostLike findByUserIdAndPostId(Long userId,Long postId);

    /**
     * 根据用户id和帖子id获取点赞
     * @param userId 用户id
     * @param postId 帖子id
     * @return 点赞
     */
    List<BusOvoPostLike> findAllByUserIdAndPostId(Long userId, Long postId);

}

package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoPostCommentLike;
import com.kantboot.business.ovo.module.entity.BusOvoPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 帖子评论的repository
 * @author 方某方
 */
public interface BusOvoPostCommentLikeRepository extends JpaRepository<BusOvoPostCommentLike,Long> {

    /**
     * 根据用户id和评论id判断是否存在点赞
     * @param userId 用户id
     * @param commentId 评论id
     * @return 是否存在
     */
    Boolean existsBusOvoPostCommentLikeByUserIdAndCommentId(Long userId,Long commentId);

    /**
     * 根据评论id删除点赞
     * @param commentId 评论id
     * @param userId 用户id
     * @return 点赞
     */
    void deleteByCommentIdAndUserId(Long commentId,Long userId);


    /**
     * 根据用户id和评论id获取点赞
     * @param userId 用户id
     * @param commentId 评论id
     * @return 点赞
     */
    List<BusOvoPostCommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

    /**
     * 根据用户id和评论id获取点赞
     * @param commentId 评论id
     * @return 点赞
     */
    Long countByCommentId(Long commentId);

}

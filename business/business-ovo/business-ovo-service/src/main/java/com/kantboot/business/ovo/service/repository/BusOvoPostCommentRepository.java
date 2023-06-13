package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoPostComment;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 帖子评论的repository
 * @author 方某方
 */
public interface BusOvoPostCommentRepository extends JpaRepository<BusOvoPostComment,Long> {

    /**
     * 根据postId获取评论列表(分页)
     * @param postId 帖子id
     * @param pageable 分页参数
     * @return 评论列表
     */
    Page<BusOvoPostComment> findByPostId(Long postId, Pageable pageable);

    /**
     * 根据postId获取评论数
     * @param postId 帖子id
     * @return 评论数
     */
    Long countByPostId(Long postId);


}

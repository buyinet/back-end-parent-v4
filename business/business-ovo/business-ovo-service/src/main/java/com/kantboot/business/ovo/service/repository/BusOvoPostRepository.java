package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 帖子的repository
 * @author 方某方
 */
public interface BusOvoPostRepository extends JpaRepository<BusOvoPost,Long> {

    /**
     * 获取自己的帖子
     * @param userId 用户id
     * @param pageable 分页参数
     * @return 自己的帖子
     */
    Page<BusOvoPost> findAllByUserId(Long userId, Pageable pageable);
}

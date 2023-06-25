package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoUserFollow;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Ovo用户关注表Repository接口
 * @author 方某方
 */
public interface BusOvoUserFollowRepository
extends JpaRepository<BusOvoUserFollow, Long>
{
    /**
     * 根据用户id和被关注者id查询是否存在
     * @param userId 用户id
     * @param followUserId 被关注者id
     * @return 是否存在
     */
    boolean existsByUserIdAndFollowUserId(Long userId, Long followUserId);

    /**
     * 根据用户id和被关注者id查询
     * @param userId 用户id
     * @param followUserId 被关注者id
     * @return 关注列表
     */
    List<BusOvoUserFollow> findByUserIdAndFollowUserId(Long userId, Long followUserId);

    /**
     * deleteByUserIdAndFollowUserId
     * @param userId 用户id
     * @param followUserId 被关注者id
     * @return 删除数量
     */
    @Transactional
    @Modifying
    @Query("delete from BusOvoUserFollow where userId = ?1 and followUserId = ?2")
    int deleteByUserIdAndFollowUserId(Long userId, Long followUserId);

    /**
     * 查看关注的人的数量
     * @param userId 用户id
     * @return 关注的人的数量
     */
    long countByUserId(Long userId);

    /**
     * 查看粉丝的数量
     * @param followUserId 被关注者id
     * @return 粉丝数量
     */
    long countByFollowUserId(Long followUserId);
}

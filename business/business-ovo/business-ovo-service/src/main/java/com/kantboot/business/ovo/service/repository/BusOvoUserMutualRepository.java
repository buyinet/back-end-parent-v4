package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoUserMutual;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Ovo用户互相关注表Repository接口
 * @author 方某方
 */
public interface BusOvoUserMutualRepository extends JpaRepository<BusOvoUserMutual, Long> {

    List<BusOvoUserMutual> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    /**
     * 根据两个用户id查询是否存在
     * @param user1Id 用户id
     * @param user2Id 互相关注的用户id
     * @return 是否存在
     */
    boolean existsByUser1IdAndUser2Id(Long user1Id, Long user2Id);


    long countByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    /**
     * 根据两个用户id删除
     * @param user1Id 用户id
     * @param user2Id 互相关注的用户id
     */
    void deleteByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    /**
     * 根据用户id查询出互相关注的用户,并分页
     * @param userId 用户id
     * @return 对方用户id
     */
    @Query(
            value= """
SELECT
    id,
    user1_id,
    user2_id,
    gmt_create,
    gmt_modified,
	CASE
		WHEN user1_id = :userId THEN user2_id\s
		WHEN user2_id = :userId THEN user1_id\s
		END AS user_id 
FROM
		bus_ovo_user_mutual\s
WHERE
  user1_id = :userId OR user2_id = :userId
"""
            ,countQuery = "SELECT COUNT(*) FROM bus_ovo_user_mutual WHERE user1_id = :userId OR user2_id = :userId"
            ,nativeQuery = true
    )
    Page<BusOvoUserMutual> findOtherUserIdByUserId(Long userId, Pageable pageable);





}

package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

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


    /**
     * 根据审核状态和大于gmtCreate获取帖子
     * @param auditStatusCode 审核状态编码
     * @param gmtCreate 时间
     * @param pageable 分页参数
     * @return 帖子
     */
    Page<BusOvoPost> findAllByAuditStatusCodeAndGmtCreateGreaterThan(String auditStatusCode, Date gmtCreate, Pageable pageable);


    /**
     * 查看附近的帖子
     * 即按时间倒序，距离正序
     * 每10分钟大于十公里
     * @param pageable 分页
     * @param latitude 纬度
     * @param longitude 经度
     * @param range 范围
     * @return Page<BusOvoUserBindLocation> 附近的帖子
     */
    @Query(value = "SELECT * FROM (" +
            "SELECT *, " +
            "ROUND(6378.138 * 2 * ASIN(SQRT(POW(SIN((:latitude * PI() / 180 - e.latitude_of_select * PI() / 180) / 2), 2) " +
            "+ COS(:latitude * PI() / 180) * COS(e.latitude_of_select * PI() / 180) *" +
            "POW(SIN((:longitude * PI() / 180 - e.longitude_of_select * PI() / 180) / 2), 2)))) " +
            "* 1000 AS distance " +
            "FROM bus_ovo_post e " +
            "WHERE e.latitude_of_select IS NOT NULL " +
            "  AND e.longitude_of_select IS NOT NULL " +
            "  AND e.audit_status_code = 'pass') AS subquery " +
            "WHERE distance <= :range " +
            "ORDER BY CASE WHEN (gmt_create - INTERVAL 10 MINUTE) > NOW() THEN distance ELSE -distance END ASC, gmt_create DESC",
            countQuery = "SELECT COUNT(*) FROM bus_ovo_post " +
                    "WHERE latitude_of_select IS NOT NULL " +
                    "  AND longitude_of_select IS NOT NULL " +
                    "  AND audit_status_code = 'pass'",
            nativeQuery = true)
    Page<BusOvoPost> findAllWithDistance(Pageable pageable, Double latitude, Double longitude, Double range);


    /**
     * 根据用户id获取帖子数量
     * @param userId 用户id
     * @param auditStatusCode 审核状态编码
     * @return 帖子数量
     */
    Long countByUserIdAndAuditStatusCode(Long userId,String auditStatusCode);

    /**
     * 根据gmtAudit获取往上的30个帖子
     * @return 往上的30个帖子
     */
    @Query(value = """
    SELECT * FROM bus_ovo_post 
    WHERE
        gmt_audit > :gmtAudit
        AND audit_status_code = 'pass'
        AND visible_code = 'all'
    ORDER BY gmt_audit ASC LIMIT 30
    """,nativeQuery = true)
    List<BusOvoPost> findAfter(@Param("gmtAudit") Date gmtAudit);

    /**
     * 根据gmtAudit获取往下的30个帖子
     * @return 往下的30个帖子
     */
    List<BusOvoPost> findLast30ByGmtAuditBeforeAndAuditStatusCodeAndVisibleCodeOrderByGmtAuditDesc(Date gmtAudit,String auditStatusCode,String visibleCode);

    /**
     * 获取最新审核通过的30个帖子
     * @return 最新审核通过的30个帖子
     */

    @Query("""
    SELECT p
    FROM BusOvoPost p
    WHERE p.auditStatusCode = :auditStatusCode
    AND p.visibleCode = :visibleCode
    AND EXISTS (
        SELECT 1
        FROM BusOvoPostLike l
        WHERE l.postId = p.id
        AND l.userId = :userId
    )
    ORDER BY p.gmtAudit DESC
    """)
    Page<BusOvoPost> findLatest(
            @Param("auditStatusCode") String auditStatusCode,
            @Param("visibleCode") String visibleCode,
            @Param("userId") Long userId,
            Pageable pageable
    );
}

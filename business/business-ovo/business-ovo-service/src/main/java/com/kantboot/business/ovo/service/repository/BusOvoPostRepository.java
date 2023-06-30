package com.kantboot.business.ovo.service.repository;

import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.entity.BusOvoUserBindLocation;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

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
     * 根据审核状态获取帖子
     * @param auditStatusCode 审核状态编码
     * @param pageable 分页参数
     * @return 帖子
     */
    Page<BusOvoPost> findAllByAuditStatusCode(String auditStatusCode, Pageable pageable);

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

}

package com.kantboot.business.ovo.service.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 帖子的mapper
 * @author 方某方
 */
@Mapper
public interface BusOvoPostMapper {


    /**
     * 查询默认推荐的帖子
     */
    @Select("""
            SELECT
            	(SELECT
            	CONCAT('[',GROUP_CONCAT(file_id),']') FROM bus_ovo_post_image WHERE post_id = a.id) AS imageFileIdArrStr,
            	(SELECT COUNT(*) FROM bus_ovo_post_like WHERE post_id = a.id ) AS likeCount,
            	(SELECT COUNT(*) FROM bus_ovo_post_comment WHERE post_id = a.id) AS commentCount,
            	(SELECT
                    CASE
                        COUNT(*) > 0
                        WHEN TRUE THEN TRUE
                        ELSE FALSE
            				END
              FROM bus_ovo_post_like WHERE post_id = a.id AND user_id = #{userId}) AS likeFlag,
            	a.*,
            	b.*,
            	c.*,
            	d.*,
            	e.longitude AS userLongitude,
            	e.latitude AS userLatitude,
            	e.province AS userProvince,
            	e.city AS userCity,
            	e.district AS userDistrict,
            	(SELECT
            	CONCAT('[',GROUP_CONCAT('"',emotional_orientation_code,'"'),']') FROM rel_bus_ovo_user_and_bus_ovo_emotional_orientation WHERE user_id = a.user_id) AS emotionalOrientationCodeArrStr
            	
            FROM
            	bus_ovo_post a
            LEFT JOIN
            	bus_ovo_user b
            ON
            	a.user_id = b.user_id
            LEFT JOIN
            	sys_user c
            ON
            	a.user_id = c.id
            LEFT JOIN
            	sys_user_online d
            ON
                a.user_id = d.user_id
            LEFT JOIN
                bus_ovo_user_bind_location e
            ON
                a.user_id = e.user_id
            WHERE
                a.audit_status_code = "PASS"
            ORDER BY a.gmt_audit DESC
            LIMIT 30
            """)

    List<Map<String,Object>> getDefaultRecommend(
            @Param("userId") Long userId
    );

    /**
     * 获取大于某个id的帖子
     * @param id id
     * @return List<Map<String,Object>>
     */
    @Select("""
            SELECT
            	(SELECT
            	CONCAT('[',GROUP_CONCAT(file_id),']') FROM bus_ovo_post_image WHERE post_id = a.id) AS imageFileIdArrStr,
            	(SELECT COUNT(*) FROM bus_ovo_post_like WHERE post_id = a.id ) AS likeCount,
            	(SELECT COUNT(*) FROM bus_ovo_post_comment WHERE post_id = a.id) AS commentCount,
            	(SELECT
                    CASE
                        COUNT(*) > 0
                        WHEN TRUE THEN TRUE
                        ELSE FALSE
            				END
              FROM bus_ovo_post_like WHERE post_id = a.id AND user_id = #{userId}) AS likeFlag,
            	a.*,
            	b.*,
            	c.*,
            	d.*,
            	e.longitude AS userLongitude,
            	e.latitude AS userLatitude,
            	e.province AS userProvince,
            	e.city AS userCity,
            	e.district AS userDistrict,
            	(SELECT
            	CONCAT('[',GROUP_CONCAT('"',emotional_orientation_code,'"'),']') FROM rel_bus_ovo_user_and_bus_ovo_emotional_orientation WHERE user_id = a.user_id) AS emotionalOrientationCodeArrStr
            	
            FROM
            	bus_ovo_post a
            LEFT JOIN
            	bus_ovo_user b
            ON
            	a.user_id = b.user_id
            LEFT JOIN
            	sys_user c
            ON
            	a.user_id = c.id
            LEFT JOIN
            	sys_user_online d
            ON
                a.user_id = d.user_id
            LEFT JOIN
                bus_ovo_user_bind_location e
            ON
                a.user_id = e.user_id
            WHERE
                a.audit_status_code = "PASS"
                AND a.gmt_audit >= (SELECT gmt_audit FROM bus_ovo_post WHERE id = #{id})
                AND a.id != #{id}
            ORDER BY a.gmt_audit DESC
            LIMIT 30
            """)
    List<Map<String,Object>> getGreaterOfRecommend(
            @Param("id") Long id,
            @Param("userId") Long userId
    );


    /**
     * 获取小于某个id的帖子
     * @param id id
     * @return List<Map<String,Object>>
     */
    @Select("""
            SELECT
            	(SELECT
            	CONCAT('[',GROUP_CONCAT(file_id),']') FROM bus_ovo_post_image WHERE post_id = a.id) AS imageFileIdArrStr,
            	(SELECT COUNT(*) FROM bus_ovo_post_like WHERE post_id = a.id ) AS likeCount,
            	(SELECT COUNT(*) FROM bus_ovo_post_comment WHERE post_id = a.id) AS commentCount,
            	(SELECT
                    CASE
                        COUNT(*) > 0
                        WHEN TRUE THEN TRUE
                        ELSE FALSE
            				END
              FROM bus_ovo_post_like WHERE post_id = a.id AND user_id = #{userId}) AS likeFlag,
            	a.*,
            	b.*,
            	c.*,
            	d.*,
            	e.longitude AS userLongitude,
            	e.latitude AS userLatitude,
            	e.province AS userProvince,
            	e.city AS userCity,
            	e.district AS userDistrict,
            	(SELECT
            	CONCAT('[',GROUP_CONCAT('"',emotional_orientation_code,'"'),']') FROM rel_bus_ovo_user_and_bus_ovo_emotional_orientation WHERE user_id = a.user_id) AS emotionalOrientationCodeArrStr
            	
            FROM
            	bus_ovo_post a
            LEFT JOIN
            	bus_ovo_user b
            ON
            	a.user_id = b.user_id
            LEFT JOIN
            	sys_user c
            ON
            	a.user_id = c.id
            LEFT JOIN
            	sys_user_online d
            ON
                a.user_id = d.user_id
            LEFT JOIN
                bus_ovo_user_bind_location e
            ON
                a.user_id = e.user_id
            WHERE
                a.audit_status_code = "PASS"
                AND a.gmt_audit <= (SELECT gmt_audit FROM bus_ovo_post WHERE id = #{id})
                AND a.id != #{id}
            ORDER BY a.gmt_audit DESC
            LIMIT 30
            """)
    List<Map<String,Object>> getLessOfRecommend(
            @Param("id") Long id,
            @Param("userId") Long userId
    );
//    ORDER BY (COALESCE(b.likeCount, 0) + COALESCE(c.commentCount, 0)) DESC


    /**
     * 获取热门的帖子
     * @param userId 用户id
     * @return List<Map<String,Object>>
     */
    @Select("""
            SELECT
            	(SELECT
            	CONCAT('[',GROUP_CONCAT(file_id),']') FROM bus_ovo_post_image WHERE post_id = a.id) AS imageFileIdArrStr,
            	(SELECT COUNT(*) FROM bus_ovo_post_like WHERE post_id = a.id ) AS likeCount,
            	(SELECT COUNT(*) FROM bus_ovo_post_comment WHERE post_id = a.id) AS commentCount,
            	(SELECT
                    CASE
                        COUNT(*) > 0
                        WHEN TRUE THEN TRUE
                        ELSE FALSE
            				END
              FROM bus_ovo_post_like WHERE post_id = a.id AND user_id = #{userId}) AS likeFlag,
            	a.*,
            	b.*,
            	c.*,
            	d.*,
            	e.longitude AS userLongitude,
            	e.latitude AS userLatitude,
            	e.province AS userProvince,
            	e.city AS userCity,
            	e.district AS userDistrict,
            	(SELECT
            	CONCAT('[',GROUP_CONCAT('"',emotional_orientation_code,'"'),']') FROM rel_bus_ovo_user_and_bus_ovo_emotional_orientation WHERE user_id = a.user_id) AS emotionalOrientationCodeArrStr
            	
            FROM
            	bus_ovo_post a
            LEFT JOIN
            	bus_ovo_user b
            ON
            	a.user_id = b.user_id
            LEFT JOIN
            	sys_user c
            ON
            	a.user_id = c.id
            LEFT JOIN
            	sys_user_online d
            ON
                a.user_id = d.user_id
            LEFT JOIN
                bus_ovo_user_bind_location e
            ON
                a.user_id = e.user_id
            WHERE
                a.audit_status_code = "PASS"
                AND a.gmt_audit >= DATE_SUB(NOW(), INTERVAL 100 HOUR)
                AND (SELECT COUNT(*) FROM bus_ovo_post_image WHERE post_id = a.id) > 0
                AND a.content IS NOT NULL AND a.content != ""
            ORDER BY (COALESCE(likeCount, 0) + COALESCE(commentCount, 0)) DESC
            LIMIT 3
            """)
    List<Map<String,Object>> getHot(
            @Param("userId") Long userId
    );
}

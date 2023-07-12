package com.kantboot.business.ovo.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kantboot.business.ovo.module.vo.BusOvoUserCharmTopVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作用户魅力值的mapper
 * 用于操作用户魅力值的数据库表
 * @author 方某方
 */
@Mapper
public interface BusOvoUserCharmMapper {

    /**
     * 根据开始时间戳和结束时间戳获取用户魅力值排行榜
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @return 用户魅力值排行榜
     */
    @Select("""
            SELECT
            	a.to_user_id AS userId,
            	(
             		SELECT SUM( charm_value ) AS count
             		FROM bus_ovo_user_gift_detail
             		WHERE to_user_id = a.to_user_id
             		AND UNIX_TIMESTAMP(gmt_create) >= #{startTimestamp}/1000
            	    AND UNIX_TIMESTAMP(gmt_create) < #{endTimestamp}/1000
             	) AS totalCharmValue,
             	
            	b.nickname AS nickname,
             	b.gender AS gender,
             	b.birthday AS birthday,
            	b.file_id_of_avatar AS fileIdOfAvatar,
            	c.introduction AS introduction,
            	c.sadomasochism_attr_code AS sadomasochismAttrCode,
            	c.sexual_orientation_code AS sexualOrientationCode,
            	d.province AS province,
            	d.city AS city,
            	d.longitude AS longitude,
            	d.latitude AS latitude,
            	e.emotional_orientation_code AS emotionalOrientationCode
            FROM
            	bus_ovo_user_charm_detail a
            LEFT JOIN
            	sys_user b
            ON
            	a.to_user_id=b.id
            LEFT JOIN
            	bus_ovo_user c
            ON
            	a.to_user_id = c.user_id
            LEFT JOIN
            	bus_ovo_user_bind_location d
            ON
            	a.to_user_id = d.user_id
            LEFT JOIN
            	rel_bus_ovo_user_and_bus_ovo_emotional_orientation e
            ON
             a.to_user_id = e.user_id
            WHERE
            	UNIX_TIMESTAMP(a.gmt_create) >= #{startTimestamp}/1000
            	AND UNIX_TIMESTAMP(a.gmt_create) < #{endTimestamp}/1000
            GROUP BY
            	userId
            ORDER BY
            	totalCharmValue DESC
            LIMIT 30
            """)
    List<BusOvoUserCharmTopVO> getTopByTimestamp(
            @Param("startTimestamp") Long startTimestamp,
            @Param("endTimestamp") Long endTimestamp);

    /**
     * 根据开始时间戳和结束时间戳和用户id获取用户魅力值排行榜
     * @param userId 用户id
     * @param startTimestamp 开始时间戳
     * @param endTimestamp 结束时间戳
     * @return 用户魅力值排行榜
     */
    @Select("""
            SELECT
            	a.to_user_id AS userId,
            	(
             		SELECT SUM( charm_value ) AS count
             		FROM bus_ovo_user_gift_detail
             		WHERE to_user_id = a.to_user_id
             		AND UNIX_TIMESTAMP(gmt_create) >= #{startTimestamp}/1000
            	    AND UNIX_TIMESTAMP(gmt_create) < #{endTimestamp}/1000
             	) AS totalCharmValue
            FROM
            	bus_ovo_user_charm_detail a
            WHERE
            	UNIX_TIMESTAMP(a.gmt_create) >= #{startTimestamp}/1000
            	AND UNIX_TIMESTAMP(a.gmt_create) < #{endTimestamp}/1000
            	AND a.to_user_id = #{userId}
            GROUP BY userId
            """)

    BusOvoUserCharmTopVO getTopByTimestampAndUserId(
            @Param("startTimestamp") Long startTimestamp,
            @Param("endTimestamp") Long endTimestamp,
            @Param("userId") Long userId);


    /**
     * 根据用户id获取ta的守护者
     * @param userId 用户id
     * @return ta的守护者
     */
    @Select("""
            SELECT
            	a.from_user_id AS userId,
            	b.nickname AS nickname,
            	(
            	    SELECT SUM( a1.charm_value ) FROM bus_ovo_user_gift_detail a1 WHERE
            	    a1.to_user_id = #{userId} 
            	    AND a1.from_user_id = a.from_user_id GROUP BY a1.from_user_id ) 
            	AS totalCharmValue,
            	b.nickname AS nickname,
            	b.nickname AS nickname,
             	b.gender AS gender,
             	b.birthday AS birthday,
            	b.file_id_of_avatar AS fileIdOfAvatar,
            	c.introduction AS introduction,
            	c.sadomasochism_attr_code AS sadomasochismAttrCode,
            	c.sexual_orientation_code AS sexualOrientationCode,
            	d.province AS province,
            	d.city AS city,
            	d.longitude AS longitude,
            	d.latitude AS latitude,
            	e.emotional_orientation_code AS emotionalOrientationCode
            FROM
            	bus_ovo_user_charm_detail a
            LEFT JOIN
            	sys_user b
            ON
            	a.from_user_id=b.id
            LEFT JOIN
            	bus_ovo_user c
            ON
            	a.from_user_id = c.user_id
            LEFT JOIN
            	bus_ovo_user_bind_location d
            ON
            	a.from_user_id = d.user_id
            LEFT JOIN
            	rel_bus_ovo_user_and_bus_ovo_emotional_orientation e
            ON
             a.from_user_id = e.user_id  
            WHERE to_user_id = #{userId}
            
            GROUP BY
            	a.from_user_id
            ORDER BY
            	totalCharmValue DESC
            LIMIT 3
            """)
    List<BusOvoUserCharmTopVO> getGuardiansByUserId(@Param("userId") Long userId);




}

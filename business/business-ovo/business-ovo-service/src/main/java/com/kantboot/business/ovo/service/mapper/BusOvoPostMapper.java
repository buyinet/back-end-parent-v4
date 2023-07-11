package com.kantboot.business.ovo.service.mapper;

import com.kantboot.business.ovo.module.vo.BusOvoPostVO;
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
            	CONCAT('[',GROUP_CONCAT(file_id),']') FROM bus_ovo_post_image WHERE post_id = a.id) AS imageList,
            	(SELECT COUNT(*) FROM bus_ovo_post_like WHERE post_id = a.id ) AS likeCount,
            	(SELECT COUNT(*) FROM bus_ovo_post_comment WHERE post_id = a.id) AS commentCount,
            	(SELECT
                    CASE
                        COUNT(*) > 0
                        WHEN TRUE THEN TRUE
                        ELSE FALSE
            				END
              FROM bus_ovo_post_like WHERE post_id = a.id AND user_id = #{userId}) AS likeFalg,
            	a.*,
            	b.*,
            	c.*
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
            ORDER BY a.id DESC
            LIMIT 30
            """)

    List<Map<String,Object>> getDefaultRecommend(
            @Param("userId") Long userId
    );

}

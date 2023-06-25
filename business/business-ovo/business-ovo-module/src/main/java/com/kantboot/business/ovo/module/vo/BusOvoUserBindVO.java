package com.kantboot.business.ovo.module.vo;

import com.kantboot.business.ovo.module.entity.BusOvoUserBind;
import lombok.Data;

/**
 * Ovo用户绑定VO
 * @author 方某方
 */
@Data
public class BusOvoUserBindVO extends BusOvoUserBind {

    /**
     * 粉丝数
     */
    private Long followersCount;

    /**
     * 关注数
     */
    private Long followingCount;

    /**
     * 帖子数
     */
    private Long postCount;

}

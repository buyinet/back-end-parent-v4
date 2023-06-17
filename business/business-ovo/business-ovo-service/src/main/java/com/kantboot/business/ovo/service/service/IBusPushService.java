package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.entity.BusPush;

import java.util.List;

/**
 * Ovo用户推送表Service接口
 * @author 方某方
 */
public interface IBusPushService {

    /**
     * cid绑定用户
     * @param cid cid
     */
    void bind(String cid);

    /**
     * 根据用户id查询推送
     * @param userId 用户id
     */
    List<BusPush> getByUserId(Long userId);
}

package com.kantboot.business.ovo.service.service;

import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.module.entity.BusOvoPost;

/**
 * 帖子的service
 * 用于处理帖子的业务逻辑
 * @author 方某方
 */
public interface IBusOvoPostService {

    /**
     * 发布帖子
     * @param dto 发布帖子的参数
     * @return 发布的帖子
     */
    BusOvoPost publish(BusOvoPostDTO dto);

}

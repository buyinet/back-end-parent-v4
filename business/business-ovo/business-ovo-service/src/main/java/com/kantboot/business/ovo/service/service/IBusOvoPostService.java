package com.kantboot.business.ovo.service.service;

import cats.kernel.Hash;
import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.module.entity.BusOvoPost;
import com.kantboot.business.ovo.module.vo.BusOvoPostVO;
import com.kantboot.util.common.result.PageResult;

import java.util.HashMap;
import java.util.List;

/**
 * 帖子的service
 * 用于处理帖子的业务逻辑
 * @author 方某方
 */
public interface IBusOvoPostService {


    /**
     * 审核帖子
     * @param busOvoPost 帖子
     * @return 帖子
     */
    BusOvoPost audit(BusOvoPost busOvoPost);


}

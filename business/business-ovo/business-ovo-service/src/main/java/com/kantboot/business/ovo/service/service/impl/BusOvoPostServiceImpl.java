package com.kantboot.business.ovo.service.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.kantboot.api.service.ITencentApiLocationService;
import com.kantboot.business.ovo.module.dto.BusOvoPostDTO;
import com.kantboot.business.ovo.module.entity.*;
import com.kantboot.business.ovo.module.vo.BusOvoPostVO;
import com.kantboot.business.ovo.service.mapper.BusOvoPostMapper;
import com.kantboot.business.ovo.service.repository.BusOvoPostCommentRepository;
import com.kantboot.business.ovo.service.repository.BusOvoPostLikeRepository;
import com.kantboot.business.ovo.service.repository.BusOvoPostRepository;
import com.kantboot.business.ovo.service.service.IBusOvoPostService;
import com.kantboot.business.ovo.service.service.IBusOvoUserService;
import com.kantboot.system.service.ISysUserService;
import com.kantboot.util.common.exception.BaseException;
import com.kantboot.util.common.result.PageResult;
import com.kantboot.util.core.redis.RedisUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 帖子的service
 * 用于处理帖子的业务逻辑
 * @author 方某方
 */
@Service
public class BusOvoPostServiceImpl implements IBusOvoPostService {

    @Resource
    private BusOvoPostRepository repository;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private BusOvoPostMapper mapper;


    private Long getUserId(){
        try {
            return sysUserService.getSelf().getId();
        } catch (BaseException e) {
            return null;
        }

    }



    @Override
    public BusOvoPost audit(BusOvoPost busOvoPost) {
        BusOvoPost post = repository.findById(busOvoPost.getId()).get();
        post.setAuditStatusCode(busOvoPost.getAuditStatusCode());
        post.setGmtAudit(new Date());

        if(busOvoPost.getAuditStatusCode().equals("reject")){
            post.setAuditRejectReason(busOvoPost.getAuditRejectReason());
        }

        BusOvoPost save = repository.save(post);
        return save;
    }

    @Override
    public Object getDefaultRecommend() {
        List<Map<String, Object>> defaultRecommend = mapper.getDefaultRecommend(sysUserService.getIdOfSelf());
        System.out.println(JSON.toJSONString(defaultRecommend));
        return defaultRecommend;
    }
}


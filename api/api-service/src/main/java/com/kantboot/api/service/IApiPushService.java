package com.kantboot.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.module.ApiPush;

/**
 * 推送服务接口
 * @author 方某方
 */
public interface IApiPushService {

    /**
     * 推送
     * @param entity 推送实体
     */
    JSONObject push(ApiPush entity);
}

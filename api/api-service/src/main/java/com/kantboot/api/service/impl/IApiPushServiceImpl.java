package com.kantboot.api.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.module.ApiPush;
import com.kantboot.api.service.IApiPushService;
import com.kantboot.api.util.uni.push.PushUtil;
import com.kantboot.system.service.ISysSettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 推送服务接口实现类
 * @author 方某方
 */
@Service
public class IApiPushServiceImpl implements IApiPushService {

    @Resource
    private ISysSettingService settingService;

    @Override
    public JSONObject push(ApiPush entity) {
        String url = settingService.getValue("uniPush", "url");

        Map<String, Object> map = new HashMap<>();
        map.put("cid", entity.getCid());
        map.put("forceNotification", entity.getForceNotification());
        map.put("title", entity.getTitle());
        map.put("content", entity.getContent());
        map.put("payload", entity.getPayload());
        return PushUtil.sendPush(url, map);


    }
}

package com.kantboot.api.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.service.IApiLocationService;
import com.kantboot.api.util.position.LocationApiUtil;
import com.kantboot.system.service.ISysSettingService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 位置服务实现类
 * @author 方某方
 */
@Service
public class ApiLocationServiceImpl implements IApiLocationService {

    @Resource
    private ISysSettingService settingService;

    @Override
    public JSONObject getLocationInfo(Double latitude, Double longitude, Integer poi) {
        String key = settingService.getValue("location", "tencentMapApiKey");
        JSONObject locationInfo = LocationApiUtil.getLocationInfo(latitude, longitude, key, poi);
        return locationInfo.getJSONObject("result");
    }

    @Override
    public JSONObject getLocationInfoByIp(String ip) {
        String key = settingService.getValue("location", "tencentMapApiKey");
        JSONObject locationInfo = LocationApiUtil.getLocationInfoByIp(ip, key);
        System.out.println(locationInfo);
        return locationInfo.getJSONObject("result");
    }
}

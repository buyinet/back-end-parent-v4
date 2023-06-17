package com.kantboot.api.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.service.IApiLocationService;
import com.kantboot.api.util.location.LocationApiUtil;
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
        return locationInfo.getJSONObject("result");
    }

    @Override
    public JSONArray getLocationInfoByRange(Double latitude, Double longitude, Double range, Integer expand) {
        String key = settingService.getValue("location", "tencentMapApiKey");
        JSONObject locationInfo = LocationApiUtil.getLocationInfoByRange(latitude, longitude, range, expand, key);
        return locationInfo.getJSONArray("data");
    }

    @Override
    public JSONObject getLocationInfoById(String id) {
        String key = settingService.getValue("location", "tencentMapApiKey");
        JSONObject locationInfo = LocationApiUtil.getLocationInfoById(id, key);
        System.out.println(locationInfo);
        return locationInfo.getJSONArray("data").getJSONObject(0);
    }
}

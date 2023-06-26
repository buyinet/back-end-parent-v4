package com.kantboot.api.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.kantboot.api.service.ITencentApiLocationService;
import com.kantboot.api.util.location.LocationApiUtil;
import com.kantboot.system.service.ISysSettingService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 位置服务实现类
 * @author 方某方
 */
@Slf4j
@Service
public class TencentApiLocationServiceImpl implements ITencentApiLocationService {

    @Resource
    private ISysSettingService settingService;

    @Override
    public JSONObject getLocationInfo(Double latitude, Double longitude, Integer poi) {
        String key = settingService.getValue("location", "tencentMapApiKey");
        JSONObject locationInfo = LocationApiUtil.getLocationInfo(latitude, longitude, key, poi);
        log.info("腾讯地图api:{}", locationInfo);
        return locationInfo.getJSONObject("result");
    }

    @Override
    public JSONObject getLocationInfo2(Double latitude, Double longitude, Integer poi, String key) {
        JSONObject locationInfo = LocationApiUtil.getLocationInfo(latitude, longitude, key, poi);
        log.info("腾讯地图api:{}", locationInfo);
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
        return locationInfo.getJSONArray("data").getJSONObject(0);
    }
}

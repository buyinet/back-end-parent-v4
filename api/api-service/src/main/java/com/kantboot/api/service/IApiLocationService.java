package com.kantboot.api.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * 位置服务
 * @author 方某方
 */
public interface IApiLocationService {


    /**
     * 腾讯地图api
     * @url https://apis.map.qq.com/ws/geocoder/v1/?location=${latitude},${longitude}&key=${key}&get_poi=${poi}
     * @param latitude 纬度
     * @param longitude 经度
     * @param poi 是否返回周边poi列表：1.返回；0.不返回(默认)
     * @return 位置信息
     */
    JSONObject getLocationInfo(Double latitude, Double longitude, Integer poi);

    /**
     * 百度地图api
     * @url https://apis.map.qq.com/ws/location/v1/ip?ip=${ip}&key=${key}
     * @param ip ip地址
     * @return 位置信息
     */
    JSONObject getLocationInfoByIp(String ip);
}
